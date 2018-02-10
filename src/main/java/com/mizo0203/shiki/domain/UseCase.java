package com.mizo0203.shiki.domain;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.mizo0203.shiki.domain.model.ReversiModel;
import com.mizo0203.shiki.repo.ConfigRepository;
import com.mizo0203.shiki.repo.Repository;
import com.mizo0203.shiki.repo.ReversiRepository;
import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;
import java.util.List;
import java.util.logging.Logger;

public class UseCase implements Closeable {
  private static final Logger LOG = Logger.getLogger(UseCase.class.getName());
  private final Repository mRepository;
  private final Translator mTranslator;

  public UseCase() {
    mRepository = new Repository();
    mTranslator = new Translator();
  }

  @Override
  public void close() {
    mRepository.destroy();
  }

  public void onLineJoin(JoinEvent event) {
    LOG.info("replyToken: " + event.getReplyToken());
    try (ConfigRepository configRepository =
        new ConfigRepository(event.getSource().getSenderId())) {
      configRepository.deleteLineTalkRoomConfig();
    }
    try (ConfigRepository configRepository =
        new ConfigRepository(event.getSource().getSenderId())) {
      LineTalkRoomConfig config = configRepository.getLineTalkRoomConfig();

      try (ReversiRepository reversiRepository = new ReversiRepository(config)) {
        ReversiModel reversiModel = reversiRepository.getReversiModel();
        String messageText = mTranslator.parseLineMessageText(reversiModel);
        mRepository.replyMessage(event.getReplyToken(), new TextMessage(messageText));
      }
    }
  }

  public void onLineTextMessage(MessageEvent event, TextMessageContent message) {
    LOG.info("text: " + message.getText());
    try (ConfigRepository configRepository =
        new ConfigRepository(event.getSource().getSenderId())) {
      LineTalkRoomConfig config = configRepository.getLineTalkRoomConfig();
      try (ReversiRepository reversiRepository = new ReversiRepository(config)) {
        ReversiModel reversiModel = reversiRepository.getReversiModel();
        Message playLineMessage = playLineMessage(reversiModel, message.getText());
        if (reversiModel.getNextPieces() != null) {
          mRepository.replyMessage(event.getReplyToken(), playLineMessage);
        } else if (event.getSource() instanceof GroupSource) {
          mRepository.replyMessage(event.getReplyToken(), playLineMessage);
          mRepository.leaveGroup(event.getSource().getSenderId());
          configRepository.deleteLineTalkRoomConfig();
        } else {
          Message resetLineMessage = resetLineMessage(reversiModel);
          mRepository.replyMessage(event.getReplyToken(), playLineMessage, resetLineMessage);
        }
      }
    }
  }

  private Message playLineMessage(ReversiModel reversiModel, String message) {
    mTranslator.play(reversiModel, message);
    return new TextMessage(mTranslator.parseLineMessageText(reversiModel));
  }

  private Message resetLineMessage(ReversiModel reversiModel) {
    reversiModel.reset();
    return new TextMessage(mTranslator.parseLineMessageText(reversiModel));
  }

  @Nullable
  public List<Event> getCallbackEventList(HttpServletRequest req) {
    return mRepository.getCallbackEventList(req);
  }
}
