package com.logger.client.main;

import java.util.concurrent.CountDownLatch;

import com.logger.client.core.MessageSendExecutor;
import com.netty.server.servicebean.Caculate;

/**
 * @author zhangxianbin
 */
public class CalcParallelRequestThread implements Runnable {

	private CountDownLatch signal;

	private CountDownLatch finish;

	private int taskNumber = 0;

	public CalcParallelRequestThread(CountDownLatch signal, CountDownLatch finish, int taskNumber) {
		this.signal = signal;
		this.finish = finish;
		this.taskNumber = taskNumber;
	}

	@Override
	public void run() {

		try {
			signal.await();
			Caculate calc = MessageSendExecutor.execute(Caculate.class);
			int add = calc.add(taskNumber, taskNumber);
			System.out.println("Calc add result:[" + add + "]");
			finish.countDown();
		} catch (Exception e) {
			System.out.println("空指针");
			e.printStackTrace();
		}

	}

}
