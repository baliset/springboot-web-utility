package com.baliset.util.controller;



import com.baliset.util.Alerts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler
{
    public static final String sDefaultErrorView = "error";
    protected static Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);


    private void alertWrap(Category c, Health h, Remedy r, String err, String desc)
    {
        String logit = String.format("+++C:%s, H:%s, R:%s, Err:%s, Desc:%s",c,h,r,err,desc);
        logger.error(logit);
//        alerts.alert(c,h,r,err,desc);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)  // 502
    @ExceptionHandler(DataAccessException.class)
    public void dae(HttpServletRequest req, Exception e) {
        String excMessage = e.toString(); //todo implement convertThrowableToLongText(e);
        alertWrap(
                Category.DataAccess,
                Health.Unknown,
                Remedy.EscalateToDevTeam,
                req.getRequestURI(),excMessage);
    }

    private String composeErrorEventName(HttpServletRequest req,  Exception e,  ResponseStatus rs)
    {
        String  exceptionClassname = e.getClass().getSimpleName();
        String responseCode = rs != null? rs.value().toString(): "No code";
        return String.format("GlobalException: %s, Status: %s", exceptionClassname, responseCode);
    }

    private String composeErrorDescription(HttpServletRequest req, Exception e, ResponseStatus rs)
    {
        String url = req.getRequestURI();
        String excMessage = e.toString(); // todo was convertThrowableToLongText(e);
        return String.format("Path:%s, Message:%s" ,url, excMessage);
    }

    // services should not be prodded for calls that don't exist, if they are, report it as suspicious activity
    // while returning 501 errors to the caller
    @ExceptionHandler(value=Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.


        final ResponseStatus annotation = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);

        String errname = composeErrorEventName(req, e, annotation);
        String errdesc = composeErrorDescription(req,e,annotation);

        alertWrap(
                Category.SuspiciousInput,
                Health.Normal,
                Remedy.TreatAsSuspicious,
                errname, errdesc);

        if(annotation != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(sDefaultErrorView);
        return mav;
    }


}