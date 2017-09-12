package com.baliset.util.model;

import org.springframework.http.*;

public class InvalidEndpoint
{
  public String         error;
  public String         url;
  public ExplodedStatus status;


  public InvalidEndpoint(String s, HttpStatus status, String e)
  {
    url = s;
    this.status = new ExplodedStatus(status);
    error = e;
  }
}
