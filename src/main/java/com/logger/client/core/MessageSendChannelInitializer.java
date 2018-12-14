package com.logger.client.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel> {

	public final static int MESSAGE_LENGTH = 4;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
				MessageSendChannelInitializer.MESSAGE_LENGTH, 0, MessageSendChannelInitializer.MESSAGE_LENGTH));
		pipeline.addLast(new LengthFieldPrepender(MessageSendChannelInitializer.MESSAGE_LENGTH));
		pipeline.addLast(new ObjectEncoder());
		pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this
				.getClass().getClassLoader())));
		pipeline.addLast(new MessageSendHandler());

	}
}
