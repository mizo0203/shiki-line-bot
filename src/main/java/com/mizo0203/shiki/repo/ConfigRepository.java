package com.mizo0203.shiki.repo;

import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.annotation.Nonnull;
import java.io.Closeable;

public class ConfigRepository implements Closeable {

  private final OfyRepository ofyRepository;
  private LineTalkRoomConfig config;

  public ConfigRepository(String senderId) {
    ofyRepository = new OfyRepository();
    config = ofyRepository.loadOrCreateLineTalkRoomConfig(senderId);
  }

  @Override
  public void close() {
    if (config != null) {
      ofyRepository.saveLineTalkRoomConfig(config);
    }
  }

  public void deleteLineTalkRoomConfig() {
    if (config != null) {
      ofyRepository.deleteLineTalkRoomConfig(config.getSenderId());
      config = null;
    }
  }

  @Nonnull
  public LineTalkRoomConfig getLineTalkRoomConfig() {
    if (config == null) {
      throw new IllegalStateException("LineTalkRoomConfig is Deleted");
    }
    return config;
  }
}
