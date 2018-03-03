package com.gamman.data;

import java.util.UUID;

public class TableData {
  private UUID id;
  private String status;
  private String user1;
  private String user2;

  public TableData(UUID id, String status, String user1, String user2) {
    this.id = id;
    this.status = status;
    this.user1 = user1;
    this.user2 = user2;
  }

  public String getStatus() {
    return status;
  }

  public UUID getId() {
    return id;
  }

  public String getUser1() {
    return user1;
  }

  public String getUser2() {
    return user2;
  }
}
