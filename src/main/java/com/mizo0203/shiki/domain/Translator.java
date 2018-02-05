package com.mizo0203.shiki.domain;

import com.mizo0203.shiki.domain.model.Pieces;
import com.mizo0203.shiki.domain.model.ReversiModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* package */ class Translator {

  /* package */ Translator() {}

  public Date parseDatetime(String datetime) {
    try {
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
      fmt.setTimeZone(Define.LINE_TIME_ZONE);
      return fmt.parse(datetime);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Date parseDate(String date) {
    // 未実装
    throw new UnsupportedOperationException();
  }

  public Date parseTime(String time) {
    // 未実装
    throw new UnsupportedOperationException();
  }

  public boolean play(ReversiModel reversiModel, String message, Pieces pieces) {
    if (message.length() != 2) {
      return false;
    }
    int x = parseX(message);
    int y = parseY(message);
    return x != 0 && y != 0 && reversiModel.play(x, y, pieces);
  }

  private int parseX(String message) {
    String x = message.substring(0, 1);
    switch (x) {
      case "a":
      case "A":
        return ReversiModel.A;
      case "b":
      case "B":
        return ReversiModel.B;
      case "c":
      case "C":
        return ReversiModel.C;
      case "d":
      case "D":
        return ReversiModel.D;
      case "e":
      case "E":
        return ReversiModel.E;
      case "f":
      case "F":
        return ReversiModel.F;
      case "g":
      case "G":
        return ReversiModel.G;
      case "h":
      case "H":
        return ReversiModel.H;
      default:
        return 0;
    }
  }

  private int parseY(String message) {
    try {
      String y = message.substring(1, 2);
      return Integer.parseInt(y);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public String parseLineMessageText(ReversiModel reversiModel, Pieces pieces) {
    StringBuilder messageText = new StringBuilder();
    int black = reversiModel.countPieces(Pieces.BLACK);
    int white = reversiModel.countPieces(Pieces.WHITE);
    messageText.append("黒Ｘ：").append(black).append("　白Ｏ：").append(white).append("\n");
    messageText.append("＼ＡＢＣＤＥＦＧＨ\n");
    for (int y = 1; y <= 8; y++) {
      messageText.append(parseAAA(y));
      for (int x = 1; x <= 8; x++) {
        messageText.append(parseBBB(reversiModel.getPieces(x, y)));
      }
      messageText.append("\n");
    }
    if (pieces == null) {
      messageText.append("ゲーム終了\n");
      if (black != white) {
        messageText.append("人生の勝利者：").append(black > white ? "黒Ｘ" : "白Ｏ");
      } else {
        messageText.append("引き分け");
      }
    } else {
      switch (pieces) {
        case BLACK:
          messageText.append("黒Ｘの手番：");
          break;
        case WHITE:
          messageText.append("白Ｏの手番：");
          break;
        default:
          // NOP
          break;
      }
    }
    return messageText.toString();
  }

  private String parseBBB(@NotNull Pieces pieces) {
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

  private String parseAAA(@Min(1) @Max(8) int y) {
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
