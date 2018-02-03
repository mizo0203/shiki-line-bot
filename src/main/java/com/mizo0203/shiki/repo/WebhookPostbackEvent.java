package com.mizo0203.shiki.repo;

public interface WebhookPostbackEvent<T> {
  void callback(T param);
}
