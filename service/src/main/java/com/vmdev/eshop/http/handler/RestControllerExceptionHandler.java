package com.vmdev.eshop.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.vmdev.eshop.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
