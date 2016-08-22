package com.zq.backend.common.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zq.backend.common.utils.json.JArray;
import com.zq.backend.common.utils.json.JArrayInterface;
import com.zq.backend.common.utils.json.JObject;
import com.zq.backend.common.utils.json.JObjectInterface;
import com.zq.backend.common.utils.json.ToJsonInterface;

/**
 * 
 * @ClassName: ToJsonUtil 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:52:21
 */
public class ToJsonUtil {
    private static String ResultSuccuss = "succuss";
    private static String ResultFailed = "failed";

    public static String exceptionToResult(Exception e)
    {
        return exceptionToResult(e.getMessage());
    }

    public static String exceptionToResult(String message)
    {
        return new ToJsonUtil().setError(message).toString();
    }

    public static String succussResult()
    {
        return new ToJsonUtil().toString();
    }

    public static ToJsonUtil rawJson()
    {
        return new ToJsonUtil(true,null);
    }
    
    public static ToJsonUtil rawJson(String arg){
        return new ToJsonUtil(true,arg);
    }
    
    public static String httpResponseToResult(String jsonParams){
    	if(StringUtils.isNotBlank(jsonParams) && !"null".equals(jsonParams)){
//    		JSONObject respJson = new JSONObject(jsonParams);
//    		boolean success = respJson.getBoolean("success");
//    		if(success){
//    			return succussResult();
//    		}else{
//    			return exceptionToResult(respJson.getString("error"));
//    		}
    	}
    	return succussResult();
    }

    private ObjectMapper jmapper = null;
    private ObjectNode jroot = null;
    private ObjectNode realjroot = null;
    private String user_arg = null;
    public ToJsonUtil(String user_arg)
    {
        this.objInit(user_arg);
    }

    public ToJsonUtil(){this.objInit(null);}

    private ToJsonUtil(boolean raw,String arg)
    {
        if(raw){
            this.user_arg = arg;
            jmapper = new ObjectMapper();
            jroot = jmapper.createObjectNode();
            realjroot = jroot;
        }else{
            this.objInit(null);
        }
    }

    private void objInit(String arg)
    {
        this.user_arg = arg;
        jmapper = new ObjectMapper();
        jroot = jmapper.createObjectNode();
        jroot.put("result", ResultSuccuss);
        realjroot = jmapper.createObjectNode();
        jroot.set("body", realjroot);
    }

    public ToJsonUtil setError(String message)
    {
        jroot.put("result", ResultFailed);
        if(!StringUtil.isEmpty(message))
            jroot.put("message",message);
        return this;
    }

    public ToJsonUtil put(String key, String value)
    {
        realjroot.put(key, value);
        return this;
    }
    public ToJsonUtil put(String key, boolean value)
    {
        realjroot.put(key, value);
        return this;
    }
    public ToJsonUtil put(String key, int value)
    {
        realjroot.put(key, value);
        return this;
    }
    public ToJsonUtil put(String key, long value)
    {
        realjroot.put(key, value);
        return this;
    }
    public ToJsonUtil put(String key, float value)
    {
        realjroot.put(key, value);
        return this;
    }
    public ToJsonUtil put(String key, double value)
    {
        realjroot.put(key, value);
        return this;
    }

    public ToJsonUtil put(String key, ToJsonInterface obj)
    {
        JObjectInterface jobj = this.newJObject();
        obj.toJson(jobj,this.user_arg);
        this.put(key,jobj);
        return this;
    }

    public ToJsonUtil put(ToJsonInterface obj)
    {
        JObjectInterface jobj = this.newJObject();
        obj.toJson(jobj,this.user_arg);
        this.put(jobj);
        return this;
    }

    public ToJsonUtil putStrings(String key, Collection<String> objs)
    {
        JArrayInterface tmpnode = this.newJArray();
        for(String obj : objs) tmpnode.add(obj);
        this.put(key,tmpnode);
        return this;
    }
    public ToJsonUtil putInts(String key, Collection<Integer> objs)
    {
        JArrayInterface tmpnode = this.newJArray();
        for(Integer obj : objs) tmpnode.add(obj);
        this.put(key,tmpnode);
        return this;
    }

    public ToJsonUtil put(String key, Collection<? extends ToJsonInterface> objs)
    {
        JArrayInterface tmpnode = this.newJArray();
        for(ToJsonInterface obj : objs)
        {
            JObjectInterface jobj = this.newJObject();
            obj.toJson(jobj,this.user_arg);
            tmpnode.add(jobj);
        }
        this.put(key,tmpnode);
        return this;
    }

    public ToJsonUtil put(String key, JArrayInterface arry)
    {
        if(arry instanceof JArray)
            realjroot.set(key, ((JArray)arry).getJnode());
        return this;
    }

    public ToJsonUtil put(String key, JObjectInterface obj)
    {
        if(obj instanceof JObject)
            realjroot.set(key, ((JObject)obj).getJnode());
        return this;
    }

    public ToJsonUtil put(JObjectInterface obj)
    {
        if(obj instanceof JObject)
        {
            ObjectNode realobj = ((JObject)obj).getJnode();
            Iterator<Map.Entry<String, JsonNode>> nodeIterator = realobj.fields();
            while (nodeIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodeIterator.next();
                this.realjroot.set(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public JArrayInterface newJArray()
    {
        return new JArray(jmapper,jmapper.createArrayNode());
    }

    public JObjectInterface newJObject()
    {
        return new JObject(jmapper,jmapper.createObjectNode());
    }

    public String getUser_arg() {
        return user_arg;
    }

    @Override
    public String toString() {
        return jroot.toString();
    }

}
