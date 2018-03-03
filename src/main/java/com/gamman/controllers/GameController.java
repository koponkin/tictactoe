package com.gamman.controllers;

import com.gamman.data.Coordinate;
import com.gamman.data.GameInfo;
import com.gamman.data.Status;
import com.gamman.data.TableData;
import com.gamman.service.GameProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class GameController {

  private Map<UUID, GameProcessor> games = new HashMap<>();

  @GetMapping("/games")
  public List<TableData> getGames(Principal user) {
    List<TableData> result = new ArrayList<>();
    for (Map.Entry<UUID, GameProcessor> entry: games.entrySet()){
      String status;
      GameProcessor gameProcessor = entry.getValue();
      if (gameProcessor.isWaitPlayer()){
        boolean isCurrentUserGameOwner = user.getName().equals(gameProcessor.getOwner());
        status = isCurrentUserGameOwner ? "Waiting second player" : "Please join";
      }else if(gameProcessor.isInProgress()){
        status = "In progress";
      }else {
        status = "Finished";
      }
      result.add(new TableData(entry.getKey(), status,
          gameProcessor.getOwner(),
          gameProcessor.getPlayer()));
    }
    return result;
  }

  @GetMapping("/game/{gameId}")
  public GameInfo getInfo(@PathVariable UUID gameId, Principal user) {
    GameProcessor gameProcessor = games.get(gameId);
    return gameProcessor.getState(user.getName());
  }

  @GetMapping("/game/{gameId}/join")
  public GameInfo joinGame(@PathVariable UUID gameId, Principal user) {
    GameProcessor gameProcessor = games.get(gameId);
    gameProcessor.startGame(user.getName());
    return gameProcessor.getState(user.getName());
  }

  @GetMapping("/game/update/{gameId}")
  public GameInfo changeState(@PathVariable UUID gameId,
                              @RequestParam int x,
                              @RequestParam int y,
                              Principal principal
  ) {
    GameProcessor gameProcessor = games.get(gameId);
    gameProcessor.updateState(x, y, principal.getName());
    return gameProcessor.getState(principal.getName());
  }

  @GetMapping("/create")
  public GameInfo getInfo(Principal user) {
    GameProcessor gameProcessor = new GameProcessor(user.getName());
    games.put(gameProcessor.getId(), gameProcessor);
    return gameProcessor.getState(user.getName());
  }
}
