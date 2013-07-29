package org.gethydrated.hydra.core.deploy;

import org.jboss.modules.*;
import org.jboss.vfs.*;
import org.jboss.vfs.util.FilterVirtualFileVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Manifest;

/**
 *
 */
public class VFSResourceLoader extends AbstractResourceLoader {

    private final Logger logger = LoggerFactory.getLogger(VFSResourceLoader.class);
    private final VirtualFile root;
    private final URL rootUrl;
    private final Manifest manifest;
    private final String rootName;

    public VFSResourceLoader(VirtualFile root, String rootName) throws IOException {
        this.root = root;
        this.rootName = rootName;
        this.rootUrl = root.toURL();
        this.manifest = VFSUtils.getManifest(root);
    }

    @Override
    public String getRootName() {
        return rootName;
    }

    @Override
    public ClassSpec getClassSpec(String fileName) throws IOException {
        logger.trace("loading class '{}'", fileName);
        final VirtualFile file = root.getChild(fileName);
        if (!file.exists()) {
            return null;
        }
        final long size = file.getSize();
        final ClassSpec spec = new ClassSpec();
        final InputStream is = file.openStream();
        try {
            if (size <= Integer.MAX_VALUE) {
                final int castSize = (int) size;
                byte[] bytes = new byte[castSize];
                int a = 0, res;
                while((res = is.read(bytes, a, castSize - a)) > 0) {
                    a += res;
                }
                is.close();
                spec.setBytes(bytes);
                spec.setCodeSource(new CodeSource(rootUrl, file.getCodeSigners()));
                return spec;
            } else {
                throw new IOException("Resource too large.");
            }
        } finally {
            VFSUtils.safeClose(is);
        }
    }

    @Override
    public PackageSpec getPackageSpec(String name) throws IOException {
        return getPackageSpec(name, this.manifest, this.rootUrl);
    }

    @Override
    public Resource getResource(String name) {

        final VirtualFile file = root.getChild(PathUtils.canonicalize(name));
        if (!file.exists()) {
            return null;
        }
        try {
            return new VFSEntryResource(file.getPathNameRelativeTo(root), file, file.toURL());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getLibrary(String name) {
        return null;
    }

    @Override
    public Collection<String> getPaths() {
        final List<String> index = new ArrayList<String>();
        // First check for an index file
        final VirtualFile indexFile = VFS.getChild(root.getPathName() + ".index");
        if (indexFile.exists()) {
            try {
                final BufferedReader r = new BufferedReader(new InputStreamReader(indexFile.openStream()));
                try {
                    String s;
                    while ((s = r.readLine()) != null) {
                        index.add(s.trim());
                    }
                    return index;
                } finally {
                    // if exception is thrown, undo index creation
                    r.close();
                }
            } catch (IOException e) {
                index.clear();
            }
        }

        FilterVirtualFileVisitor visitor = new FilterVirtualFileVisitor(new VirtualFileFilter() {
            @Override
            public boolean accepts(VirtualFile file) {
                return file.isDirectory();
            }
        }, VisitorAttributes.RECURSE);
        try {
            root.visit(visitor);
        } catch (IOException e) {
            e.printStackTrace();
            index.clear();
        }

        index.add("");
        for (VirtualFile dir : visitor.getMatched()) {
            index.add(dir.getPathNameRelativeTo(root));
        }
        return index;
    }

    static class VFSEntryResource implements Resource {
        private final String name;
        private final VirtualFile entry;
        private final URL resourceURL;

        VFSEntryResource(final String name, final VirtualFile entry, final URL resourceURL) {
            this.name = name;
            this.entry = entry;
            this.resourceURL = resourceURL;
        }

        public String getName() {
            return name;
        }

        public URL getURL() {
            return resourceURL;
        }

        public InputStream openStream() throws IOException {
            return entry.openStream();
        }

        public long getSize() {
            final long size = entry.getSize();
            return size == -1 ? 0 : size;
        }
    }
}
