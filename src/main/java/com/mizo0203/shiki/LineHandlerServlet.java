package com.mizo0203.shiki;

import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.*;
import com.mizo0203.shiki.domain.LineDateFormat;
import com.mizo0203.shiki.domain.LineDatetimeFormat;
import com.mizo0203.shiki.domain.LineTimeFormat;
import com.mizo0203.shiki.domain.UseCase;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LineHandlerServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(LineHandlerServlet.class.getName());

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) {
    onLineWebhook(req, resp);
  }

  /**
   * LINE Platform からのリクエストを受信
   *
   * <p>友だち追加やメッセージの送信のようなイベントがトリガーされると、webhook URL に HTTPS POST リクエストが送信されます。Webhook URL
   * はチャネルに対してコンソールで設定します。
   *
   * <p>リクエストはボットアプリのサーバーで受信および処理されます。
   *
   * @param req an {@link HttpServletRequest} object that contains the request the client has made
   *     of the servlet
   * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends
   *     to the client
   */
  private void onLineWebhook(HttpServletRequest req, HttpServletResponse resp) {
    try (UseCase useCase = new UseCase()) {
      List<Event> eventList = useCase.getCallbackEventList(req);
      if (eventList == null) {
        return;
      }
      for (Event event : eventList) {
        if (event instanceof MessageEvent) {
          onLineMessage(useCase, (MessageEvent) event);
        } else if (event instanceof UnfollowEvent) {
          onLineUnfollow(useCase, (UnfollowEvent) event);
        } else if (event instanceof FollowEvent) {
          onLineFollow(useCase, (FollowEvent) event);
        } else if (event instanceof JoinEvent) {
          onLineJoin(useCase, (JoinEvent) event);
        } else if (event instanceof LeaveEvent) {
          onLineLeave(useCase, (LeaveEvent) event);
        } else if (event instanceof PostbackEvent) {
          onLinePostback(useCase, (PostbackEvent) event);
        } else if (event instanceof BeaconEvent) {
          onLineBeacon(useCase, (BeaconEvent) event);
        }
      }
    } finally {
      // ボットアプリのサーバーに webhook から送信される HTTP POST リクエストには、ステータスコード 200 を返す必要があります。
      // https://developers.line.me/ja/docs/messaging-api/reference/#anchor-99cdae5b4b38ad4b86a137b508fd7b1b861e2366
      resp.setStatus(HttpServletResponse.SC_OK);
    }
  }

  private void onLineMessage(UseCase useCase, MessageEvent event) {
    MessageContent message = event.getMessage();
    if (message instanceof TextMessageContent) {
      onLineTextMessage(useCase, event, (TextMessageContent) message);
    } else if (message instanceof ImageMessageContent) {
      onLineImageMessage(useCase, event, (ImageMessageContent) message);
    } else if (message instanceof LocationMessageContent) {
      onLineLocationMessage(useCase, event, (LocationMessageContent) message);
    } else if (message instanceof AudioMessageContent) {
      onLineAudioMessage(useCase, event, (AudioMessageContent) message);
    } else if (message instanceof VideoMessageContent) {
      onLineVideoMessage(useCase, event, (VideoMessageContent) message);
    } else if (message instanceof StickerMessageContent) {
      onLineStickerMessage(useCase, event, (StickerMessageContent) message);
    } else if (message instanceof FileMessageContent) {
      onLineFileMessage(useCase, event, (FileMessageContent) message);
    }
  }

  private void onLineTextMessage(UseCase useCase, MessageEvent event, TextMessageContent message) {
    useCase.onLineTextMessage(event, message);
  }

  private void onLineImageMessage(
      UseCase useCase, MessageEvent event, ImageMessageContent message) {
    LOG.info("onLineImageMessage");
    // NOP
  }

  private void onLineLocationMessage(
      UseCase useCase, MessageEvent event, LocationMessageContent message) {
    LOG.info("onLineLocationMessage");
    // NOP
  }

  private void onLineAudioMessage(
      UseCase useCase, MessageEvent event, AudioMessageContent message) {
    LOG.info("onLineAudioMessage");
    // NOP
  }

  private void onLineVideoMessage(
      UseCase useCase, MessageEvent event, VideoMessageContent message) {
    LOG.info("onLineVideoMessage");
    // NOP
  }

  private void onLineStickerMessage(
      UseCase useCase, MessageEvent event, StickerMessageContent message) {
    LOG.info("onLineStickerMessage");
    // NOP
  }

  private void onLineFileMessage(UseCase useCase, MessageEvent event, FileMessageContent message) {
    LOG.info("onLineFileMessage");
    // NOP
  }

  private void onLineUnfollow(UseCase useCase, UnfollowEvent event) {
    LOG.info("onLineUnfollow");
    // NOP
  }

  private void onLineFollow(UseCase useCase, FollowEvent event) {
    LOG.info("onLineFollow");
    // NOP
  }

  private void onLineLeave(UseCase useCase, LeaveEvent event) {
    LOG.info("onLineLeave");
    // NOP
  }

  private void onLineJoin(UseCase useCase, JoinEvent event) {
    useCase.onLineJoin(event);
  }

  private void onLinePostback(UseCase useCase, PostbackEvent event) {
    Date date = getLinePostBackDateParam(event.getPostbackContent().getParams());
    if (date == null) {
      onLinePostBackNoParam(useCase, event);
    } else {
      onLinePostBackDateParam(useCase, event, date);
    }
  }

  private Date getLinePostBackDateParam(@Nullable Map<String, String> params) {
    if (params == null || params.isEmpty()) {
      return null;
    }
    try {
      if (params.containsKey("date")) {
        String source = params.get("date");
        return new LineDateFormat().parse(source);
      } else if (params.containsKey("time")) {
        String source = params.get("time");
        return new LineTimeFormat().parse(source);
      } else if (params.containsKey("datetime")) {
        String source = params.get("datetime");
        return new LineDatetimeFormat().parse(source);
      } else {
        LOG.log(Level.SEVERE, "parseLinePostbackEvent not match");
        return null;
      }
    } catch (ParseException e) {
      LOG.log(Level.SEVERE, "parseLinePostbackEvent", e);
      return null;
    }
  }

  private void onLinePostBackNoParam(UseCase useCase, PostbackEvent event) {
    LOG.info("onLinePostBackNoParam");
    // NOP
  }

  private void onLinePostBackDateParam(UseCase useCase, PostbackEvent event, Date date) {
    LOG.info("onLinePostBackDateParam");
    // NOP
  }

  private void onLineBeacon(UseCase useCase, BeaconEvent event) {
    LOG.info("onLineBeacon");
    // NOP
  }
}
