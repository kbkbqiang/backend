package com.zq.server.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dao.mapper.InvestigationUrgeInfoMapper;
import com.backend.dao.model.InvestigationUrgeInfo;
import com.backend.dao.separate.DataSource;
import com.zq.server.service.SpringContextUtil;
import com.zq.server.service.TestService;

/**
 * @ClassName: TestService
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月23日 下午2:08:58
 */
@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	private InvestigationUrgeInfoMapper investigationUrgeInfoMapper;

	@Override
	@DataSource("read")
	public void say() {
		InvestigationUrgeInfo urgeInfo = investigationUrgeInfoMapper.selectByPrimaryKey(46);
		System.out.println("=======read say========" + urgeInfo.getRemark());
	}

	@Override
	@DataSource("write")
	@Transactional(rollbackFor = Exception.class)
	public void testInsert() throws Exception {
		InvestigationUrgeInfo testInfo = new InvestigationUrgeInfo();
		testInfo.setInvestigationUserId("test");
		testInfo.setScore(BigDecimal.valueOf(2.22));
		testInfo.setRemark("test");
		testInfo.setCreateManager(11111111);
		testInfo.setCreateTime(new Date());
		investigationUrgeInfoMapper.insertSelective(testInfo);
		System.out.println("===================");
		throw new Exception();

	}

	@Override
	@DataSource("write")
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void testUpdate() throws Exception {
		InvestigationUrgeInfo urgeInfo = investigationUrgeInfoMapper.selectByPrimaryKey(46);
		urgeInfo.setRemark("write1111");
		urgeInfo.setCreateTime(new Date());
		investigationUrgeInfoMapper.updateByPrimaryKeySelective(urgeInfo);
		// throw new Exception();
	}

	@Override
	@DataSource("write")
	@Transactional(rollbackFor = Exception.class)
	public void testTransaction() throws Exception {
		// TestService service = (TestService)
		// SpringContextUtil.getBean("testService");
		// service.testUpdate();
		testUpdate();
		testInsert();
		say();
	}

}
