package com.msst.platform.web.rest.errors;

public class MovieNotFoundException extends BadRequestAlertException{
  public MovieNotFoundException(String defaultMessage) {
    super(defaultMessage, "Movie", "movieNotFound");
  }
}
