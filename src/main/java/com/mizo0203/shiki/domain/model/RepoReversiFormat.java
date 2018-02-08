package com.mizo0203.shiki.domain.model;

public class RepoReversiFormat extends ReversiFormat {

  @Override
  public String format(ReversiModel reversiModel) {
    StringBuilder reversiBoard = new StringBuilder();
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        reversiBoard.append(reversiModel.getPieces(x, y).ordinal());
      }
    }
    return reversiBoard.toString();
  }

  @Override
  public ReversiModel parse(String source) {
    ReversiModel reversiModel = new ReversiModel();
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        Pieces pieces = Pieces.values()[Integer.parseInt(source.substring(0, 1))];
        source = source.substring(1);
        reversiModel.setPieces(x, y, pieces);
      }
    }
    return reversiModel;
  }
}
