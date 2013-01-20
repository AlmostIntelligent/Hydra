package org.gethydrated.hydra.util.xml;

import java.io.InputStream;

/**
 *
 */
public interface XMLReader<T> {

    T parse(InputStream inputStream);

}
