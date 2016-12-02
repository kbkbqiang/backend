package com.backend.dao.mapper;

import java.util.List;

import com.backend.dao.model.Order;

public interface OrderMapper {

	void insert(Order model);

	int update(List<Integer> userIds);

	int deleteAll();

	List<Order> selectAll();

}
