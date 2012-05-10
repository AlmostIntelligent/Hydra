package org.gethydrated.hydra.api;

public interface HydraApi {

	public void startService() throws HydraException;
	
	public void stopService() throws HydraException;
	
	public void registerLocal();
	
	public void registerGlobal();
	
	public void getLocalService();
	
	public void getGlobalService();
}
