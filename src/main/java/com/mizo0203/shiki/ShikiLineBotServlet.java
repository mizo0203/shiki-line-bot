package com.mizo0203.shiki;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.mizo0203.shiki.domain.UseCase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.logging.Logger;

public class ShikiLineBotServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(ShikiLineBotServlet.class.getName());

  private UseCase mUseCase;

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
    mUseCase = new UseCase();
    try {
      mUseCase.parseWebhookEvent(
          req,
          this::onLineMessage,
          null,
          null,
          mUseCase::onLineJoin,
          null,
          this::onLinePostBack,
          null);
    } finally {
      // ボットアプリのサーバーに webhook から送信される HTTP POST リクエストには、ステータスコード 200 を返す必要があります。
      // https://developers.line.me/ja/docs/messaging-api/reference/#anchor-99cdae5b4b38ad4b86a137b508fd7b1b861e2366
      resp.setStatus(HttpServletResponse.SC_OK);
      mUseCase.close();
    }
  }

  private void onLineMessage(MessageEvent event) {
    mUseCase.parseMessageEvent(
        event.getMessage(),
        message -> mUseCase.onLineTextMessage(event, message),
        null,
        null,
        null,
        null,
        null,
        null);
  }

  private void onLinePostBack(PostbackEvent event) {
    mUseCase.parseLinePostbackEvent(
        event.getPostbackContent().getParams(),
        param -> onLinePostBackNoParam(event),
        param -> onLinePostBackDateParam(event, param));
  }

  private void onLinePostBackNoParam(PostbackEvent event) {}

  private void onLinePostBackDateParam(PostbackEvent event, Date date) {}
}
