package org.gethydrated.hydra.core.deploy;

import org.gethydrated.hydra.api.service.deploy.ArchiveSpec;
import org.gethydrated.hydra.api.service.deploy.ArchiveSpec.Builder;
import org.gethydrated.hydra.api.service.deploy.ServiceResolver;
import org.gethydrated.hydra.api.service.deploy.ServiceSpec;
import org.gethydrated.hydra.core.xml.ArchiveReader;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.util.FilterVirtualFileVisitor;
import org.jboss.vfs.util.SuffixMatchFilter;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 *
 */
public class FileBaseResolver implements ServiceResolver {

    private final VirtualFile root;

    private TempFileProvider provider;

    private final Set<Closeable> mounts = new HashSet<>();

    private final Map<String, ArchiveSpec> archives = new HashMap<>();

    private boolean closed = false;

    public FileBaseResolver(String fileBase) {

        if (fileBase == null || fileBase.equals("")) {
            throw new IllegalArgumentException("Illegal resolver base directory: '" + fileBase + "'");
        }
        root = VFS.getChild(fileBase);
        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalStateException("File URL doesn't exist or is no directory.");
        }
        FilterVirtualFileVisitor visitor = new FilterVirtualFileVisitor(new SuffixMatchFilter("jar"));
        try {
            provider = TempFileProvider.create("tmp", Executors.newScheduledThreadPool(2));
            root.visit(visitor);
            for (VirtualFile f : visitor.getMatched()) {
                scanJar(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArchiveSpec resolveArchive(String name) {
        return resolveArchive(name, null);
    }

    @Override
    public ArchiveSpec resolveArchive(String name, String version) {
        return archives.get(name);
    }

    @Override
    public ServiceSpec resolveService(String archive, String name) {
        return resolveService(archive, null,  name, null);
    }

    @Override
    public ServiceSpec resolveService(String archive, String archiveVersion, String name, String version) {
        ArchiveSpec archiveSpec = resolveArchive(archive, archiveVersion);
        if (archiveSpec == null) {
            return null;
        }
        for (ServiceSpec spec : archiveSpec.getServiceSpecs()) {
            if (spec.getName().equals(name) && (version == null || version.equals(spec.getVersion()))) {
                return spec;
            }
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        for(Closeable c : mounts) {
            c.close();
        }
        closed = true;
    }

    private void scanJar(VirtualFile f) throws IOException {
        Closeable handle = VFS.mountZip(f, f, provider);
        VirtualFile archiveInfo = f.getChild("HYDRA-INF/archive.xml");
        if (archiveInfo.exists()) {
            try {
                ArchiveReader reader = new ArchiveReader();
                Builder spec = reader.parse(archiveInfo.openStream());
                spec.addResource(new VFSResourceSpec(f));
                ArchiveSpec sp = spec.create();
                archives.put(sp.getArchiveName(), sp);
                mounts.add(handle);
            } catch (IOException e){
                handle.close();
                throw e;
            }
        } else {
            handle.close();
        }
    }
}
