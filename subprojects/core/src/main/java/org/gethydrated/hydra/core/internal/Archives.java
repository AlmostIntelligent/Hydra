package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.deploy.*;
import org.gethydrated.hydra.core.deploy.VFSResourceLoader;
import org.jboss.modules.*;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.ModuleSpec.Builder;
import org.jboss.modules.filter.PathFilters;
import org.jboss.vfs.VirtualFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Archives manager.
 */
public class Archives extends ModuleLoader {

    public static final String MODULE_PREFIX = "service.";

    private final Configuration cfg;

    private final ConcurrentMap<String, Archive> archives = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<ModuleIdentifier, ModuleSpec> modules = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(Archives.class);

    private final Set<ServiceResolver> resolvers = new HashSet<>();

    private final ModuleLoader parent;
    /**
     * Constructor.
     * @param cfg Hydra configuration.
     * @param resolvers
     */
    public Archives(final Configuration cfg, Set<ServiceResolver> resolvers, ModuleLoader parent) {
        this.cfg = cfg;
        this.resolvers.addAll(resolvers);
        this.parent = parent;
    }

    /**
     * Returns a service for a given name.
     * @param serviceName service name.
     * @return service.
     */
    public Service getService(final String serviceName) throws IOException, ModuleLoadException {
        final String[] arr = serviceName.split("::");
        if (arr.length != 2) {
            logger.error(
                    "Servicename didnt match pattern 'archive::service': {}",
                    serviceName);
            return null;
        }
        Archive archive = getArchive(arr[0]);
        if (archive != null) {
            return archive.getService(arr[1]);
        }
        return null;
    }


    /**
     * Returns a service archive for a given name.
     * @param archiveName archive name.
     * @return service archive.
     */
    private Archive getArchive(final String archiveName) throws IOException, ModuleLoadException {
        if (archives.containsKey(archiveName)) {
            return archives.get(archiveName);
        }
        logger.debug("looking up '{}'", archiveName);
        ArchiveSpec spec = null;
        for (ServiceResolver r: resolvers) {
            spec = r.resolveArchive(archiveName);
            if (spec != null) {
                break;
            }
        }
        logger.debug("found: {}", spec);
        return (spec == null) ? null : buildArchive(spec);
    }

    private Archive buildArchive(ArchiveSpec spec) throws IOException, ModuleLoadException {
        Archive archive = new Archive();
        archive.setName(spec.getArchiveName());
        archive.setVersion(spec.getVersion());
        for (ServiceSpec s : spec.getServiceSpecs()) {
            Service service = new Service();
            service.setName(s.getName());
            service.setVersion(s.getVersion());
            service.setActivator(s.getActivator());
            service.setArchive(archive);
            archive.addService(service);
        }
        Builder builder = ModuleSpec.build(ModuleIdentifier.fromString(MODULE_PREFIX + spec.getArchiveName()));
        for (ResourceSpec r : spec.getResourceSpecs()) {
            VirtualFile f = (VirtualFile) r.getResource();
            builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(new VFSResourceLoader(f, f.getName())));
        }
        builder.addDependency(DependencySpec.createLocalDependencySpec(PathFilters.acceptAll(), PathFilters.acceptAll()));
        builder.addDependency(DependencySpec.createModuleDependencySpec(PathFilters.acceptAll(), ModuleIdentifier.fromString("org.gethydrated.hydra.api"), false));
        builder.addDependency(DependencySpec.createModuleDependencySpec(PathFilters.acceptAll(), ModuleIdentifier.fromString("javax.api"), false));
        builder.addDependency(DependencySpec.createModuleDependencySpec(PathFilters.acceptAll(), ModuleIdentifier.fromString("org.slf4j.slf4j-api"), false));
        for (org.gethydrated.hydra.api.service.deploy.DependencySpec d : spec.getDependencySpecs()) {
            if (d instanceof ModuleDependency) {
                builder.addDependency(DependencySpec.createModuleDependencySpec(PathFilters.acceptAll(), ModuleIdentifier.fromString(d.getName()), false));
            }
        }

        modules.put(builder.getIdentifier(),builder.create());
        Module m = loadModule(builder.getIdentifier());
        relink(m);
        archive.setModule(loadModule(builder.getIdentifier()));
        return archive;
    }

    @Override
    protected Module preloadModule(final ModuleIdentifier identifier) throws ModuleLoadException {
        if (isServiceModule(identifier)) {
            return super.preloadModule(identifier);
        } else {
            return preloadModule(identifier, parent);
        }
    }

    @Override
    public ModuleSpec findModule(final ModuleIdentifier identifier) throws ModuleLoadException {
        return modules.get(identifier);
    }

    public static boolean isServiceModule(ModuleIdentifier identifier) {
        return identifier.getName().startsWith(MODULE_PREFIX);
    }
}
