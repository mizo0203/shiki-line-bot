package com.mizo0203.shiki.repo;

import com.mizo0203.shiki.repo.objectify.entity.KeyEntity;
import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/* package */ class KeyRepository {

  private static final Logger LOG = Logger.getLogger(KeyRepository.class.getName());
  private final OfyRepository ofyRepository;
  private LineTalkRoomConfig config;

  /* package */ KeyRepository() {
    ofyRepository = new OfyRepository();
  }

  @Nonnull
  public String get(String key) {
    KeyEntity keyEntity = ofyRepository.loadKeyEntity(key);

    if (keyEntity == null) {
      keyEntity = new KeyEntity();
      keyEntity.key = key;
      keyEntity.value = "";
      ofyRepository.saveKeyEntity(keyEntity);
    }

    if (keyEntity.value.isEmpty()) {
      LOG.severe(key + " isEmpty");
    }

    return keyEntity.value;
  }
}
