package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HydraImpl implements Hydra {

    private static final Logger logger = LoggerFactory.getLogger(Hydra.class);

    private final ShutdownHook shutdownhook = new ShutdownHook(this);

    private final Configuration cfg;

    public HydraImpl(Configuration cfg) {
        this.cfg = cfg;
    }

    @Override
	public void start() {
        logger.info("Starting Hydra.");
        shutdownhook.register();
    }

    @Override
	public void stop() {
        logger.info("Stopping Hydra.");
        shutdownhook.unregister();
    }

    @Override
    public void startService() throws HydraException {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopService() throws HydraException {
        // TODO Auto-generated method stub

    }

}
