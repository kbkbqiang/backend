package com.zq.server.config;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.zq.backend.common.utils.ToJsonUtil;

/**
 * 控制器增强处理
 * 
 * @ClassName: DefaultExceptionHandler 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年9月5日 下午6:40:02
 */
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		if (ex instanceof org.apache.shiro.authz.UnauthenticatedException) {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(HttpStatus.UNAUTHORIZED.getReasonPhrase()), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		} else if (ex instanceof org.apache.shiro.authz.UnauthorizedException) {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(HttpStatus.FORBIDDEN.getReasonPhrase()), new HttpHeaders(), HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN);
		}
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", ex, 0);
		}

		if (ex instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()), new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
		} else if (ex instanceof org.springframework.web.servlet.NoHandlerFoundException) {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(HttpStatus.NOT_FOUND.getReasonPhrase()), new HttpHeaders(), HttpStatus.NOT_FOUND);
		} else if (ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult("parameter error"), new HttpHeaders(), HttpStatus.OK);
		} else if (ex instanceof org.springframework.beans.TypeMismatchException || ex instanceof org.springframework.http.converter.HttpMessageNotReadableException) {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult("parameter error"), new HttpHeaders(), HttpStatus.OK);
		} else if (ex instanceof BindException) {
			BindException exception = (BindException) ex;
			List<ObjectError> allerr = exception.getAllErrors();

			StringBuilder sb = new StringBuilder();
			boolean firstflag = true;
			for (ObjectError item : allerr) {
				if (firstflag)
					firstflag = false;
				else
					sb.append(",");
				sb.append(item.getDefaultMessage());
			}

			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(sb.toString()), new HttpHeaders(), HttpStatus.OK);
		} else if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
			BindingResult result = exception.getBindingResult();
			List<ObjectError> allerr = result.getAllErrors();

			StringBuilder sb = new StringBuilder();
			boolean firstflag = true;
			for (ObjectError item : allerr) {
				if (firstflag)
					firstflag = false;
				else
					sb.append(",");
				sb.append(item.getDefaultMessage());
			}
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(sb.toString()), new HttpHeaders(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(ToJsonUtil.exceptionToResult(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}