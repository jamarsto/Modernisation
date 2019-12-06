package uk.me.jasonmarston.mvc.controller.advice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConstraintAdvice {
	private static final String EXCEPTION =
			"javax.servlet.error.exception";
	private static final String STATUS_CODE = 
			"javax.servlet.error.status_code";

	@ExceptionHandler({
			ConstraintViolationException.class,
			MethodArgumentNotValidException.class})
	public String exception(final Throwable e, HttpServletRequest req) {
		req.setAttribute(EXCEPTION, e);
		req.setAttribute(STATUS_CODE,  HttpStatus.BAD_REQUEST.value());

		return "forward:/error";
	}
}