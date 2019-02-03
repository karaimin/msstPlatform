package com.msst.platform.domain.file.locator.google.query;

public class IdQueryFactory extends AttributeQueryFactory {

  public IdQueryFactory(Object value) {
    super("id", value);
  }

  @Override
  public AttributeQuery equal(){
    return AttributeQuery.getInstance(this, AttributeQuery.Operator.EQUAL);
  }

}
