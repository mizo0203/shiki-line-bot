package com.mizo0203.shiki.repo;

import com.linecorp.bot.model.event.Event;

public interface WebhookEvent<T extends Event> {
  void callback(T event);
}
