package com.mizo0203.shiki.repo;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Repository {

  private static final Logger LOG = Logger.getLogger(Repository.class.getName());
  private final LineRepository mLineRepository;

  public Repository() {
    KeyRepository keyRepository = new KeyRepository();
    String channelSecret = keyRepository.get("ChannelSecret");
    String channelAccessToken = keyRepository.get("ChannelAccessToken");
    mLineRepository = new LineRepository(channelSecret, channelAccessToken);
  }

  public void destroy() {
    mLineRepository.destroy();
  }

  /**
   * 応答メッセージを送る
   *
   * @param replyToken Webhook で受信する応答トークン
   * @param messages 送信するメッセージ (最大件数：5)
   */
  public void replyMessage(String replyToken, Message... messages) {
    mLineRepository.replyMessage(new ReplyMessage(replyToken, Arrays.asList(messages)));
  }

  /**
   * プッシュメッセージを送る
   *
   * @param to 送信先のID。Webhookイベントオブジェクトで返される、userId、groupId、またはroomIdの値を使用します。LINEアプリに表示されるLINE
   *     IDは使用しないでください。
   * @param messages 送信するメッセージ 最大件数：5
   */
  public void pushMessage(String to, Message... messages) {
    mLineRepository.pushMessage(new PushMessage(to, Arrays.asList(messages)));
  }

  /**
   * グループメンバーのユーザーIDを取得する
   *
   * @param groupId グループID。Webhookイベントオブジェクトのsourceオブジェクトで返されます。
   */
  @SuppressWarnings("unused")
  public void idsMembersGroup(String groupId) {
    mLineRepository.idsMembersGroup(groupId);
  }

  @Nullable
  public List<Event> getCallbackEventList(HttpServletRequest req) {
    return mLineRepository.getCallbackEventList(req);
  }

  public void leaveGroup(String groupId) {
    mLineRepository.leaveGroup(groupId);
  }
}
