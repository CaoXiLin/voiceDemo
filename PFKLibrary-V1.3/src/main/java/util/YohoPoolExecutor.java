/*
 * Created by fanchao
 * 
 * Date:2014年9月22日下午4:57:15 
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */
package util;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Function: 管理应用的线程池
 * 
 * <p>
 * 使用原则 
 * 1、如果单纯的开启线程，没有跟UI线程交互，直接用线程开启 
 * 2、如果用asynctask,则需要使用如下的方法，这样才不会被阻塞 
 * <pre>
 * new AsyncTask().executeOnExecutor(new ThreadPool(), Params... params)
 * </pre>
 * </p>
 * 
 * <pre>
 * 1:
 * YohoPoolExecutor.getInstance().getExecutor().execute(new Runnable() { 
 *			@Override 
 *			public void run() {
 *				// TODO
 *			}
 *		});
 * 2:
 * AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
 *			@Override
 *			protected Void doInBackground(Void... params) {
 *				return null;
 *			}};
 *		task.executeOnExecutor(YohoPoolExecutor.getInstance().getExecutor());
 * </pre>
 * 
 * Date: 2014年9月22日 下午4:57:15
 * 
 * @author fanchao
 */
public final class YohoPoolExecutor {

	/**
	 * 实例对象
	 */
	private static YohoPoolExecutor instance;
	/**
	 * 线程池执行类
	 */
	private ThreadPoolExecutor executor;

	/**
	 * 线程工厂
	 */
	private static final ThreadFactory threadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "ZHJ----thread #" + mCount.getAndIncrement());
		}
	};

	public static YohoPoolExecutor getInstance() {
		if (instance == null) {
			instance = new YohoPoolExecutor();
		}
		return instance;
	}

	/**
	 * 线程池构造方法
	 * <p>
	 * 默认同时最小同时的线程池为3个 使用直接提交策略
	 * </p>
	 * 
	 */
	public YohoPoolExecutor() {
		executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
	}

	/**
	 * 判断线程池是否为空
	 * 
	 * @return true empty
	 */
	public boolean isQueueEmpty() {
		return executor.getQueue().isEmpty();
	}

	/**
	 * 获取到线程池的大小
	 * 
	 * @return int 线程池大小
	 */
	public int getQueueSize() {
		return executor.getQueue().size();
	}

	/**
	 * 获取executor
	 * 
	 * @return
	 */
	public Executor getExecutor() {
		return executor;
	}
}
