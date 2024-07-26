package com.demo.example.controller;

/*
This class controls the flow of the game and responds to communication from the front end.

Data: GameService gameService - connects to front end

Responsibilities:
    - Respond to front end with updated Game information:
        - when the application is started
        - when a new game is started
        - when a card is played

Relies upon:
    - Game: create a Game, update its fields based on information sent from the front end (info on the cards that
        were clicked), get its fields
 */

import com.demo.example.Card;
import com.demo.example.Game;
import com.demo.example.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /*
    Starts the new game and sets it up for play.
     */
    @GetMapping("/current-game")
    public String startGame(){

        Game current_game = gameService.getCurrentGame();
        System.out.println("Starting game");
        current_game.setGameStatus(Game.GameStatus.NOTSTARTED);
        current_game.setUpGame();
        System.out.println(current_game.getPlayerA().getHand().getHandAsStrings());
        System.out.println(current_game.getPlayerB().getHand().getHandAsStrings());
        return sendData(current_game);
    }

    /*
    Gets the value of the inputted card and either:
        - puts it into the Dealer's crib if the game is in the DEAL state
        - puts it into the arrayList of played cards and adds any points earned to the player
     */
    @PostMapping("/play-card")
    public String playCard(@RequestBody Map<String, Object> cardInfo) {
        Game current_game = gameService.getCurrentGame();

        int cardValue = (int) cardInfo.get("value");
        int cardSuit = (int) cardInfo.get("suit");
        String gamestatus = (String) cardInfo.get("gameStatus");

        current_game.setGameStatus(Game.GameStatus.fromString(gamestatus));

        Card sent_card = Card.of(cardValue, cardSuit);

        // Process the played card based on value and suit
        // - Update the game state

        // DEAL
        if (current_game.getGameStatus().equals(Game.GameStatus.DEAL)) {
            current_game.addCardToCribWithoutKnowingWhoPlayedIt(sent_card);
            // PLAY
        } else if (current_game.getGameStatus().equals(Game.GameStatus.PLAY)) {
            current_game.play(sent_card);
        }

        return sendData(current_game);
    }

    /*
    Updates the score on the frontend after PLAY is over.
     */
    @PostMapping("/show-score")
    public String showScore() {
        Game current_game = gameService.getCurrentGame();
        current_game.showScore();
        return sendData(current_game);
    }

    /*
    Changes the dealer, deals new cards, and updates the frontend.
     */
    @PostMapping("/start-new-round")
    public String startNewRound () {
        Game current_game = gameService.getCurrentGame();
        current_game.setGameStatus(Game.GameStatus.NOTSTARTED);

        // toggle dealer
        current_game.toggleDealer();

        // instantiate players' hands
        // Deal cards to players
        current_game.setUpDeal();
        current_game.setGameStatus(Game.GameStatus.DEAL);

        for (int i = 0; i < 6; i++) {
            current_game.dealCard(current_game.getPlayerA());
            current_game.dealCard(current_game.getPlayerB());
        }

        return sendData(current_game);
    }

    /*
    Starts a new game and updates the frontend.
     */
    @PostMapping("/start-new-game")
    public String startNewGame () {
        Game newGame = Game.of();  // Create a new game instance
        gameService.setCurrentGame(newGame);  // Set the new game as the current game
        newGame.setGameStatus(Game.GameStatus.NOTSTARTED);

        newGame.setUpGame();

        Game current_game = gameService.getCurrentGame();
        return sendData(current_game);
    }


    /*
    Sends all necessary data as a json to the frontend.
    Data includes:
        - Current points for each player
        - If either player has won
        - The runningSum, for PLAY
     */
    public String sendData(Game game){
        Map<String, Object> gameInfo = new HashMap<>();
        gameInfo.put("gameState", game.getGameStatus());
        if(game.getPlayerA().getHand() == null){
            gameInfo.put("player1Hand", game.getPlayerA().getHand());
        }
        else{
            gameInfo.put("player1Hand", game.getPlayerA().getHand().getHandAsIntArray());
        }

        if(game.getPlayerB().getHand() == null){
            gameInfo.put("player2Hand", game.getPlayerB().getHand());
        }
        else{
            gameInfo.put("player2Hand", game.getPlayerB().getHand().getHandAsIntArray());
        }
        gameInfo.put("player1Points", game.board.getPlayerAPoints());
        gameInfo.put("player2Points", game.board.getPlayerBPoints());
        gameInfo.put("Player1Won", game.board.didPlayerAWin());
        gameInfo.put("Player2Won", game.board.didPlayerBWin());
        gameInfo.put("Player1HasLegalCards", game.getPlayerA().hasLegalCard(game.getRunningSum()));
        gameInfo.put("Player2HasLegalCards", game.getPlayerB().hasLegalCard(game.getRunningSum()));
        gameInfo.put("RunningSum", game.getRunningSum());

        // Convert to JSON using Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(gameInfo);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error processing JSON";
        }
    }
}
