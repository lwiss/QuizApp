package epfl.sweng.patterns;

import epfl.sweng.servercomm.communication.ServerCommunication;
import epfl.sweng.servercomm.communication.ServerCommunicationProxy;

/**
 * 
 * @author MohamedBenArbia
 * 
 */

public class CheckProxyHelper implements ICheckProxyHelper {

	public Class<?> getServerCommunicationClass() {
		// TODO Auto-generated method stub

		return ServerCommunication.class;
	}

	public Class<?> getProxyClass() {
		return ServerCommunicationProxy.class;
	}

}
