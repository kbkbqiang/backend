package com.zq.backend.common.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


/**
 * 
 * @ClassName: JArray 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:36:14
 */
public class JArray implements JArrayInterface {
	private ObjectMapper mapper = null;
	private ArrayNode jnode = null;

	public JArray(ObjectMapper mapper, ArrayNode jnode) {
		assert jnode != null && mapper != null : "can NOT be empty";
		this.mapper = mapper;
		this.jnode = jnode;
	}

	public ArrayNode getJnode() {
		return jnode;
	}

	public JArrayInterface add(String value) {
		jnode.add(value);
		return this;
	}

	public JArrayInterface add(boolean value) {
		jnode.add(value);
		return this;
	}

	public JArrayInterface add(int value) {
		jnode.add(value);
		return this;
	}

	public JArrayInterface add(long value) {
		jnode.add(value);
		return this;
	}

	public JArrayInterface add(float value) {
		jnode.add(value);
		return this;
	}

	public JArrayInterface add(double value) {
		jnode.add(value);
		return this;
	}

	public JArrayInterface add(JObjectInterface value) {
		if (value instanceof JObject)
			jnode.add(((JObject) value).getJnode());
		return this;
	}

	public JObjectInterface newObject() {
		return new JObject(mapper, mapper.createObjectNode());
	}

	public JArrayInterface newArray() {
		return new JArray(mapper, mapper.createArrayNode());
	}
}
