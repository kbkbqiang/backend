package com.zq.backend.common.utils.json;

import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @ClassName: JObject
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:35:23
 */
public class JObject implements JObjectInterface {

	private ObjectMapper mapper = null;
	private ObjectNode jnode = null;

	public JObject(ObjectMapper mapper, ObjectNode jnode) {
		assert jnode != null && mapper != null : "can NOT be empty";
		this.mapper = mapper;
		this.jnode = jnode;
	}

	public ObjectNode getJnode() {
		return jnode;
	}

	public JObjectInterface put(String key, String value) {
		jnode.put(key, value);
		return this;
	}

	public JObjectInterface put(String key, boolean value) {
		jnode.put(key, value);
		return this;
	}

	public JObjectInterface put(String key, int value) {
		jnode.put(key, value);
		return this;
	}

	public JObjectInterface put(String key, long value) {
		jnode.put(key, value);
		return this;
	}

	public JObjectInterface put(String key, float value) {
		jnode.put(key, value);
		return this;
	}

	public JObjectInterface put(String key, double value) {
		jnode.put(key, value);
		return this;
	}

	public JObjectInterface put(String key, JArrayInterface value) {
		if (value instanceof JArray)
			jnode.set(key, ((JArray) value).getJnode());
		return this;
	}

	public JObjectInterface put(String key, JObjectInterface value) {
		if (value instanceof JObject)
			jnode.set(key, ((JObject) value).getJnode());
		return this;
	}

	public JObjectInterface put(String key, ToJsonInterface obj, String user_arg) {
		JObjectInterface tmpobj = this.newObject();
		obj.toJson(tmpobj, user_arg);
		this.put(key, tmpobj);
		return this;
	}

	public JObjectInterface put(ToJsonInterface obj, String user_arg) {
		obj.toJson(this, user_arg);
		return this;
	}

	public JObjectInterface put(String key, Collection<? extends ToJsonInterface> objs, String user_arg) {
		ArrayNode tmpnode = mapper.createArrayNode();
		for (ToJsonInterface obj : objs) {
			JObject jobj = new JObject(mapper, mapper.createObjectNode());
			obj.toJson(jobj, user_arg);
			tmpnode.add(jobj.getJnode());
		}
		jnode.set(key, tmpnode);
		return this;
	}

	public JObjectInterface newObject() {
		return new JObject(mapper, mapper.createObjectNode());
	}

	public JArrayInterface newArray() {
		return new JArray(mapper, mapper.createArrayNode());
	}
}
