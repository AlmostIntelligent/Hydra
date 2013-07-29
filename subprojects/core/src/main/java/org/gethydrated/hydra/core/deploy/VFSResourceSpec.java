package org.gethydrated.hydra.core.deploy;

import org.gethydrated.hydra.api.service.deploy.ResourceSpec;
import org.jboss.vfs.VirtualFile;

/**
 *
 */
public class VFSResourceSpec implements ResourceSpec<VirtualFile> {

    private final VirtualFile root;

    public VFSResourceSpec(VirtualFile file) {
        root = file;
    }

    @Override
    public VirtualFile getResource() {
        return root;
    }
}
