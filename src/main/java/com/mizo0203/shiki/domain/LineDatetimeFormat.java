package com.mizo0203.shiki.domain;

import java.text.SimpleDateFormat;

public class LineDatetimeFormat extends SimpleDateFormat {

  public LineDatetimeFormat() {
    super("yyyy-MM-dd'T'HH:mm");
    setTimeZone(Define.LINE_TIME_ZONE);
  }
}
