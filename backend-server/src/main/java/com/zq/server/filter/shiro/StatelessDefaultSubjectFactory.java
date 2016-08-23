package com.zq.server.filter.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 
 * 
 * @ClassName: StatelessDefaultSubjectFactory 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午10:50:22
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session 开启可支持JSP
        // context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
