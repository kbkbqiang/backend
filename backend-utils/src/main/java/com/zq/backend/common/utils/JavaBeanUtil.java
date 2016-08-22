package com.zq.backend.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zq.backend.common.utils.json.JObjectInterface;

/**
 * 
 * @ClassName: JavaBeanUtil 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:46:28
 */
public class JavaBeanUtil {
	public static Map<String, Object> introspect(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
		BeanInfo info = null;
		try {
			info = Introspector.getBeanInfo(obj.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();
				if (reader != null &&
						!pd.getName().equals("class") &&
						!reader.isAnnotationPresent(JsonIgnore.class)) {
					result.put(pd.getName(), reader.invoke(obj));
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
			result.clear();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			result.clear();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			result.clear();
		}
		return result;
	}

	static public void setMembers2Default(Object obj) {
		BeanInfo info = null;
		try {
			info = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] e = info.getPropertyDescriptors();
			int var4 = e.length;
			for (int var5 = 0; var5 < var4; ++var5) {
				PropertyDescriptor pd = e[var5];
				Method writer = pd.getWriteMethod();
				if (writer != null && !pd.getName().equals("class")) {
					if (pd.getPropertyType() == String.class)
						writer.invoke(obj, new Object[] { String.class.getName() });
					if (pd.getPropertyType() == Integer.class || pd.getPropertyType() == int.class)
						writer.invoke(obj, new Object[] { 0 });
					if (pd.getPropertyType() == Double.class || pd.getPropertyType() == double.class)
						writer.invoke(obj, new Object[] { 0.0 });
					if (pd.getPropertyType() == Long.class || pd.getPropertyType() == long.class)
						writer.invoke(obj, new Object[] { 0 });
					if (pd.getPropertyType() == Boolean.class || pd.getPropertyType() == boolean.class)
						writer.invoke(obj, new Object[] { false });
				}
			}
		} catch (IntrospectionException var8) {
			var8.printStackTrace();
		} catch (IllegalAccessException var9) {
			var9.printStackTrace();
		} catch (InvocationTargetException var10) {
			var10.printStackTrace();
		}
	}

	static public void object2Json(Object obj, JObjectInterface jObjectInterface) {
		Map<String, Object> map = JavaBeanUtil.introspect(obj);
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry) it.next();
			jObjectInterface.put(pair.getKey(), pair.getValue().toString());
		}
	}

	public static void object2Json(Object obj, JObjectInterface jObjectInterface, List<String> filterFields) {
		try {
			if (filterFields == null) {
				filterFields = new ArrayList<String>();
			}
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (filterFields.contains(field.getName())) {
					continue;
				}
				String methodStr = "get" + getMethodName(field.getName());
				Method method = obj.getClass().getMethod(methodStr);
				if (field.getGenericType().toString().equals("class java.lang.String")) {
					String val = (String) method.invoke(obj);
					if (StringUtils.isNoneBlank(val)) {
						jObjectInterface.put(field.getName(), val);
					}
				}
				if (field.getGenericType().toString().equals("class java.lang.Integer")) {
					Integer val = (Integer) method.invoke(obj);
					if (val != null) {
						jObjectInterface.put(field.getName(), val);
					}
				}
				if (field.getGenericType().toString().equals("class java.lang.Long")) {
					Long val = (Long) method.invoke(obj);
					if (val != null) {
						jObjectInterface.put(field.getName(), val);
					}
				}
				if (field.getGenericType().toString().equals("class java.lang.Float")) {
					Float val = (Float) method.invoke(obj);
					if (val != null) {
						jObjectInterface.put(field.getName(), val);
					}
				}
				if (field.getGenericType().toString().equals("class java.lang.Double")) {
					Double val = (Double) method.invoke(obj);
					if (val != null) {
						jObjectInterface.put(field.getName(), val);
					}
				}
				// 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
				// 反射找不到getter的具体名
				if (field.getGenericType().toString().equals("class java.lang.Boolean")
						|| field.getGenericType().toString().equals("boolean")) {
					Method m = obj.getClass().getMethod(field.getName());
					Boolean val = (Boolean) m.invoke(obj);
					if (val != null) {
						jObjectInterface.put(field.getName(), val);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将json字符串解析成key-value格式 - 不支持多级数组
	 * 
	 * @param jObjectInterface
	 * @param jsonParams
	 */
	public static void json2Map(JObjectInterface jObjectInterface, String jsonParams) throws Exception {
		if (StringUtils.isNotBlank(jsonParams)) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(jsonParams);
			
			if (node != null && node.size() > 0) {
				toMap(jObjectInterface, node);
			}
		}
	}

	private static void toMap(JObjectInterface jObjectInterface, JsonNode node) {
		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()){
			Entry<String, JsonNode> entry = fields.next();
			String key = entry.getKey();
			JsonNode value = entry.getValue();
			if (value.isTextual()) {
				jObjectInterface.put(key, value.asText());
			} else if (value.isBoolean()) {
				jObjectInterface.put(key, value.asBoolean());
			} else if (value.isInt()) {
				jObjectInterface.put(key, value.asInt());
			} else if (value.isFloat()) {
				jObjectInterface.put(key, value.floatValue());
			} else if (value.isDouble()) {
				jObjectInterface.put(key, value.asDouble());
			}
		}
	}

	/**
	 * 把一个字符串的第一个字母大写
	 * 
	 * @param fildeName
	 * @return
	 * @throws Exception
	 */
	private static String getMethodName(String fildeName) throws Exception {
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}
}
