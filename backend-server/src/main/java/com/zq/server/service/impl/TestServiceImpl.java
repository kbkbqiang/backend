package com.zq.server.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dao.mapper.InvestigationUrgeInfoMapper;
import com.backend.dao.model.InvestigationUrgeInfo;
import com.backend.dao.separate.DataSource;
import com.github.pagehelper.PageHelper;
import com.zq.server.service.TestService;

/** 
 * @ClassName: TestService 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月23日 下午2:08:58 
 */
@Service
@Transactional(readOnly = true)
public class TestServiceImpl implements TestService {
	
	@Autowired
	private InvestigationUrgeInfoMapper investigationUrgeInfoMapper;
	
	@Override
	@DataSource("read")
	public void say(){
		try{
			InvestigationUrgeInfo urgeInfo = investigationUrgeInfoMapper.selectByPrimaryKey(46);
			System.out.println("===============" + urgeInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	@DataSource("write")
	@Transactional(rollbackFor = Exception.class)
	public void testInsert(){
		// 暂时未解决事务问题
		InvestigationUrgeInfo testInfo = new InvestigationUrgeInfo();
		testInfo.setInvestigationUserId("test");
		testInfo.setScore(BigDecimal.valueOf(2.22));
		testInfo.setRemark("test");
		investigationUrgeInfoMapper.insertSelective(testInfo);
		System.out.println("===================");
	}

}
