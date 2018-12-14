package com.logger.client.main;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.time.StopWatch;

import com.logger.client.core.MessageSendExecutor;

/**
 * @author zhangxianbin
 */
public class RpcParalleTest {

	public static void main(String[] args) throws InterruptedException {
		final MessageSendExecutor executor = new MessageSendExecutor("127.0.0.1:18888");
		StopWatch sw = new StopWatch();
		sw.start();
		int parallel = 1000;
		CountDownLatch signal = new CountDownLatch(1);
		CountDownLatch finish = new CountDownLatch(parallel);

		for (int index = 0; index < parallel; index++) {
			CalcParallelRequestThread thread = new CalcParallelRequestThread(signal, finish, index);
			new Thread(thread).start();
		}
		signal.countDown();
		finish.await();
		sw.stop();
		String message = String.format("[一共耗时:%d]", sw.getTime());
		System.out.println(message);
		executor.stop();

	}
}
