package com.demo.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

/*
This class represents a Hand object, analogous to a set of cards that make up a player's hand or crib in Cribbage.

Data:
    - ArrayList<Card> cards. Represents the Cards in the hand.

Responsibilities:
    - Create and represent a Hand object.
        - Factory method takes in an ArrayList of Cards (can be empty ArrayList).
    - (Calculate) and return values of a Hand, including:
        - Get its current cards
        - Return the values in the hand as an ArrayList int arrays of size 2, where the first item in the int array
            is the Card's value (as an int) and the second item is its suit (as an int). This is used for integration with the frontend.
        - Return the values in the hand as an ArrayList of Strings.
        - Remove all Cards from the Hand
        - Remove specific Cards from the Hand, by index.
        - Add cards to the hand
        - Calculate and return points earned by the hand, taking into account the faceUpCard. (Used during play)

Relies upon:
    - Game to provide the faceUpCard for score calculation
    - Card, to provide Card objects, toString method, getValue, getSuitAsInt

 */

public class Hand {
    private ArrayList<Card> cards; // Holds the Card objects. Can be empty.

    /*
    Constructor. Sets field cards to the inputted ArrayList of cards.
     */
    private Hand(ArrayList<Card> cardsToAdd) {
        this.cards = new ArrayList<>(); // Need to initialize the hand field first
        this.cards.addAll(cardsToAdd);
    }

    /*
    Factory method. Returns a new Hand object.
     */
    public static Hand of(ArrayList<Card> cardsToAdd) {
        return new Hand(cardsToAdd);
    }

    /*
    Returns all the cards in the hand.
     */
    public ArrayList<Card> getCards() {
        return this.cards;
    }

    /*
    Returns all the cards in the hand as an ArrayList of Strings.
     */
    public ArrayList<String> getHandAsStrings() {
        ArrayList<String> output = new ArrayList<>();
        for (Card card : cards) {
            output.add(card.toString());
        }
        return output;
    }

    /*
    Returns all cards in the hand as an ArrayList of integer arrays of size 2.

    Example:
        - Hand with an Ace of Diamonds and Queen of Spades returns ArrayList([1, 2], [10, 3])
    */
    public ArrayList<int[]> getHandAsIntArray() {
        ArrayList<int[]> toReturn = new ArrayList<>();

        for (Card card : cards) {
            int[] toAdd = new int[2];
            toAdd[0] = card.getValue();
            toAdd[1] = card.getSuitAsInt();
            toReturn.add(toAdd);
        }
        return toReturn;
    }


    /*
    Removes all Cards from the cards field.
    */
    public void removeAll() {
        this.cards.clear();
    }

    /*
    Removes a specific Card from the cards field.
    */
    public void removeCardFromHand(Card card) { // logic handled by player for which card
        cards.remove(card);
    }

    /*
    Adds a card to the cards field.
    */
    public void addCard(Card card) {
        cards.add(card);
    }

    /*
    Returns the score of the hand, based on fifteens, pairs/three of a kind/four of a kind, flushes, and runs.

    Score =
        2 points per fifteen (cards whose scoring value add to 15, e.g., (7, 8) or (King, Ace, 4).
        2 points per pair (cards whose values are identical. e.g., (2 of Diamonds, 2 of Spades).
            Note: three-of-a-kind is equivalent to 3 pairs and is scored consistently (6 points). Four-of-a-kind is 6 pairs, 12 points.
        1 point per card in a run (cards whose values are ordered. At least 3 card. e.g., (7, 8, 9).
        1 point per card in a flush (cards whose suits are identical. All cards in the hand must match suit for any flush points to be earned)
     */
    public int calculatePoints(Card faceUpCard){
        int fifteensPoints = this.calculateFifteensPoints(faceUpCard);
        int pairPoints = this.countPairsPoints(faceUpCard);
        int runPoints = this.countRunPoints(faceUpCard);
        int flushPoints = this.countFlushPoints(faceUpCard);

        return fifteensPoints + pairPoints + runPoints + flushPoints;
    }

    /*
    - Calculate the points earned by unique card combinations that sum to 15.
    - Uses a recursive helper method to evaluate the sum of every possible combination of cards in the set of cards passed to it.
    - Returns an int representing the points earned.
     */
    int calculateFifteensPoints(Card faceUpCard) {
        // Create copy of cards to look at and add in faceUpCard
        ArrayList<Card> cardsToConsider = new ArrayList<>(this.cards);
        cardsToConsider.add(faceUpCard);

        int expectedCombinations = (int) Math.pow(2, this.cards.size() + 1);
        int[] combinationCounter = new int[1]; // Array to hold the count of combinations explored, array to track across recursive calls
        // TODO: should this just be an integer value?
        // a primitive int value needs to be more carefully manipulated to hold its value across recursive calls

        int countFifteens = countFifteensRecursive(cardsToConsider, 0, 0, combinationCounter);

        if (combinationCounter[0] != expectedCombinations) {
            throw new IllegalStateException("Not all combinations were considered. Expected: "
                    + expectedCombinations + ", but got: " + combinationCounter[0]);
        }

        return countFifteens * 2;
    }
    /*
    Method: countFifteensRecursive
    Inputs: ArrayList<Card> cards - the set of cards for which we want to evaluate the number of unique combinations of 15
            int start - the index of the card being evaluated
            int currentSum - the running total of the cards included in the combination being evaluated
            int[] combinationCounter - counter variable to ensure all possible combinations have been evaluated
                                        int array to track across recursive calls
    Outputs: int the number of unique combinations that sum to 15
    Description: This method uses recursion to evaluate all possible subsets of cards. Each unique combination is the sum
    of independent decisions to include or exclude a particular card from the combination. For example, consider a
    hand of three cards. There are 7 possible subsets of 3 cards (excluding null), enumerated as a sequence of include/exclude decisions:
    [III], [IIE], [IEI], [EII], [IEE], [EIE], [EEI]. Therefore, we have a unique set of cards once an include/exclude decision
    has been made for each card. If the total of that unique combination sums to 15, we increment the count of unique
    combinations of 15.
     */
    private int countFifteensRecursive(ArrayList<Card> cards, int start, int currentSum, int[] combinationCounter) {
        int count = 0;

        if (start == cards.size()) {
            combinationCounter[0]++;
            return currentSum == 15 ? 1 : 0;
        }

        // Include the current card in the sum
        count += countFifteensRecursive(cards, start + 1, currentSum + cards.get(start).getScoringValue(), combinationCounter);

        // Skip the current card and move to the next
        count += countFifteensRecursive(cards, start + 1, currentSum, combinationCounter);

        return count;
    }

