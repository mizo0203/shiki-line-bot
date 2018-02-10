package com.mizo0203.shiki.domain;

import java.text.SimpleDateFormat;

public class LineTimeFormat extends SimpleDateFormat {

  public LineTimeFormat() {
    super("HH:mm");
    setTimeZone(Define.LINE_TIME_ZONE);
  }
}
