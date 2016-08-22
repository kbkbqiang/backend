package com.zq.backend.common.utils;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @ClassName: QueueStorage
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:47:23
 */
public class QueueStorage {
	// 仓库最大存储量
	private final int MAX_SIZE = 1024;
	// 仓库存储的载体
	private LinkedBlockingQueue<Object> list = new LinkedBlockingQueue<Object>(MAX_SIZE);

	// 生产num个产品
	public void produce(Object var) throws InterruptedException {
		try {
			// 放入产品，自动阻塞
			list.put(var);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 消费num个产品
	public Object consume() throws InterruptedException {
		try {
			// 消费产品，自动阻塞
			return list.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
