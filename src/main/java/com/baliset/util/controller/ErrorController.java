package com.baliset.util.controller;

import com.baliset.util.Alerts.*;
import com.baliset.util.RequestBasedSerializationUtil;
import com.baliset.util.model.InvalidEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController
{

  private static Logger logger = LoggerFactory.getLogger(ErrorController.class);


  @SuppressWarnings("SameParameterValue")
  private void alertWrap(String ip, Category c, String err, String desc)
  {
    String logit = String.format("+++AlertActivity: {ip:%s, c:%s, err:{%s}, desc:{%s}}", ip, c, err, desc);
    logger.error(logit);
//        alerts.alertActivity("", ip, "", c, err, desc);
  }


  @RequestMapping(value = "/error", produces = {"application/json", "application/xml", "text/xml", "text/x-yaml"})
  public String error(HttpServletRequest req, Exception exception)
  {
    exception.printStackTrace();
    return RequestBasedSerializationUtil.serializeInAcceptedFormat(req, exception);
  }

  @RequestMapping(produces = {"application/json" /*"application/xml", "text/xml", "text/x-yaml"*/})
  public ResponseEntity<InvalidEndpoint> noSuchRequest(final HttpServletRequest request)
  {
    String ip = request.getRemoteAddr();

    // as a service we treat any access to unsupported endpoints to be suspicious
    // respond with Json to be friendly to clients like account app and their handling
    alertWrap(ip, Category.SuspiciousInput, "NoSuchRequest", request.getRequestURI());

    HttpStatus status =   HttpStatus.NOT_IMPLEMENTED;
    return new ResponseEntity<>(new InvalidEndpoint(request.getRequestURI(),status, "Invalid Endpoint"), HttpStatus.NOT_IMPLEMENTED);
  }


}