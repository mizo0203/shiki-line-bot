package com.mizo0203.shiki.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.servlet.LineBotCallbackException;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.mizo0203.shiki.util.HttpUtil;
import com.mizo0203.shiki.util.PaserUtil;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 注意: com.linecorp.bot:line-bot-api-client は Java App Engine アプリケーションで使用できない */
/* package */ class LineRepository {

  private static final Logger LOG = Logger.getLogger(LineRepository.class.getName());

  private static final String MESSAGING_API_REPLY_MESSAGE_URL_STR =
      "https://api.line.me/v2/bot/message/reply";
  private static final String MESSAGING_API_PUSH_MESSAGE_URL_STR =
      "https://api.line.me/v2/bot/message/push";
  private static final String MESSAGING_API_IDS_MEMBERS_GROUP_URL_STR =
      "https://api.line.me/v2/bot/group/%s/members/ids";
  private static final String MESSAGING_API_LEAVE_GROUP_URL_STR =
      "https://api.line.me/v2/bot/group/%s/leave";
  private final LineBotCallbackRequestParser mLineBotCallbackRequestParser;
  private final String mChannelAccessToken;

  LineRepository(String channelSecret, String channelAccessToken) {
    mLineBotCallbackRequestParser =
        new LineBotCallbackRequestParser(new LineSignatureValidator(channelSecret.getBytes()));
    mChannelAccessToken = channelAccessToken;
  }

  @SuppressWarnings("EmptyMethod")
  public void destroy() {
    // NOP
  }

  /**
   * 応答メッセージを送る
   *
   * <p>ユーザー、グループ、またはトークルームからのイベントに対して応答メッセージを送信するAPIです。
   *
   * <p>イベントが発生するとwebhookを使って通知されます。応答できるイベントには応答トークンが発行されます。
   *
   * <p>応答トークンは一定の期間が経過すると無効になるため、メッセージを受信したらすぐに応答を返す必要があります。応答トークンは1回のみ使用できます。
   *
   * <p>https://developers.line.me/ja/docs/messaging-api/reference/#anchor-36ddabf319927434df30f0a74e21ad2cc69f0013
   *
   * @param replyMessage ReplyMessage
   */
  public void replyMessage(ReplyMessage replyMessage) {
    try {
      final URL url = new URL(MESSAGING_API_REPLY_MESSAGE_URL_STR);
      final Map<String, String> reqProp = new HashMap<>();
      reqProp.put("Content-Type", "application/json");
      reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
      final String body = PaserUtil.toJson(replyMessage);
      HttpUtil.post(url, reqProp, body, null);
    } catch (JsonProcessingException | MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * プッシュメッセージを送る
   *
   * <p>注：プッシュメッセージは一部のプランでのみご利用いただけます。詳しくは、LINE@サイトを参照してください。
   *
   * <p>ユーザー、グループ、またはトークルームに、任意のタイミングでプッシュメッセージを送信するAPIです。
   *
   * <p>https://developers.line.me/ja/docs/messaging-api/reference/#anchor-0c00cb0f42b970892f7c3382f92620dca5a110fc
   *
   * @param pushMessage PushMessage
   */
  public void pushMessage(PushMessage pushMessage) {
    try {
      final URL url = new URL(MESSAGING_API_PUSH_MESSAGE_URL_STR);
      final Map<String, String> reqProp = new HashMap<>();
      reqProp.put("Content-Type", "application/json");
      reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
      final String body = PaserUtil.toJson(pushMessage);
      HttpUtil.post(url, reqProp, body, null);
    } catch (MalformedURLException | JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * グループメンバーのユーザーIDを取得する
   *
   * <p>注：この機能は認証済みLINE@アカウントまたは公式アカウントのみでご利用いただけます。詳しくは、LINE@サイトの「LINE@アカウントを作成しましょう」ページまたはLINE
   * Partnerサイトを参照してください。
   *
   * <p>ボットが参加しているグループのメンバーの、ユーザーIDを取得するAPIです。ボットを友だちとして追加していないユーザーや、ボットをブロックしているユーザーのユーザーIDも取得します。
   *
   * <p>https://developers.line.me/ja/docs/messaging-api/reference/#anchor-b3c29117f4c090d4c3aabc67516a0092e9e9a3b8
   *
   * @param groupId グループID。Webhookイベントオブジェクトのsourceオブジェクトで返されます。
   */
  public void idsMembersGroup(final String groupId) {
    try {
      final URL url = new URL(String.format(MESSAGING_API_IDS_MEMBERS_GROUP_URL_STR, groupId));
      final Map<String, String> reqProp = new HashMap<>();
      reqProp.put("Content-Type", "application/json");
      reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
      HttpUtil.get(url, reqProp, null);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public void leaveGroup(String groupId) {
    try {
      final URL url = new URL(String.format(MESSAGING_API_LEAVE_GROUP_URL_STR, groupId));
      final Map<String, String> reqProp = new HashMap<>();
      reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
      HttpUtil.post(url, reqProp, "", null);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Nullable
  public List<Event> getCallbackEventList(HttpServletRequest req) {
    try {
      return mLineBotCallbackRequestParser.handle(req).getEvents();
    } catch (LineBotCallbackException | IOException e) {
      LOG.log(Level.SEVERE, "", e);
      return null;
    }
  }
}
