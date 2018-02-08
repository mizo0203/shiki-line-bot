package com.mizo0203.shiki;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.mizo0203.shiki.domain.UseCase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
      useCase.parseWebhookEvent(
          req,
          event -> onLineMessage(useCase, event),
          null,
          null,
          event -> onLineJoin(useCase, event),
          null,
          event -> onLinePostBack(useCase, event),
          null);
    } finally {
      // ボットアプリのサーバーに webhook から送信される HTTP POST リクエストには、ステータスコード 200 を返す必要があります。
      // https://developers.line.me/ja/docs/messaging-api/reference/#anchor-99cdae5b4b38ad4b86a137b508fd7b1b861e2366
      resp.setStatus(HttpServletResponse.SC_OK);
    }
  }

  private void onLineMessage(UseCase useCase, MessageEvent event) {
    useCase.parseMessageEvent(
        event.getMessage(),
        message -> onLineTextMessage(useCase, event, message),
        null,
        null,
        null,
        null,
        null,
        null);
  }

  private void onLineTextMessage(UseCase useCase, MessageEvent event, TextMessageContent message) {
    useCase.onLineTextMessage(event, message);
  }

  private void onLineJoin(UseCase useCase, JoinEvent event) {
    useCase.onLineJoin(event);
  }

  private void onLinePostBack(UseCase useCase, PostbackEvent event) {
    useCase.parseLinePostbackEvent(
        event.getPostbackContent().getParams(),
        param -> onLinePostBackNoParam(useCase, event),
        param -> onLinePostBackDateParam(useCase, event, param));
  }

  private void onLinePostBackNoParam(UseCase useCase, PostbackEvent event) {}

  private void onLinePostBackDateParam(UseCase useCase, PostbackEvent event, Date date) {}
}
