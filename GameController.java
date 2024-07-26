package com.demo.example.controller;

import com.demo.example.Card;
import com.demo.example.Game;
import com.demo.example.GameService;
import com.demo.example.Hand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/current-game")
    public String startGame(){

        Game current_game = gameService.getCurrentGame();
        current_game.setGameStatus(Game.GameStatus.NOTSTARTED);

        // choose first dealer
        Random rand = new Random();
        boolean chooseFirstDealer = rand.nextBoolean();
        if (chooseFirstDealer) {
            current_game.setCurrentDealerA();
            current_game.setCurrentPlayerB();
        }
        else {
            current_game.setCurrentDealerB();
            current_game.setCurrentPlayerA();
        }

        // instantiate players' hands
        // Deal cards to players
        current_game.setUpDeal();
        current_game.setGameStatus(Game.GameStatus.DEAL);

        for(int i=0; i<6; i++) {
            current_game.dealCard(current_game.getPlayerA());
            current_game.dealCard(current_game.getPlayerB());
        }

        current_game.resetRunningSum();
        current_game.identifyLegalAndNotLegalCards(0);

        return sendData(current_game);
    }

    @PostMapping("/play-card")
    public String playCard(@RequestBody Map<String, Object> cardInfo) {
        Game current_game = gameService.getCurrentGame();

        int cardValue = (int) cardInfo.get("value");
        int cardSuit = (int) cardInfo.get("suit");
        String gamestatus = (String) cardInfo.get("gameStatus");

        current_game.setGameStatus(Game.GameStatus.fromString(gamestatus));

        Card sent_card = Card.of(cardValue, cardSuit);

        // Process the played card based on value and suit

        // - Update the game stat
        // DEAL
        if (current_game.getGameStatus().equals(Game.GameStatus.DEAL)) {
            if (current_game.getPlayerA().getHand().getCards().contains(sent_card)){
//                if (current_game.getPlayerA().getHand().getCards().size() <= 4) {
//                    System.out.println("can't do that");
//                } else {
                    current_game.getPlayerA().addCardToCrib(sent_card);
                    current_game.getPlayerA().getHand().removeCardFromHand(sent_card);
//                }
                //System.out.println(current_game.getPlayerA().getHand().getHandAsIntArray());
            } else {
//                if (current_game.getPlayerB().getHand().getCards().size() <= 4) {
//                    System.out.println("can't do that");
//                } else {
                    current_game.getPlayerB().addCardToCrib(sent_card);
                    current_game.getPlayerB().getHand().removeCardFromHand(sent_card);
//                }
            }

            return sendData(current_game);

        } else if (current_game.getGameStatus().equals(Game.GameStatus.PLAY)) {
            // PLAY
//
//            // if card is in non-player's hand, don't allow it to be played
//            if (!current_game.getCurrentPlayer().getHand().getCards().contains(sent_card)) {
//                return sendData(current_game);
//            }
//
//            // if card isn't legal to play, don't allow it to be played
//            if (!current_game.isLegalCard(sent_card, current_game.getRunningSum())) {
//                System.out.println("not legal card");
//                return sendData(current_game);
//            }

            if (!current_game.doesSomeoneHaveLegalCards(current_game.getPlayerA(),current_game.getPlayerB(),current_game.getRunningSum())) {
                current_game.resetRunningSum();
                current_game.identifyLegalAndNotLegalCards(0);
                if (current_game.getPlayerA().getHand().getCards().contains(sent_card)) {
                    current_game.addCardToPlayedCards(sent_card);
                    current_game.getBoard().addToPlayerAPoints(current_game.calculatePointsInArray(current_game.getPlayedCards()));
                    current_game.addToRunningSum(sent_card.getScoringValue());
                    current_game.getPlayerA().getHand().removeCardFromHand(sent_card);
                    current_game.addToPlayerAPlayed(sent_card);
                    current_game.identifyLegalAndNotLegalCards(current_game.getRunningSum());
                    current_game.chooseCurrentPlayer(current_game.isCurrentPlayerA(), current_game.getRunningSum());

                    if (current_game.handsEmpty()) {
                        current_game.setGameStatus(Game.GameStatus.SHOW);
                    }
                    return sendData(current_game);
                } else {
                    current_game.addCardToPlayedCards(sent_card);
                    current_game.getBoard().addToPlayerBPoints(current_game.calculatePointsInArray(current_game.getPlayedCards()));
                    current_game.addToRunningSum(sent_card.getScoringValue());
                    current_game.getPlayerB().getHand().removeCardFromHand(sent_card);
                    current_game.addToPlayerBPlayed(sent_card);
                    System.out.println("B played");
                    System.out.println(sent_card);
                    System.out.println(current_game.getPlayerBPlayed());
                    current_game.identifyLegalAndNotLegalCards(current_game.getRunningSum());
                    current_game.chooseCurrentPlayer(current_game.isCurrentPlayerA(), current_game.getRunningSum());
                    if (current_game.handsEmpty()) {
                        current_game.setGameStatus(Game.GameStatus.SHOW);
                    }
                    return sendData(current_game);
                }
            }

            if (current_game.getPlayerA().getHand().getCards().contains(sent_card)) {
                current_game.addCardToPlayedCards(sent_card);
                current_game.getBoard().addToPlayerAPoints(current_game.calculatePointsInArray(current_game.getPlayedCards()));
                current_game.addToRunningSum(sent_card.getScoringValue());
                current_game.getPlayerA().getHand().removeCardFromHand(sent_card);
                current_game.addToPlayerAPlayed(sent_card);
                current_game.identifyLegalAndNotLegalCards(current_game.getRunningSum());
                current_game.chooseCurrentPlayer(current_game.isCurrentPlayerA(), current_game.getRunningSum());

                if (current_game.handsEmpty()) {
                    current_game.setGameStatus(Game.GameStatus.SHOW);
                }
                return sendData(current_game);
            } else {
                current_game.addCardToPlayedCards(sent_card);
                current_game.getBoard().addToPlayerBPoints(current_game.calculatePointsInArray(current_game.getPlayedCards()));
                current_game.addToRunningSum(sent_card.getScoringValue());
                current_game.getPlayerB().getHand().removeCardFromHand(sent_card);
                current_game.addToPlayerBPlayed(sent_card);
                System.out.println("B played");
                System.out.println(sent_card);
                System.out.println(current_game.getPlayerBPlayed());
                current_game.identifyLegalAndNotLegalCards(current_game.getRunningSum());
                current_game.chooseCurrentPlayer(current_game.isCurrentPlayerA(), current_game.getRunningSum());
                if (current_game.handsEmpty()) {
                    current_game.setGameStatus(Game.GameStatus.SHOW);
                }
                return sendData(current_game);
            }
            }
            // check if someone has won
            if (current_game.getBoard().hasSomeoneWon()) {
                current_game.setGameStatus(Game.GameStatus.OVER);
                return sendData(current_game);
                // TODO: somehow print this out --> doing at frontend
            }
            /*
            if (current_game.getPlayerA().getHand().getCards().contains(sent_card)){
                current_game.addCardToPlayedCards(sent_card);
                current_game.getBoard().addToPlayerAPoints(current_game.calculatePointsInArray(current_game.getPlayedCards()));

                current_game.addToRunningSum(sent_card.getScoringValue());
                current_game.getPlayerA().getHand().removeCardFromHand(sent_card);

                return sendData(current_game);
            } else {
                current_game.addCardToPlayedCards(sent_card);
                current_game.getBoard().addToPlayerBPoints(current_game.calculatePointsInArray(current_game.getPlayedCards()));
                current_game.addToRunningSum(sent_card.getScoringValue());
                current_game.getPlayerB().getHand().removeCardFromHand(sent_card);
                return sendData(current_game);
            }
             */
        return sendData(current_game);
    }

    // - Determine the next steps in the game
    // - Calculate points, winner, etc.

    // After processing the card, prepare the updated game state
    // For example, assume newGameState is a Map representing the updated game stat
    @PostMapping("/show-score")
    public String showScore() {
        Game current_game = gameService.getCurrentGame();

        current_game.getPlayerA().createHand(current_game.getPlayerAPlayed());
        current_game.getPlayerB().createHand(current_game.getPlayerBPlayed());

        // current dealer counts second
        if (current_game.isCurrentDealerA()) {
            // count B's hand
            current_game.getBoard().addToPlayerBPoints(current_game.getPlayerB().getHand().calculatePoints(current_game.faceUpCard));
            // check if B won
            if (current_game.getBoard().didPlayerBWin()) {
                current_game.setGameStatus(Game.GameStatus.OVER);
                return sendData(current_game);
            }

            // count A's hand
            current_game.getBoard().addToPlayerAPoints(current_game.getPlayerA().getHand().calculatePoints(current_game.faceUpCard));

            // count A's crib
            current_game.getBoard().addToPlayerAPoints(current_game.getPlayerA().getCrib().calculatePoints(current_game.faceUpCard));

            // check if A won
            if (current_game.getBoard().didPlayerAWin()) {
                current_game.setGameStatus(Game.GameStatus.OVER);
                //break;
            }
        } else {
            // count A's hand
            current_game.getBoard().addToPlayerBPoints(current_game.getPlayerA().getHand().calculatePoints(current_game.faceUpCard));
            // check if A won
            if (current_game.getBoard().didPlayerAWin()) {
                current_game.setGameStatus(Game.GameStatus.OVER);
                return sendData(current_game);
            }

            // count B's hand
            current_game.getBoard().addToPlayerAPoints(current_game.getPlayerB().getHand().calculatePoints(current_game.faceUpCard));

            // count B's crib
            current_game.getBoard().addToPlayerAPoints(current_game.getPlayerB().getCrib().calculatePoints(current_game.faceUpCard));

            // check if B won
            if (current_game.getBoard().didPlayerBWin()) {
                current_game.setGameStatus(Game.GameStatus.OVER);
                return sendData(current_game);
            }
        }

        return sendData(current_game);

    }

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

    @PostMapping("/start-new-game")
    public String startNewGame () {
        Game newGame = Game.of();  // Create a new game instance
        gameService.setCurrentGame(newGame);  // Set the new game as the current game
        newGame.setGameStatus(Game.GameStatus.NOTSTARTED);

        // choose first dealer
        Random rand = new Random();
        boolean chooseFirstDealer = rand.nextBoolean();
        if (chooseFirstDealer) {
            newGame.setCurrentDealerA();
            newGame.setCurrentPlayerB();
        }
        else {
            newGame.setCurrentDealerB();
            newGame.setCurrentPlayerA();
        }

        // instantiate players' hands
        // Deal cards to players
        newGame.setUpDeal();
        newGame.setGameStatus(Game.GameStatus.DEAL);

        for (int i = 0; i < 6; i++) {
            newGame.dealCard(newGame.getPlayerA());
            newGame.dealCard(newGame.getPlayerB());
        }

        Game current_game = gameService.getCurrentGame();
        return sendData(current_game);
    }

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
        gameInfo.put("Player1HasLegalCards", game.hasLegalCard(game.getPlayerA(),game.getRunningSum()));
        gameInfo.put("Player2HasLegalCards", game.hasLegalCard(game.getPlayerB(),game.getRunningSum()));
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
