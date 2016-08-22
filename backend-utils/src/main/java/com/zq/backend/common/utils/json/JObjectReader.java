package com.zq.backend.common.utils.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IteratorUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @ClassName: JObjectReader
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:35:31
 */
public class JObjectReader {
	private static String jsonErrorMessage = "json error";

	public static JObjectReader createJObjectReader(final String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(json);
			if (root == null || !root.isObject())
				throw new Exception("json NOT object");
			return new JObjectReader(mapper, root);
		} catch (IOException e) {
			throw new Exception(jsonErrorMessage);
		}
	}

	private ObjectMapper mapper = null;
	private JsonNode jroot = null;

	private JObjectReader(ObjectMapper mapper, JsonNode root) {
		assert jroot == null && this.mapper == null;
		this.mapper = mapper;
		this.jroot = root;
	}

	public JsonNode readJNode(String path) throws Exception {
		assert jroot != null;
		String[] keys = path.split("\\.");
		if (keys.length == 0)
			throw new Exception(jsonErrorMessage);
		JsonNode curnode = this.jroot;
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(key).append("/");
			curnode = curnode.path(key);
			if (curnode == null || curnode.isMissingNode())
				throw new Exception("加载json'" + sb.toString() + "'失败");
		}
		return curnode;
	}

	public boolean pathExist(String path) throws Exception {
		assert jroot != null;
		String[] keys = path.split("\\.");
		if (keys.length == 0)
			throw new Exception(jsonErrorMessage);
		JsonNode curnode = this.jroot;
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(key).append("/");
			curnode = curnode.path(key);
			if (curnode == null || curnode.isMissingNode())
				return false;
		}
		return !curnode.isMissingNode();
	}

	public <T> T readObject(String path, Class<T> cls) throws Exception {
		JsonNode node = this.readJNode(path);
		if (!node.isObject())
			throw new Exception("json 字段'" + path + "'必须为对象类型");
		ObjectNode realnode = (ObjectNode) node;
		return mapper.convertValue(realnode, cls);
	}

	public <T> Collection<T> readObjectArray(String path, Class<T> cls) throws Exception {
		JsonNode node = this.readJNode(path);
		if (!node.isArray())
			throw new Exception("json 字段'" + path + "'必须为数组类型");

		Collection<T> results = new ArrayList<>();
		ArrayNode realnode = (ArrayNode) node;
		for (JsonNode item : realnode) {
			if (!item.isObject())
				throw new Exception("json 数组元素'" + path + "'必须为对象类型");
			T obj = mapper.convertValue(item, cls);
			results.add(obj);
		}
		return results;
	}

	public String readNoneEmptyString(String path) throws Exception {
		String result = this.readString(path);
		if (result.isEmpty())
			throw new Exception("json 字段'" + path + "'不能为空");
		return result;
	}

	public String readString(String path) throws Exception {

		JsonNode curnode = this.readJNode(path);
		if (curnode.isObject() || curnode.isArray())
			throw new Exception("json 字段'" + path + "'必须为基本类型");
		return curnode.asText().trim();
	}

	public boolean readBoolean(String path) throws Exception {

		JsonNode curnode = this.readJNode(path);
		if (!curnode.isBoolean())
			throw new Exception("json 字段'" + path + "'必须为基本类型");
		return curnode.asBoolean();
	}

	public Collection<String> readFieldNames() {
		return IteratorUtils.toList(jroot.fieldNames());
	}

	/*
	 * public int readInt(String path) throws Exception { JsonNode curnode =
	 * this.readJNode(path); if(curnode.isInt()) return curnode.getIntValue();
	 * throw new Exception(jsonErrorMessage); }
	 * 
	 * public double readDouble(String path) throws Exception { JsonNode curnode
	 * = this.readJNode(path); if(curnode.isDouble()) return
	 * curnode.getDoubleValue(); throw new Exception(jsonErrorMessage); }
	 * 
	 * public long readLong(String path) throws Exception { JsonNode curnode =
	 * this.readJNode(path); if(curnode.isLong()) return curnode.getLongValue();
	 * throw new Exception(jsonErrorMessage); }
	 */

	private Collection<JsonNode> readJNodeList(String path, boolean can_empty) throws Exception {
		assert jroot != null;
		String[] keys = path.split("\\.");
		if (keys.length == 0)
			throw new Exception(jsonErrorMessage);
		JsonNode curnode = this.jroot;
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(key).append("/");
			curnode = curnode.path(key);
			if (curnode == null || curnode.isMissingNode())
				throw new Exception("加载json'" + sb.toString() + "'失败");
		}
		if (!curnode.isArray())
			throw new Exception("json 字段'" + path + "'必须为数组类型");

		List<JsonNode> nodes = new ArrayList<JsonNode>();
		for (final JsonNode item : curnode) {
			if (item == null || item.isMissingNode())
				throw new Exception("加载json'" + sb.toString() + "'失败");
			nodes.add(item);
		}
		if (!can_empty && nodes.isEmpty())
			throw new Exception("json 数组'" + path + "'不能为空");
		return nodes;
	}

	public Collection<String> readStringList(String path, boolean can_empty, boolean can_empty_str) throws Exception {

		Collection<JsonNode> curnodes = this.readJNodeList(path, can_empty);
		List<String> strs = new ArrayList<String>();
		for (JsonNode node : curnodes) {
			if (node.isArray() || node.isObject())
				throw new Exception("json 字段'" + path + "'必须为基本类型");
			String txt = node.asText().trim();
			// 字符串不能为空
			if (!can_empty_str && txt.isEmpty())
				throw new Exception("json 字段'" + path + "'不能为空");
			strs.add(txt);
		}
		return strs;
	}

	public Collection<String> readStringListUniq(String path, boolean can_empty, boolean can_empty_str) throws Exception {

		Collection<JsonNode> curnodes = this.readJNodeList(path, can_empty);
		Set<String> strs = new HashSet<String>();
		for (JsonNode node : curnodes) {
			if (node.isArray() || node.isObject())
				throw new Exception("json 字段'" + path + "'必须为基本类型");
			String txt = node.asText().trim();
			// 字符串不能为空
			if (!can_empty_str && txt.isEmpty())
				throw new Exception("json 字段'" + path + "'不能为空");
			strs.add(txt);
		}
		// 是否有重复
		if (curnodes.size() != strs.size())
			throw new Exception("json 字段'" + path + "'不能重复");
		return strs;
	}
}
