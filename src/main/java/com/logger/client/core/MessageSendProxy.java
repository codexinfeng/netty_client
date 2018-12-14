package com.logger.client.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.netty.server.model.MessageRequest;

public class MessageSendProxy<T> implements InvocationHandler {

	private Class<T> cls;

	public MessageSendProxy(Class<T> cls) {
		this.cls = cls;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MessageRequest request = new MessageRequest();
		request.setMessageId(UUID.randomUUID().toString());
		request.setClassName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setTypeParameter(method.getParameterTypes());
		request.setParameterVal(args);

		MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
		MessageCallBack callBack = handler.sendRequest(request);
		return callBack.start();
		
		
		
		
		
		
		
		
		

	}

}
