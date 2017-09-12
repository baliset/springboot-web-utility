package com.baliset.util.model;

import org.springframework.http.*;

// since we don't like how HttpStatus serializes, this exploded model is more informative for error messages
public class ExplodedStatus
{
  public int value;
  public String series;
  public String reason;

  public ExplodedStatus(HttpStatus httpStatus)
  {
    value = httpStatus.value();
    series = httpStatus.series().name();
    reason = httpStatus.getReasonPhrase();
  }
}
