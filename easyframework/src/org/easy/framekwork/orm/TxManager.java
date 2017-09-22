package org.easy.framekwork.orm;

import java.lang.reflect.Method;

import org.easy.framekwork.log.Logger;
import org.easy.framekwork.orm.annotation.Transaction;

import net.sf.cglib.proxy.MethodProxy;

public class TxManager {

	private static final Logger logger = Logger.getLogger(TxManager.class);

	private static final ThreadLocal<Boolean> flagContainer = new ThreadLocal<Boolean>() {

		@Override
		protected Boolean initialValue() {
			// TODO Auto-generated method stub
			return false;
		}

	};

	public Object doTransaction(Method method, MethodProxy methodProxy, Object targetObject, Object[] methodParams) throws Throwable {
		// TODO Auto-generated method stub
		Object result = null;
		boolean flag = flagContainer.get();
		if (method.isAnnotationPresent(Transaction.class) && !flag) {
			flagContainer.set(true);
			try {
				logger.info("begin transaction");
				DataBaseManager.beginTransaction();
				result = methodProxy.invokeSuper(targetObject, methodParams);
				logger.info("commit transaction");
				DataBaseManager.commitTransaction();
			} catch (Exception e) {
				logger.info("rollback transaction");
				DataBaseManager.rollbackTransaction();
				throw e;
			} finally {
				flagContainer.remove();
			}
		}
		return result;
	}

}
