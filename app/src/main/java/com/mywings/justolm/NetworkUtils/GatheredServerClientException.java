package com.mywings.justolm.NetworkUtils;

/**
 * This exception gathers all exceptions regarding server-client communication
 * This will keep the amount of different exceptions to a minimum in the
 * toplayers
 * 
 * @author A500381
 * 
 */
public class GatheredServerClientException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9077027195335549364L;

	public GatheredServerClientException(String msg) {
		super(msg);
	}
}
