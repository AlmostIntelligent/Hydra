package org.gethydrated.hydra.api.service.deploy;

/**
 *
 */
public class ModuleDependency implements DependencySpec {

    private String name;

    public ModuleDependency(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
