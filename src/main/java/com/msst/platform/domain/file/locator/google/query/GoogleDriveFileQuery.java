package com.msst.platform.domain.file.locator.google.query;

public class GoogleDriveFileQuery {
  private static final char OPEN_BRACKET = '(';
  private static final char CLOSE_BRACKET = ')';

  private String currentQuery;

  public GoogleDriveFileQuery(AttributeQuery query) {
    this.currentQuery = query.toString();
  }

  private GoogleDriveFileQuery(String validQueryString){
    this.currentQuery = validQueryString;
  }

  public GoogleDriveFileQuery and(GoogleDriveFileQuery otherCurrentQuery){
    String currentNormalizedQuery = surroundQueryInBrackets(currentQuery);
    String otherNormalizedQuery = surroundQueryInBrackets(otherCurrentQuery.currentQuery);

    // @formatter:off
    String newQuery =  new StringBuilder()
        .append(currentNormalizedQuery)
        .append(" and ")
        .append(otherNormalizedQuery)
        .toString();
    // @formatter:on

    return new GoogleDriveFileQuery(newQuery);
  }

  public GoogleDriveFileQuery and(AttributeQuery query){
    GoogleDriveFileQuery other = new GoogleDriveFileQuery(query);
    return and(other);
  }


  private String surroundQueryInBrackets(String query){
    return new StringBuilder().append(OPEN_BRACKET).append(query).append(CLOSE_BRACKET).toString();
  }

  @Override
  public String toString(){
    return currentQuery;
  }

}
