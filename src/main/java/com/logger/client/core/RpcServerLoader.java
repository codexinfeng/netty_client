package com.logger.client.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RpcServerLoader {

	private volatile static RpcServerLoader rpcServerLoader;

	private final static String DELIMITER = ":";

	@SuppressWarnings("unused")
	private RpcSerializerProtocol serializeProtocol = RpcSerializerProtocol.JDK_SERIALLZE;

	private final static int parallel = Runtime.getRuntime().availableProcessors();
	private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);
	private static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
	private MessageSendHandler messageSendHandler = null;
	private Lock lock = new ReentrantLock();
	private Condition signal = lock.newCondition();

	private RpcServerLoader() {
	}

	public static RpcServerLoader getInstance() {
		if (rpcServerLoader == null) {
			synchronized (RpcServerLoader.class) {
				if (rpcServerLoader == null) {
					rpcServerLoader = new RpcServerLoader();
				}
			}
		}
		return rpcServerLoader;
	}

	public void load(String serverAddress, RpcSerializerProtocol serializeProtocol) {
		String[] ipAddr = serverAddress.split(RpcServerLoader.DELIMITER);
		if (ipAddr.length == 2) {
			String host = ipAddr[0];
			int port = Integer.valueOf(ipAddr[1]);
			final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);
			threadPoolExecutor.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddr, serializeProtocol));
		}
	}

	public void setMessageSendHandler(MessageSendHandler messageSendHandler) {
		try {
			lock.lock();
			this.messageSendHandler = messageSendHandler;
			signal.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public MessageSendHandler getMessageSendHandler() throws InterruptedException {
		try {
			lock.lock();
			if (messageSendHandler == null) {
				signal.await();
			}
			return messageSendHandler;
		} finally {
			lock.unlock();
		}
	}

	public void unLoad() {
		messageSendHandler.close();
		threadPoolExecutor.shutdown();
		eventLoopGroup.shutdownGracefully();
	}

	public void setSerializeProtocol(RpcSerializerProtocol serializeProtocol) {
		this.serializeProtocol = serializeProtocol;
	}
}
