package com.mizo0203.shiki.domain.model;

public abstract class ReversiFormat {

  public String format(ReversiModel reversiModel) {
    throw new UnsupportedOperationException();
  }

  public ReversiModel parse(String source) {
    throw new UnsupportedOperationException();
  }
}
