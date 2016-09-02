package com.backend.dao.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;


public class BackendDataSourceTransactionManager extends org.springframework.jdbc.datasource.DataSourceTransactionManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(BackendDataSourceTransactionManager.class);
	
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		try {
			HandleObjs.startTransaction();
		} catch (Exception e) {
			logger.error("",e);
		}
		super.doBegin(transaction, definition);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) { 
		super.doCommit(status);
		try {
			HandleObjs.processTask();
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	@Override
	protected void doRollback(DefaultTransactionStatus status){
		super.doRollback(status);
		try {
			HandleObjs.removeAll();
		} catch (Exception e) {
			logger.error("",e);
		}
	}
}
