package com.demo.example;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private Game currentGame;

    public GameService() {
        this.currentGame = Game.of();
        //this.currentGame.runGame();
    }

    public Game getCurrentGame() {
        return this.currentGame;
    }

    public void setCurrentGame(Game game){
        this.currentGame = game;
    }

    // Other methods to manipulate the game state as needed
}
