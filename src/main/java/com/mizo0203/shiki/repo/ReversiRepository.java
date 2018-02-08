package com.mizo0203.shiki.repo;

import com.mizo0203.shiki.domain.model.Pieces;
import com.mizo0203.shiki.domain.model.RepoReversiFormat;
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
    if (reversiBoard == null) {
      return new ReversiModel();
    } else {
      ReversiModel reversiModel = new RepoReversiFormat().parse(reversiBoard);
      reversiModel.setNextPieces(Pieces.values()[nextPieces]);
      return reversiModel;
    }
  }

  public ReversiModel getReversiModel() {
    return reversiModel;
  }

  @Override
  public void close() {
    if (reversiModel.getNextPieces() != null) {
      config.setReversiBoard(formatReversiBoard());
      config.setPieces(reversiModel.getNextPieces().ordinal());
    } else {
      config.setReversiBoard(null);
      config.setPieces(0);
    }
  }

  private String formatReversiBoard() {
    return new RepoReversiFormat().format(reversiModel);
  }
}
