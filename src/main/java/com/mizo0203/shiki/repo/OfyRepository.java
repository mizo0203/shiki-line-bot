package com.mizo0203.shiki.repo;

import com.googlecode.objectify.ObjectifyService;
import com.mizo0203.shiki.repo.objectify.entity.KeyEntity;
import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.annotation.Nonnull;

/* package */ class OfyRepository {

  @SuppressWarnings("EmptyMethod")
  public void destroy() {
    // NOP
  }

  public void deleteLineTalkRoomConfig(String key) {
    ObjectifyService.ofy().delete().type(LineTalkRoomConfig.class).id(key).now();
  }

  @Nonnull
  public LineTalkRoomConfig loadOrCreateLineTalkRoomConfig(String sourceId) {
    LineTalkRoomConfig config =
        ObjectifyService.ofy().load().type(LineTalkRoomConfig.class).id(sourceId).now();
    if (config == null) {
      config = new LineTalkRoomConfig(sourceId);
    }
    return config;
  }

  public void saveLineTalkRoomConfig(LineTalkRoomConfig entity) {
    ObjectifyService.ofy().save().entity(entity).now();
  }

  public KeyEntity loadKeyEntity(String key) {
    return ObjectifyService.ofy().load().type(KeyEntity.class).id(key).now();
  }

  public void saveKeyEntity(KeyEntity entity) {
    ObjectifyService.ofy().save().entity(entity).now();
  }
}
