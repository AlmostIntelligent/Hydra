package org.gethydrated.hydra.api.service.deploy;

/**
 *
 */
public class ServiceSpec {

    private final String name;

    private final String version;

    private final String activator;

    private ArchiveSpec archive;

    private ServiceSpec(String name, String version, String activator) {
        this.name = name;
        this.version = version;
        this.activator = activator;
    }

    public void setArchive(ArchiveSpec archive) {
        this.archive = archive;
    }

    public ArchiveSpec getArchive() {
        return archive;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getActivator() {
        return activator;
    }

    @Override
    public String toString() {
        return "ServiceSpec{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", activator='" + activator + '\'' +
                '}';
    }

    public static Builder build() {
        return new Builder() {
            String name;
            String version;
            String activator;

            @Override
            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            @Override
            public Builder setVersion(String version) {
                this.version = version;
                return this;
            }

            @Override
            public Builder setActivator(String activator) {
                this.activator = activator;
                return this;
            }

            @Override
            public ServiceSpec create() {
                return new ServiceSpec(name, version, activator);
            }
        };
    }

    public interface Builder {

        Builder setName(String name);

        Builder setVersion(String version);

        Builder setActivator(String activator);

        ServiceSpec create();
    }
}
