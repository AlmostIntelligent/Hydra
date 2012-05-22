package org.gethydrated.hydra.core.service.locator;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Locator chain.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ChainedServiceLocator implements ServiceLocator {

    /**
     * Child locators.
     */
    private List<ServiceLocator> childs = new LinkedList<>();
    
    /**
     * Appends an locator to the child list.
     * 
     * @param sl
     *            appended ServiceLocator
     */
    public final void append(final ServiceLocator sl) {
        if (sl != null) {
            childs.add(sl);
        }
    }

    @Override
    public final URL locate(final String name) {
        return null;
    }

    @Override
    public final URL locate(final String name, final String version) {
        // TODO Auto-generated method stub
        return null;
    }
}
