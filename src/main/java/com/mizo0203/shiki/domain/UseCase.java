package com.mizo0203.shiki.domain;

import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.TextMessage;
import com.mizo0203.shiki.domain.model.Pieces;
import com.mizo0203.shiki.domain.model.ReversiModel;
import com.mizo0203.shiki.repo.*;
import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.io.Closeable;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        String messageText =
            mTranslator.parseLineMessageText(reversiModel, reversiRepository.getNextPieces());
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
        String messageText = play(reversiRepository, message.getText());
        mRepository.replyMessage(event.getReplyToken(), new TextMessage(messageText));
        if (reversiRepository.getNextPieces() == null) {
          if (event.getSource() instanceof GroupSource) {
            mRepository.leaveGroup(event.getSource().getSenderId());
            configRepository.deleteLineTalkRoomConfig();
          }
        }
      }
    }
  }

  private String play(ReversiRepository reversiRepository, String message) {
    ReversiModel reversiModel = reversiRepository.getReversiModel();
    Pieces oneself = reversiRepository.getNextPieces();
    if (oneself == null) {
      reversiRepository.reset();
      return mTranslator.parseLineMessageText(reversiModel, reversiRepository.getNextPieces());
    }
    boolean played = mTranslator.play(reversiModel, message, oneself);
    if (played) {
      Pieces nextPieces = reversiModel.nextPieces(oneself);
      reversiRepository.setNextPieces(nextPieces);
    }
    return mTranslator.parseLineMessageText(reversiModel, reversiRepository.getNextPieces());
  }

  public void parseWebhookEvent(
      HttpServletRequest req,
      @Null WebhookEvent<MessageEvent> onMessageEvent,
      @Null WebhookEvent<UnfollowEvent> onUnfollowEvent,
      @Null WebhookEvent<FollowEvent> onFollowEvent,
      @Null WebhookEvent<JoinEvent> onJoinEvent,
      @Null WebhookEvent<LeaveEvent> onLeaveEvent,
      @Null WebhookEvent<PostbackEvent> onPostbackEvent,
      @Null WebhookEvent<BeaconEvent> onBeaconEvent) {
    List<Event> eventList = getCallbackEventList(req);
    if (eventList == null) {
      return;
    }
    for (Event event : eventList) {
      if (event instanceof MessageEvent) {
        if (onMessageEvent != null) {
          onMessageEvent.callback((MessageEvent) event);
        }
      } else if (event instanceof UnfollowEvent) {
        if (onUnfollowEvent != null) {
          onUnfollowEvent.callback((UnfollowEvent) event);
        }
      } else if (event instanceof FollowEvent) {
        if (onFollowEvent != null) {
          onFollowEvent.callback((FollowEvent) event);
        }
      } else if (event instanceof JoinEvent) {
        if (onJoinEvent != null) {
          onJoinEvent.callback((JoinEvent) event);
        }
      } else if (event instanceof LeaveEvent) {
        if (onLeaveEvent != null) {
          onLeaveEvent.callback((LeaveEvent) event);
        }
      } else if (event instanceof PostbackEvent) {
        if (onPostbackEvent != null) {
          onPostbackEvent.callback((PostbackEvent) event);
        }
      } else if (event instanceof BeaconEvent) {
        if (onBeaconEvent != null) {
          onBeaconEvent.callback((BeaconEvent) event);
        }
      }
    }
  }

  @Null
  private List<Event> getCallbackEventList(HttpServletRequest req) {
    return mRepository.getCallbackEventList(req);
  }

  public void parseMessageEvent(
      MessageContent message,
      @Null WebhookMessageEvent<TextMessageContent> onTextMessageEvent,
      @Null WebhookMessageEvent<ImageMessageContent> onImageMessageEvent,
      @Null WebhookMessageEvent<LocationMessageContent> onLocationMessageEvent,
      @Null WebhookMessageEvent<AudioMessageContent> onAudioMessageEvent,
      @Null WebhookMessageEvent<VideoMessageContent> onVideoMessageEvent,
      @Null WebhookMessageEvent<StickerMessageContent> onStickerMessageEvent,
      @Null WebhookMessageEvent<FileMessageContent> onFileMessageEvent) {
    if (message instanceof TextMessageContent) {
      if (onTextMessageEvent != null) {
        onTextMessageEvent.callback((TextMessageContent) message);
      }
    } else if (message instanceof ImageMessageContent) {
      if (onImageMessageEvent != null) {
        onImageMessageEvent.callback((ImageMessageContent) message);
      }
    } else if (message instanceof LocationMessageContent) {
      if (onLocationMessageEvent != null) {
        onLocationMessageEvent.callback((LocationMessageContent) message);
      }
    } else if (message instanceof AudioMessageContent) {
      if (onAudioMessageEvent != null) {
        onAudioMessageEvent.callback((AudioMessageContent) message);
      }
    } else if (message instanceof VideoMessageContent) {
      if (onVideoMessageEvent != null) {
        onVideoMessageEvent.callback((VideoMessageContent) message);
      }
    } else if (message instanceof StickerMessageContent) {
      if (onStickerMessageEvent != null) {
        onStickerMessageEvent.callback((StickerMessageContent) message);
      }
    } else if (message instanceof FileMessageContent) {
      if (onFileMessageEvent != null) {
        onFileMessageEvent.callback((FileMessageContent) message);
      }
    }
  }

  public void parseLinePostbackEvent(
      @Null Map<String, String> params,
      @Null WebhookPostbackEvent<Void> onLinePostBackNone,
      @Null WebhookPostbackEvent<Date> onLinePostBackDate) {
    if (params == null || params.isEmpty()) {
      if (onLinePostBackNone != null) {
        onLinePostBackNone.callback(null);
      }
    } else if (params.containsKey("date")) {
      if (onLinePostBackDate != null) {
        onLinePostBackDate.callback(mTranslator.parseDate(params.get("date")));
      }
    } else if (params.containsKey("time")) {
      if (onLinePostBackDate != null) {
        onLinePostBackDate.callback(mTranslator.parseTime(params.get("time")));
      }
    } else if (params.containsKey("datetime")) {
      if (onLinePostBackDate != null) {
        onLinePostBackDate.callback(mTranslator.parseDatetime(params.get("datetime")));
      }
    }
  }
}
