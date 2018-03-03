package com.gamman.service;

import com.gamman.data.GameInfo;
import com.gamman.data.Status;

import java.util.UUID;

public class GameProcessor {
  public GameProcessor(String owner) {
    this.gameInfo = new GameInfo(owner);
  }

  private GameInfo gameInfo;
  private int[][][] winVariants = {
      {{0,0},{0,1},{0,2}}, //column 1
      {{1,0},{1,1},{1,2}}, //column 2
      {{2,0},{2,1},{2,2}}, //column 3
      {{0,0},{1,0},{2,0}}, //row 1
      {{0,1},{1,1},{2,1}}, //row 2
      {{0,2},{1,2},{2,2}}, //row 3
      {{0,0},{1,1},{2,2}}, //top left - bottom right
      {{0,2},{1,1},{2,0}}, //bottom left - top right
  };

  public void updateState(int x, int y, String userName){
    if(this.gameInfo.getState()[x][y] != null){
      return;
    }
    char userChar = userName.equals(gameInfo.getOwner()) ? 'X' : 'O';
    this.gameInfo.setPrevPlayer(userName);
    this.gameInfo.getState()[x][y] = userChar;
  }

  private void verifyStatus(String userName) {
    if(gameInfo.getPlayer() == null){
      return;
    }

    Character winCharacter = null;
    for (int[][] winVariant : winVariants) {
      Character[] lineDataArray = getLineDataArray(winVariant);
      boolean isWinLine = checkWinLines(lineDataArray);
      if (isWinLine) {
        winCharacter = lineDataArray[0];
        break;
      }
    }

    if(winCharacter == null){
      gameInfo.setStatus(Status.IN_PROGRESS);
      return;
    }

    Character userSign = userName.equals(gameInfo.getOwner()) ? 'X' : 'O';

    if(winCharacter == userSign){
      gameInfo.setStatus(Status.WIN);
    }else {
      gameInfo.setStatus(Status.LOST);
    }
  }

  public GameInfo getState(String userName){
    verifyStatus(userName);
    return gameInfo;
  }

  private boolean checkWinLines(Character[] lineDataArray) {
    return lineDataArray[0] != null
        && lineDataArray[0] == lineDataArray[1]
        && lineDataArray[0] == lineDataArray[2];
  }

  private Character[] getLineDataArray(int[][] winVariant) {
    Character[] rowData = new Character[3];
    for(int cellIndex=0; cellIndex<3; cellIndex++){
      int[] cellCoordinate = winVariant[cellIndex];
      Character[][] state = gameInfo.getState();
      rowData[cellIndex]  = state[cellCoordinate[0]][cellCoordinate[1]];
    }
    return rowData;
  }

  public UUID getId() {
    return gameInfo.getId();
  }

  public boolean isWaitPlayer() {
    return gameInfo.isWaitPlayer();
  }

  public boolean isInProgress() {
    return gameInfo.isInProgress();
  }

  public String getOwner() {
    return gameInfo.getOwner();
  }

  public String getPlayer() {
    return gameInfo.getPlayer();
  }

  public void startGame(String name) {
    gameInfo.setPlayer(name);
    gameInfo.setStatus(Status.IN_PROGRESS);
  }
}
