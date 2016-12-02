/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.backend.dao.algorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.NoneTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

public final class SingleKeyModuloTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {
    
	/** 
	*  =
    *  select * from t_order from t_order where order_id = 11  
    *          └── SELECT *  FROM t_order_1 WHERE order_id = 11 
    *  select * from t_order from t_order where order_id = 44 
    *          └── SELECT *  FROM t_order_0 WHERE order_id = 44 
    */ 
    @Override
    public String doEqualSharding(final Collection<String> availableTargetNames, final ShardingValue<Integer> shardingValue) {
    	int singleTableTotalSize = 10000000;
    	/**
    	 * 按表数据量取模
    	 */
    	for (String each : availableTargetNames) {
            if (each.endsWith(shardingValue.getValue() / singleTableTotalSize + "")) {
                return each;
            }
        }
    	
    	// 按分表取模
        /*for (String each : availableTargetNames) {
            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
                return each;
            }
        }*/
        throw new UnsupportedOperationException();
    }
    
    /** 
     * in 
     *  select * from t_order from t_order where order_id in (11,44)   
     *          ├── SELECT *  FROM t_order_0 WHERE order_id IN (11,44)  
     *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,44)  
     *  select * from t_order from t_order where order_id in (11,13,15)   
     *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,13,15)   
     *  select * from t_order from t_order where order_id in (22,24,26)   
     *          └──SELECT *  FROM t_order_0 WHERE order_id IN (22,24,26)  
     */
    @Override
    public Collection<String> doInSharding(final Collection<String> availableTargetNames, final ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Collection<Integer> values = shardingValue.getValues();
        for (Integer value : values) {
            for (String tableNames : availableTargetNames) {
                if (tableNames.endsWith(value % 2 + "")) {
                    result.add(tableNames);
                }
            }
        }
        return result;
    }
    
    /** 
     * BETWEEN
     *  select * from t_order from t_order where order_id between 10 and 20  
     *          ├── SELECT *  FROM t_order_0 WHERE order_id BETWEEN 10 AND 20  
     *          └── SELECT *  FROM t_order_1 WHERE order_id BETWEEN 10 AND 20  
     */
    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availableTargetNames, final ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Integer> range = shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : availableTargetNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
