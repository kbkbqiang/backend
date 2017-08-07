package com.backend.dao.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.backend.dao.separate.DynamicDataSource;

public class BackendDataSourceTransactionManager extends org.springframework.jdbc.datasource.DataSourceTransactionManager {

	private static Logger logger = LoggerFactory.getLogger(BackendDataSourceTransactionManager.class);

	private static final long serialVersionUID = 775864650703500204L;

	public BackendDataSourceTransactionManager(DynamicDataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		try {
			HandleObjs.startTransaction();
		} catch (Exception e) {
			logger.error("", e);
		}
		super.doBegin(transaction, definition);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		try {
			super.doCommit(status);
		} finally {
			HandleObjs.endTransaction();
		}
		try {
			HandleObjs.processTask();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		try {
			super.doRollback(status);
		} finally {
			HandleObjs.endTransaction();
		}
		try {
			HandleObjs.removeAll();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
