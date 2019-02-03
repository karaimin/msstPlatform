package com.msst.platform.domain.file.locator.google.query;

public class ParentAttributeQueryFactory extends ReverseAttributeQueryFactory {
  public ParentAttributeQueryFactory(Object value) {
    super("parents", value);
  }

  @Override
  public AttributeQuery in() {
    return super.in();
  }
}
