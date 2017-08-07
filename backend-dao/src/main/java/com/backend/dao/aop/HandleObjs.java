package com.backend.dao.aop;

import java.util.HashSet;
import java.util.Set;

public class HandleObjs {
	// 线程池

	public static final ThreadLocal<Set<IProcesser>> keys = new ThreadLocal<Set<IProcesser>>();

	public static final ThreadLocal<Boolean> isStartTransaction = new ThreadLocal<Boolean>();

	public static boolean putKey(IProcesser key) {
		if (key == null) {
			return false;
		}
		if (keys.get() == null) {
			initKeys();
		}
		return keys.get().add(key);
	}

	private static synchronized void initKeys() {
		if (keys.get() == null) {
			keys.set(new HashSet<IProcesser>());
		}
	}

	public static Set<IProcesser> getAll() {
		return keys.get();
	}

	public static void removeAll() {
		keys.remove();
	}

	public static void startTransaction() {
		isStartTransaction.set(true);
		HandleObjs.removeAll();
	}

	public static void endTransaction() {
		isStartTransaction.set(false);
	}

	public static boolean getIsStartTransaction() {
		if (isStartTransaction == null || isStartTransaction.get() == null) {
			return false;
		}
		return isStartTransaction.get();
	}

	public static void processTask() {
		Set<IProcesser> doingSet = HandleObjs.getAll();
		if (doingSet != null && doingSet.size() > 0) {
			Set<IProcesser> mySet = new HashSet<IProcesser>(doingSet);
			// 清空任务
			HandleObjs.removeAll();
			for (IProcesser key : mySet) {
				if (key != null) {
					key.process(); // 这里可以考虑放到线程池调用
				}
			}
		}

		HandleObjs.endTransaction();
	}
}
