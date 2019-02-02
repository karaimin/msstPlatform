package com.msst.platform.domain.file.locator.google.query;

public class AttributeQuery{
  private static final String EMPTY_SPACE = " ";

  private String queryAttribute;
  private Operator operator;
  private Object value;

  private AttributeQuery(String queryAttribute, Operator operator, Object value) {
    this.queryAttribute = queryAttribute;
    this.operator = operator;
    this.value = value;
  }

  public enum Operator{
    EQUAL("="),
    CONTAINS ("contains"),
    NOT_EQUAL("!="),
    IN("in");

    private String name;

    Operator(String name){
      this.name = name;
    }

    private String apply(Object value){
      // @formatter:off
      return new StringBuilder().append(name).append(EMPTY_SPACE).append(value.toString()).toString();
      // @formatter:on
    }
  }

  public static AttributeQuery getInstance(AbstractAttributeQueryFactory queryFactory, Operator operator){
    return new AttributeQuery(queryFactory.getAttributeName(), operator, queryFactory.getValue());
  }

  @Override
  public String toString(){
    return new StringBuilder().append(queryAttribute).append(EMPTY_SPACE).append(operator.apply(value)).toString();
  }
}
