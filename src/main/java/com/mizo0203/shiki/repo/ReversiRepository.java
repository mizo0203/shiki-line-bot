package com.mizo0203.shiki.repo;

import com.mizo0203.shiki.domain.model.Pieces;
import com.mizo0203.shiki.domain.model.ReversiModel;
import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.annotation.Nullable;
import java.io.Closeable;

public class ReversiRepository implements Closeable {

  private final LineTalkRoomConfig config;
  private final ReversiModel reversiModel;

  public ReversiRepository(LineTalkRoomConfig config) {
    this.config = config;
    reversiModel = parseReversiBoard(config.getReversiBoard());
  }

  private static ReversiModel parseReversiBoard(@Nullable String reversiBoard) {
    ReversiModel reversiModel = new ReversiModel();
    if (reversiBoard == null) {
      reversiModel.reset();
    } else {
      for (int y = 1; y <= 8; y++) {
        for (int x = 1; x <= 8; x++) {
          Pieces pieces = Pieces.values()[Integer.parseInt(reversiBoard.substring(0, 1))];
          reversiBoard = reversiBoard.substring(1);
          reversiModel.setPieces(x, y, pieces);
        }
      }
    }
    return reversiModel;
  }

  public ReversiModel getReversiModel() {
    return reversiModel;
  }

  @Override
  public void close() {
    config.setReversiBoard(parseReversiBoard());
  }

  private String parseReversiBoard() {
    StringBuilder reversiBoard = new StringBuilder();
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        reversiBoard.append(reversiModel.getPieces(x, y).ordinal());
      }
    }
    return reversiBoard.toString();
  }

  public Pieces getNextPieces() {
    int pieces = config.getPieces();
    if (pieces < 0) {
      return null;
    }
    return Pieces.values()[pieces];
  }

  public void setNextPieces(@Nullable Pieces pieces) {
    if (pieces != null) {
      config.setPieces(pieces.ordinal());
    } else {
      config.setPieces(-1);
    }
  }

  public void reset() {
    reversiModel.reset();
    setNextPieces(Pieces.BLACK);
  }
}
