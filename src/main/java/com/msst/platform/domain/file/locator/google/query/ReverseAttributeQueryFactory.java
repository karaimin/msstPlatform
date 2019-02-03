package com.msst.platform.domain.file.locator.google.query;

public class ReverseAttributeQueryFactory extends AbstractAttributeQueryFactory {

  public ReverseAttributeQueryFactory(String attributeName, Object value) {
    super(normalizeValue(value).toString(), attributeName);
  }

  @Override
  protected AttributeQuery equal(){
    return AttributeQuery.getInstance(this, AttributeQuery.Operator.EQUAL);
  }

  @Override
  protected AttributeQuery notEqual(){
    return AttributeQuery.getInstance(this, AttributeQuery.Operator.NOT_EQUAL);
  }

  @Override
  protected AttributeQuery contains(){
    return AttributeQuery.getInstance(this, AttributeQuery.Operator.CONTAINS);
  }

  protected AttributeQuery in(){
    return AttributeQuery.getInstance(this, AttributeQuery.Operator.IN);
  }
}
