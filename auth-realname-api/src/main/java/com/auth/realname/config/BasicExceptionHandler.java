package com.auth.realname.config;

import java.io.Serializable;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth.realname.exception.SystemBusyException;
import com.auth.realname.resp.BaseDto;
import com.auth.realname.resp.ResultCode;
import com.auth.realname.resp.ResultUtil;
import com.auth.realname.util.ExceptionUtil;


@ControllerAdvice
public class BasicExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(BasicExceptionHandler.class);

    /**
     * 系统繁忙
     */
    @ExceptionHandler(value = SystemBusyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseDto<Serializable> systemBusyException(SystemBusyException ex) {
    	logger.error("SystemBusyException :{}", ExceptionUtil.asString(ex));
    	
    	return ResultUtil.result(ex.getCode(), ex.getMessage());
	}
    
    /**
     * 参数验证失败
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseDto<Serializable> badRequestException(ValidationException ex) {
        logger.error("ValidationException :{}", ExceptionUtil.asString(ex));

        return ResultUtil.result(ResultCode.BAD_REQUEST.getCode(), ex.getMessage());
    }
    
    /**
     * 404
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseDto<Serializable> noHandlerFoundException(Exception  ex) {
        logger.error("noHandlerFoundException :{}", ExceptionUtil.asString(ex));

        return ResultUtil.result(ResultCode.URL_ERROR.getCode(), ex.getMessage());
    }

    /**
     * 暂时未处理的异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<BaseDto<Serializable>> exception(Exception ex, HttpServletResponse response) {
        logger.error("Response :{}; Exception :{}", response.getStatus(), ExceptionUtil.asString(ex));

        ResponseEntity<BaseDto<Serializable>> resp = null;
		if (ex instanceof ServletRequestBindingException) {// MissingRequestHeaderException
			resp = new ResponseEntity<BaseDto<Serializable>>(
					ResultUtil.result(ResultCode.BAD_REQUEST.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
		}

		if (ex instanceof HttpRequestMethodNotSupportedException) {
			//请求方式不支持
			resp = new ResponseEntity<BaseDto<Serializable>>(
					ResultUtil.result(ResultCode.METHOD_ERROR.getCode(), ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
		}
		
		if (ex instanceof NoHandlerFoundException) {
			//没找到处理方法
			resp = new ResponseEntity<BaseDto<Serializable>>(
					ResultUtil.result(ResultCode.URL_ERROR.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
		}
		
		if (ex instanceof HttpMediaTypeException) {
			//参数提交格式不正确
			resp = new ResponseEntity<BaseDto<Serializable>>(
					ResultUtil.result(ResultCode.MEDIA_TYPE_ERROR.getCode(), ex.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		
		if (Objects.isNull(resp)) {
			resp = new ResponseEntity<BaseDto<Serializable>>(
					ResultUtil.result(ResultCode.ERROR.getCode(), ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return resp;
    }
}
