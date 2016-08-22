package com.zq.backend.common.utils.valid;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @ClassName: ValidatorUtil 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:52:52
 */
public class ValidatorUtil {
    private ValidatorFactory factory = null;
    private Validator validator = null;

    public ValidatorUtil()
    {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public <T> void validateProperty(T var1, String var2) throws ValidException {
        Set<ConstraintViolation<T>> errors = validator.validateProperty( var1, var2 );
        Set<ConstraintViolation<?>> errorsss = new HashSet<ConstraintViolation<?>>();
        for(ConstraintViolation<?> item : errors) errorsss.add(item);
        if(!errors.isEmpty()) throw new ValidException(errorsss);
    }

    public <T> void validate(T var1) throws ValidException {
        Set errors = this.validator.validate(var1, new Class[0]);
        HashSet errorsss = new HashSet();
        Iterator var5 = errors.iterator();

        while(var5.hasNext()) {
            ConstraintViolation item = (ConstraintViolation)var5.next();
            errorsss.add(item);
        }

        if(!errors.isEmpty()) {
            throw new ValidException(errorsss);
        }
    }
}
