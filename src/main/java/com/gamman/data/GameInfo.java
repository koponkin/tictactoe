package com.gamman.data;

import java.util.UUID;

public class GameInfo {
  private Status status = Status.WAIT_SECOND_PLAYER;
  private Character[][] state = new Character[3][3];
  private String owner;
  private String player;
  private UUID id;
  private String prevPlayer;

  public GameInfo(String owner) {
    this.owner = owner;
    this.id = UUID.randomUUID();
  }

  public void setState(Character[][] state){
    this.state = state;
  }

  public Character[][] getState(){
    return state;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getOwner() {
    return owner;
  }

  public UUID getId() {
    return id;
  }

  public boolean isWaitPlayer() {
    return status == Status.WAIT_SECOND_PLAYER;
  }

  public boolean isInProgress() {
    return status == Status.IN_PROGRESS;
  }

  public String getPlayer() {
    return player;
  }

  public void setPlayer(String player) {
    this.player = player;
  }

  public void setPrevPlayer(String prevPlayer) {
    this.prevPlayer = prevPlayer;
  }

  public String getPrevPlayer() {
    return prevPlayer;
  }
}
