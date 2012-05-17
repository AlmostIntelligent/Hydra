package org.gethydrated.hydra.api.service;

import java.io.InputStream;
import java.io.PrintStream;

public interface ServiceContext extends ServiceApi {

    Service getService();
    
    PrintStream getOutputStream();
    
    InputStream getInputStream();
}
