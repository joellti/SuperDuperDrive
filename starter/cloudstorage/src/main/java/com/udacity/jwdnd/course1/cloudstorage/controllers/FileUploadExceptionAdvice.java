package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    //@ExceptionHandler(MaxUploadSizeExceededException.class)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleMaxSizeException(final MaxUploadSizeExceededException throwable, final Model model) {
        model.addAttribute("maxUploadSizeExceeded", true);
        return "result";
    }
}
