package com.mizo0203.shiki.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtil {

  private static final Logger LOG = Logger.getLogger(HttpUtil.class.getName());

  public static void post(URL url, Map<String, String> reqProp, String body, Callback callback) {
    LOG.info("post url:     " + url);
    LOG.info("post reqProp: " + reqProp);
    LOG.info("post body:    " + body);
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      for (String key : reqProp.keySet()) {
        connection.setRequestProperty(key, reqProp.get(key));
      }
      BufferedWriter writer =
          new BufferedWriter(
              new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
      writer.write(body);
      writer.flush();
      LOG.info("getResponseCode():    " + connection.getResponseCode());
      LOG.info("getResponseMessage(): " + connection.getResponseMessage());
      if (connection.getErrorStream() != null) {
        LOG.severe("getErrorStream(): " + PaserUtil.parseString(connection.getErrorStream()));
      }
      if (callback != null) {
        callback.response(connection);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public static void get(URL url, Map<String, String> reqProp, Callback callback) {
    LOG.info("get url:     " + url);
    LOG.info("get reqProp: " + reqProp);
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      for (String key : reqProp.keySet()) {
        connection.setRequestProperty(key, reqProp.get(key));
      }
      LOG.info("getResponseCode():    " + connection.getResponseCode());
      LOG.info("getResponseMessage(): " + connection.getResponseMessage());
      if (connection.getErrorStream() != null) {
        LOG.severe("getErrorStream(): " + PaserUtil.parseString(connection.getErrorStream()));
      }
      if (callback != null) {
        callback.response(connection);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public interface Callback {

    void response(HttpURLConnection connection);
  }
}
