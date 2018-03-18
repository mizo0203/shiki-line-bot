package com.mizo0203.shiki.domain.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LineReversiFormat extends ReversiFormat {

  @Override
  public String format(ReversiModel reversiModel) {
    StringBuilder messageText = new StringBuilder();
    int black = reversiModel.countPieces(Pieces.BLACK);
    int white = reversiModel.countPieces(Pieces.WHITE);
    messageText.append("黒Ｘ：").append(black).append("　白Ｏ：").append(white).append("\n");
    messageText.append("＼ＡＢＣＤＥＦＧＨ／\n");
    for (int y = 1; y <= 8; y++) {
      messageText.append(formatY(y));
      for (int x = 1; x <= 8; x++) {
        messageText.append(formatPieces(reversiModel.getPieces(x, y)));
      }
      messageText.append(formatY(y)).append("\n");
    }
    messageText.append("／ＡＢＣＤＥＦＧＨ＼\n");
    Pieces nextPieces = reversiModel.getNextPieces();
    if (nextPieces == null) {
      messageText.append("そこまで！\n");
      if (black != white) {
        messageText.append("人生の勝利者：").append(black > white ? "黒Ｘ" : "白Ｏ");
      } else {
        messageText.append("引き分け");
      }
    } else {
      boolean nextPassed = reversiModel.isNextPassed();
      switch (nextPieces) {
        case BLACK:
          if (nextPassed) {
            messageText.append("白Ｏの手番：パス\n");
          }
          messageText.append("黒Ｘの手番：");
          break;
        case WHITE:
          if (nextPassed) {
            messageText.append("黒Ｘの手番：パス\n");
          }
          messageText.append("白Ｏの手番：");
          break;
        default:
          // NOP
          break;
      }
    }
    return messageText.toString();
  }

  private String formatPieces(@NotNull Pieces pieces) {
    switch (pieces) {
      case BLACK:
        return "Ｘ";
      case WHITE:
        return "Ｏ";
      case EMPTY:
        return "＿";
      default:
        return "";
    }
  }

  private String formatY(@Min(1) @Max(8) int y) {
    switch (y) {
      case 1:
        return "１";
      case 2:
        return "２";
      case 3:
        return "３";
      case 4:
        return "４";
      case 5:
        return "５";
      case 6:
        return "６";
      case 7:
        return "７";
      case 8:
        return "８";
      default:
        return "";
    }
  }
}
