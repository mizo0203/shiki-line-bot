package com.mizo0203.shiki.domain.model;

public enum Pieces {
  BLACK, //
  WHITE, //
  EMPTY, //
  ;

  public Pieces getOpponent() {
    switch (this) {
      case BLACK:
        return WHITE;
      case WHITE:
        return BLACK;
      default:
        return null;
    }
  }
}
