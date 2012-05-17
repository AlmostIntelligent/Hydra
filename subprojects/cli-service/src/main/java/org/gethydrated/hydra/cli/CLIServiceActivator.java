package org.gethydrated.hydra.cli;

import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLIServiceActivator implements ServiceActivator {

        /**
         * 
         */
        private CLIService cli;

        @Override
        public final void start(final ServiceContext context) throws Exception {
                cli = new CLIService(context, null);
                cli.handleInput();
                System.out.println("CLI Service Start");
        }

        @Override
        public final void stop(final ServiceContext context) throws Exception {
                System.out.println("CLI Service Stop");
        }

}
