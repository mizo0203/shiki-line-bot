package com.mizo0203.shiki.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PaserUtil {

  /** @see ObjectMapper#writeValueAsString(Object) */
  public static String toJson(ReplyMessage data) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(data);
  }

  /** @see ObjectMapper#writeValueAsString(Object) */
  public static String toJson(PushMessage data) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(data);
  }

  private static String parseString(BufferedReader br) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
    br.close();
    return sb.toString();
  }

  public static String parseString(InputStream is) throws IOException {
    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
    return parseString(new BufferedReader(isr));
  }
}
