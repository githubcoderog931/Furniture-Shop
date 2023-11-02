//package com.sheryians.major.controller;
//
//
//import javassist.NotFoundException;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CustomGlobalExceptionHandler {
//    @ExceptionHandler(NotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleNotFoundException() {
//        return "error/404";
//    }
//}