    /*
    - Calculate and return the points earned by unique pairs of cards (cards with the same value).
    - Returns an int representing the points earned.
     */
    int countPairsPoints(Card faceUpCard) {

        HashMap<Integer, Integer> valueCounts= new HashMap<>();

        // Increment the count for each card value in the hand
        for (Card card : this.cards) {
            if (!valueCounts.containsKey(card.getValue())) {
                valueCounts.put(card.getValue(), 1);
            } else {
                valueCounts.put(card.getValue(), valueCounts.get(card.getValue()) + 1);
            }
        }

        // add the face up card's value to the HashMap
        if (!valueCounts.containsKey(faceUpCard.getValue())){
            valueCounts.put(faceUpCard.getValue(), 1);
        } else {
            valueCounts.put(faceUpCard.getValue(), valueCounts.get(faceUpCard.getValue()) + 1);
        }

        for (Integer value : valueCounts.keySet()) {
            if (valueCounts.get(value) > 1) {
                if (valueCounts.get(value) == 2) return 2; // pair exists
                else if (valueCounts.get(value) == 3) return 6; // three of a kind exists
                else if (valueCounts.get(value) == 4) return 12; // four of a kind exists
                else throw new IllegalStateException("It's not possible to have more than four of a kind");
            }
        }

        return 0;
    }

    /*
    Calculate and return the points earned by making runs in a Hand.
    Returns an int representing points scored.
    */
    int countRunPoints(Card faceUpCard) {
        ArrayList<Card> allCards = new ArrayList<>(this.cards);
        allCards.add(faceUpCard);
        Collections.sort(allCards, Comparator.comparing(Card::getValue));

        int totalRunPoints = 0;
        ArrayList<Card> currentRun = new ArrayList<>();

        for (int i = 0; i < allCards.size(); i++) {
            // Check that cards are strictly increasing (duplicates OK)
            if (currentRun.isEmpty() ||
                    allCards.get(i).getValue() == currentRun.get(currentRun.size() - 1).getValue() + 1 ||
                    allCards.get(i).getValue() == currentRun.get(currentRun.size() - 1).getValue()) {
                currentRun.add(allCards.get(i));
            } else {
                totalRunPoints += countRunPointsHandleDuplicates(currentRun);
                currentRun.clear();
                currentRun.add(allCards.get(i));
            }
        }

        totalRunPoints += countRunPointsHandleDuplicates(currentRun);

        return totalRunPoints;
    }

    /*
    Helper method for counting points in a run. Removes duplicate cards, so run is accurately counted.

    Returns an int representing points earned by a run.
    */
    private int countRunPointsHandleDuplicates(ArrayList<Card> run) {
        HashSet<Integer> uniqueCardValues = new HashSet<>();
        int totalDuplicates = 0;

        for (Card card : run) {
            int runValue = card.getValue();
            if (!uniqueCardValues.contains(runValue)) {
                uniqueCardValues.add(runValue);
            } else {
                totalDuplicates++;
            }
        }

        if ((run.size() - totalDuplicates) < 3) {
            return 0; // Invalid run, fewer than 3 cards.
        }

        return (run.size() - totalDuplicates) * (totalDuplicates + 1);
    }

    /*
    Calculates and returns the points earned in a hand for flushes. All cards in a hand must be the same suit for a flush
        to be counted; if the face card matches suit, a bonus point is added. No partial-hand flushes are allowed.
     */
    int countFlushPoints(Card faceUpCard) {

        if (getCards().isEmpty()) {
            return 0;
        }

        int totalMatching = 0;

        Card.Suit flushSuit = this.cards.get(0).getSuit();

        for (Card card : this.cards) {
            if (card.getSuit() != flushSuit) {
                return 0;
            }
            else {
                totalMatching += 1;
            }
        }

        if (faceUpCard.getSuit() == flushSuit) {
            totalMatching += 1;
        }

        return totalMatching;
    }

    /*
    Returns true if two Hands are equal.

    A Hand is equal to other Hand if they both contain the exact same set of cards.

     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Hand) {
            Collections.sort(((Hand) other).getCards(), Comparator.comparing(Card::getValue));
            Collections.sort(this.getCards(), Comparator.comparing(Card::getValue));

            for (int i = 0; i < this.getCards().size(); i++) {
                if (!((Hand) other).getCards().get(i).equals(this.getCards().get(i))) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

}