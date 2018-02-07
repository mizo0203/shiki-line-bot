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
    reversiModel = parseReversiBoard(config.getReversiBoard(), config.getPieces());
  }

  private static ReversiModel parseReversiBoard(@Nullable String reversiBoard, int nextPieces) {
    ReversiModel reversiModel = new ReversiModel();
    if (reversiBoard == null) {
      reversiModel.reset();
    } else {
      reversiModel.setNextPieces(Pieces.values()[nextPieces]);
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
    if (reversiModel.getNextPieces() != null) {
      config.setReversiBoard(parseReversiBoard());
      config.setPieces(reversiModel.getNextPieces().ordinal());
    } else {
      config.setReversiBoard(null);
      config.setPieces(0);
    }
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
}
