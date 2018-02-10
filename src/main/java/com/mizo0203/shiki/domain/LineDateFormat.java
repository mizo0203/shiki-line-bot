package com.mizo0203.shiki.domain;

import java.text.SimpleDateFormat;

public class LineDateFormat extends SimpleDateFormat {

  public LineDateFormat() {
    super("yyyy-MM-dd");
    setTimeZone(Define.LINE_TIME_ZONE);
  }
}
