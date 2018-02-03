package com.mizo0203.shiki.domain.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class ReversiModel {
  public static final int A = 1;
  public static final int B = 2;
  public static final int C = 3;
  public static final int D = 4;
  public static final int E = 5;
  public static final int F = 6;
  public static final int G = 7;
  public static final int H = 8;
  private static final int PERIMETER = 8;
  private final Pieces[][] board = new Pieces[10][10];
  private final int[] dx_array = new int[] {-1, 0, 1, -1, 1, -1, 0, 1};
  private final int[] dy_array = new int[] {-1, -1, -1, 0, 0, 1, 1, 1};

  public boolean play(@Min(1) @Max(8) int x, @Min(1) @Max(8) int y, Pieces pieces) {
    if (reversePieces(x, y, pieces) == 0) {
      return false;
    }
    board[y][x] = pieces;
    return true;
  }

  public void setPieces(@Min(1) @Max(8) int x, @Min(1) @Max(8) int y, Pieces pieces) {
    board[y][x] = pieces;
  }

  @NotNull
  public Pieces getPieces(@Min(1) @Max(8) int x, @Min(1) @Max(8) int y) {
    return board[y][x];
  }

  public void reset() {
    for (Pieces[] piecesLine : board) {
      Arrays.fill(piecesLine, Pieces.EMPTY);
    }
    board[4][D] = Pieces.WHITE;
    board[4][E] = Pieces.BLACK;
    board[5][D] = Pieces.BLACK;
    board[5][E] = Pieces.WHITE;
  }

  private int reversePieces(@Min(1) @Max(8) int x, @Min(1) @Max(8) int y, Pieces oneself) {
    if (board[y][x] != Pieces.EMPTY) {
      return 0;
    }
    int cnt = 0;
    Pieces opponent = oneself.getOpponent();
    for (int i = 0; i < PERIMETER; i++) {
      int dy = dy_array[i];
      int dx = dx_array[i];
      int d = 1;
      while (board[y + (dy * d)][x + (dx * d)] == opponent) {
        d++;
      }
      if (board[y + (dy * d)][x + (dx * d)] == oneself) {
        for (int j = 1; j < d; j++) {
          board[y + (dy * j)][x + (dx * j)] = oneself;
          cnt++;
        }
      }
    }
    return cnt;
  }

  private boolean canReversePieces(@Min(1) @Max(8) int x, @Min(1) @Max(8) int y, Pieces oneself) {
    if (board[y][x] != Pieces.EMPTY) {
      return false;
    }
    Pieces opponent = oneself.getOpponent();
    for (int i = 0; i < PERIMETER; i++) {
      int dy = dy_array[i];
      int dx = dx_array[i];
      int d = 1;
      while (board[y + (dy * d)][x + (dx * d)] == opponent) {
        d++;
      }
      if (board[y + (dy * d)][x + (dx * d)] == oneself) {
        if (1 < d) {
          return true;
        }
      }
    }
    return false;
  }

  public Pieces nextPieces(Pieces oneself) {
    Pieces opponent = oneself.getOpponent();
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        if (canReversePieces(x, y, opponent)) {
          return opponent;
        }
      }
    }
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        if (canReversePieces(x, y, oneself)) {
          return oneself;
        }
      }
    }
    return null;
  }
}
