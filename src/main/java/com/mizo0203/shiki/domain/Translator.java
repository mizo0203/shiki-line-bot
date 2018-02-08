package com.mizo0203.shiki.domain;

import com.mizo0203.shiki.domain.model.LineReversiFormat;
import com.mizo0203.shiki.domain.model.ReversiModel;

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

  public void play(ReversiModel reversiModel, String message) {
    if (message.length() != 2) {
      return;
    }
    int x = parseX(message);
    int y = parseY(message);
    if (x != 0 && y != 0) {
      reversiModel.play(x, y);
    }
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

  public String parseLineMessageText(ReversiModel reversiModel) {
    return new LineReversiFormat().format(reversiModel);
  }
}
