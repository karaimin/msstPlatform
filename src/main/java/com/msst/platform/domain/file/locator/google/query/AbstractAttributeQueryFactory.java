package com.msst.platform.domain.file.locator.google.query;

public abstract class AbstractAttributeQueryFactory {

  private static final char QUOTE = '\'';

  private String attributeName;
  private Object value;

  public AbstractAttributeQueryFactory(String attributeName, Object value) {
    this.attributeName = attributeName;
    this.value = value;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public Object getValue() {
    return value;
  }

  protected abstract AttributeQuery equal();

  protected abstract AttributeQuery notEqual();

  protected abstract AttributeQuery contains();

  protected static Object normalizeValue(Object value){
    if(value instanceof String) {
      return new StringBuilder().append(QUOTE).append(value).append(QUOTE).toString();
    }

    return value;
  }
}
