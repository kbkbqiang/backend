package com.zq.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zq.backend.common.utils.ResultResp;
import com.zq.backend.common.utils.ToJsonUtil;
import com.zq.server.entity.params.UserPasswordParameter;

/** 
 * @ClassName: SelfController 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午2:56:28 
 */
@RestController
@RequestMapping(value="/account/self/", produces = "text/html;charset=UTF-8")
@Api(value = "用户个人操作",description = "(SelfController)")
public class SelfController {

	@RequiresGuest
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ApiOperation(value = "账户登录",httpMethod = "POST",response = ResultResp.class)
    public String login(@Valid @RequestBody UserPasswordParameter param) {
        try {
            String content = ToJsonUtil.rawJson()
                    .put("email",param.getUsername())
                    .put("password",param.getPassword())
                    .toString();
//            AccountPostResult<AccountLoginResult> result = AccountHttpSender.Instance().ObjectPost(
//                    "/account/login_email_password",
//                    content,
//                    AccountLoginResult.class);
//            if(!result.getResult() || result.getBody() == null) throw new Exception(result.getMessage());
//
//            // 验证账号密码
//            User userobj = service.searchUserByEmail(param.getUsername());
//            if(userobj == null)
//                throw new Exception("账号密码错误");
//
//            if(userobj.getStatus().equals(User.Status.disabled.toString()))
//                throw new Exception("账户已被禁用");
//            else if(!userobj.getStatus().equals(User.Status.normal.toString()))
//                throw new Exception("账户异常，请联系客服");
//
//            return new ToJsonUtil()
//                    .put("user_id",userobj.getId())
//                    .put("token",result.getBody().getToken())
//                    .toString();
        	return null;
        } catch (Exception e) {
            return ToJsonUtil.exceptionToResult(e);
        }
    }

    @RequiresAuthentication
    @RequestMapping(value="/logout", method = RequestMethod.POST)
    @ApiOperation(value = "账户登出",httpMethod = "POST",response = ResultResp.class)
    public String logout() throws Exception {

        try{
//            Session session = TokenHelper.getToken();
//            if(session == null) throw new UnauthenticatedException();
//            AccountPostResult<AccountHttpSender.DummyType> result = AccountHttpSender.Instance().ObjectPost(
//                    "/account/logout",
//                    ToJsonUtil.rawJson().put("token",session.getToken()).toString(),
//                    AccountHttpSender.DummyType.class);
//            if(!result.getResult() || result.getBody() == null) throw new Exception(result.getMessage());

            return ToJsonUtil.succussResult();
        } catch (Exception e) {
            if(e instanceof UnauthenticatedException) throw e;
            return ToJsonUtil.exceptionToResult(e);
        }
    }
    
    @RequestMapping(value="/test", method = RequestMethod.POST)
    @ApiOperation(value = "测试方法",httpMethod = "POST",response = ResultResp.class)
    public void test(){
    	
    }
}
