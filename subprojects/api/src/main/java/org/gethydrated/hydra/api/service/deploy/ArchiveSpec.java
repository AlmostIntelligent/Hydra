package org.gethydrated.hydra.api.service.deploy;

import java.util.*;

/**
 * Archive specification.
 */
public class ArchiveSpec {

    private final String archiveName;
    private final String version;
    private final Set<ServiceSpec> serviceSpecs;
    private final Set<ResourceSpec> resourceSpecs;
    private final Set<DependencySpec> dependencySpecs;
    private final Map<String, String> props;

    private ArchiveSpec(String archiveName, String version, Set<ServiceSpec> serviceSpecs, Set<ResourceSpec> resourceSpecs,
                        Set<DependencySpec> dependencySpecs, Map<String, String> props) {
        this.archiveName = archiveName;
        this.version = version;
        this.serviceSpecs = serviceSpecs;
        this.resourceSpecs = resourceSpecs;
        this.dependencySpecs = dependencySpecs;
        this.props = props;
        for (ServiceSpec spec : serviceSpecs) {
            spec.setArchive(this);
        }
    }

    public String getArchiveName() {
        return archiveName;
    }

    public String getVersion() {
        return version;
    }

    public Set<ServiceSpec> getServiceSpecs() {
        return Collections.unmodifiableSet(serviceSpecs);
    }

    public Set<ResourceSpec> getResourceSpecs() {
        return Collections.unmodifiableSet(resourceSpecs);
    }

    public Set<DependencySpec> getDependencySpecs() {
        return Collections.unmodifiableSet(dependencySpecs);
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(props);
    }

    @Override
    public String toString() {
        return "ArchiveSpec{" +
                "archiveName='" + archiveName + '\'' +
                ", version='" + version + '\'' +
                ", serviceSpecs=" + serviceSpecs +
                ", resourceSpecs=" + resourceSpecs +
                ", dependencySpecs=" + dependencySpecs +
                ", props=" + props +
                '}';
    }

    /**
     * Returns a new specification builder.
     *
     * @return new spec builder.
     */
    public static Builder build() {
        return new Builder() {

            private String name;
            private String version;
            private final Set<ServiceSpec> serviceSpecs = new HashSet<>();
            private final Set<ResourceSpec> resourceSpecs = new HashSet<>();
            private final Set<DependencySpec> dependencySpecs = new HashSet<>();
            private final Map<String, String> props = new HashMap<>();

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setVersion(String version) {
                this.version = version;
                return this;
            }

            @Override
            public Builder addService(ServiceSpec spec) {
                serviceSpecs.add(spec);
                return this;
            }

            @Override
            public Builder addResource(ResourceSpec spec) {
                resourceSpecs.add(spec);
                return this;
            }

            @Override
            public Builder addDependency(DependencySpec spec) {
                dependencySpecs.add(spec);
                return this;
            }

            @Override
            public Builder addProperty(String name, String value) {
                props.put(name, value);
                return this;
            }

            @Override
            public ArchiveSpec create() {
                return new ArchiveSpec(name, version, serviceSpecs, resourceSpecs, dependencySpecs, props);
            }
        };
    }

    public interface Builder {

        Builder setName(String name);

        Builder setVersion(String version);

        /**
         * Adds a new service specification.
         *
         * @param spec service specification.
         * @return this builder.
         */
        Builder addService(ServiceSpec spec);

        /**
         * Adds a new resource for classloading and resource resolution. Every
         * resource is available for every service this archive contains.
         *
         * @param spec resource specification.
         * @return this builder.
         */
        Builder addResource(ResourceSpec spec);

        /**
         * Adds a new Dependency for all services of this archive.
         *
         * @param spec dependency specification.
         * @return this builder.
         */
        Builder addDependency(DependencySpec spec);

        /**
         * Adds a property to the archive specification. Every added property
         * is available via the hydra configuration api for every service the
         * archive contains.
         *
         * @param name property name.
         * @param value property value.
         * @return this builder.
         */
        Builder addProperty(String name, String value);


        /**
         * Creates the resulting archive specification.
         * @return new archive specification.
         */
        ArchiveSpec create();
    }
}
