package com.zq.backend.common.utils.valid;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 
 * @ClassName: ValidException 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:52:45
 */
public class ValidException extends Exception{
    private Set<ConstraintViolation<?>> errors;

    public Set<ConstraintViolation<?>> getErrors() {
        return errors;
    }

    public void setErrors(Set<ConstraintViolation<?>> errors) {
        this.errors = errors;
    }

    public ValidException(Set<ConstraintViolation<?>> errors) {
        this.errors = errors;
    }
}
