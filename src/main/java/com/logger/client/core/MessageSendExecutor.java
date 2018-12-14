package com.logger.client.core;

import java.lang.reflect.Proxy;

/**
 * 
 * @author JZG
 *
 */
public class MessageSendExecutor {

	private RpcServerLoader loader = RpcServerLoader.getInstance();

	public MessageSendExecutor(String serverAddress) {
		loader.load(serverAddress, RpcSerializerProtocol.JDK_SERIALLZE);
	}

	public void stop() {
		loader.unLoad();
	}

	@SuppressWarnings("unchecked")
	public static <T> T execute(Class<T> rpcInterface) {
		return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface },
				new MessageSendProxy(rpcInterface));
	}
}
