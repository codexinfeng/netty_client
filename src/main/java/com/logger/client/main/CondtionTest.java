package com.logger.client.main;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangxianbin
 */
public class CondtionTest {

	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		Condition con = lock.newCondition();
		long start = System.currentTimeMillis();
		System.out.println(start);
		try {
			lock.lock();
			con.await(10 * 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		int seconds = (int) ((System.currentTimeMillis() - start) / 1000);
		System.out.println(seconds);
	}
}
