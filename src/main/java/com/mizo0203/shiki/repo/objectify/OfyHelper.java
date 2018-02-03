package com.mizo0203.shiki.repo.objectify;

import com.googlecode.objectify.ObjectifyService;
import com.mizo0203.shiki.repo.objectify.entity.KeyEntity;
import com.mizo0203.shiki.repo.objectify.entity.LineTalkRoomConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run. This is
 * required to let JSP's access OfyRepository.
 */
public class OfyHelper implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent event) {
    // This will be invoked as part of a warmup request, or the first user
    // request if no warmup
    // request.
    ObjectifyService.register(KeyEntity.class);
    ObjectifyService.register(LineTalkRoomConfig.class);
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    // App Engine does not currently invoke this method.
  }
}
