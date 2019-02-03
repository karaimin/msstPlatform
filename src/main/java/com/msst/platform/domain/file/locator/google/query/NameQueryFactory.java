package com.msst.platform.domain.file.locator.google.query;

public class NameQueryFactory extends AttributeQueryFactory{

  public NameQueryFactory(Object value) {
    super("name", value);
  }

  @Override
  public AttributeQuery equal(){
    return super.equal();
  }

  @Override
  public AttributeQuery notEqual(){
    return super.notEqual();
  }

  @Override
  public AttributeQuery contains(){
    return super.contains();
  }
}
