package com.mizo0203.shiki.repo.objectify.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.mizo0203.shiki.repo.objectify.OfyHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Min;

/**
 * The @Entity tells Objectify about our entity. We also register it in {@link OfyHelper} Our
 * primary key @Id is set automatically by the Google Datastore for us.
 *
 * <p>We add a @Parent to tell the object about its ancestor. We are doing this to support many
 * guestbooks. Objectify, unlike the AppEngine library requires that you specify the fields you want
 * to index using @Index. Only indexing the fields you need can lead to substantial gains in
 * performance -- though if not indexing your data from the start will require indexing it later.
 *
 * <p>NOTE - all the properties are PUBLIC so that can keep the code simple.
 */
@Entity
public class LineTalkRoomConfig {

  @Id private String senderId;
  private String reversiBoard;
  private int pieces;

  public LineTalkRoomConfig() {
    // LineTalkRoomConfig must have a no-arg constructor
    reversiBoard = null;
  }

  /** A convenience constructor */
  public LineTalkRoomConfig(@Nonnull String senderId) {
    this();
    this.senderId = senderId;
  }

  public String getSenderId() {
    return senderId;
  }

  @Nullable
  public String getReversiBoard() {
    return reversiBoard;
  }

  public void setReversiBoard(String reversiBoard) {
    this.reversiBoard = reversiBoard;
  }

  @Min(-1)
  public int getPieces() {
    return pieces;
  }

  public void setPieces(int pieces) {
    this.pieces = pieces;
  }
}
