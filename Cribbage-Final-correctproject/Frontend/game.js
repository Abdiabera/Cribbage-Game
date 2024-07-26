const card = [
  {id:[1,0], imagePath:'./images/ace_of_clubs.png'},
  {id:[2,0], imagePath:'./images/2_of_clubs.png'},
  {id:[3,0], imagePath:'./images/3_of_clubs.png'},
  {id:[4,0], imagePath:'./images/4_of_clubs.png'},
  {id:[5,0], imagePath:'./images/5_of_clubs.png'},
  {id:[6,0], imagePath:'./images/6_of_clubs.png'},
  {id:[7,0], imagePath:'./images/7_of_clubs.png'},
  {id:[8,0], imagePath:'./images/8_of_clubs.png'},
  {id:[9,0], imagePath:'./images/9_of_clubs.png'},
  {id:[10,0], imagePath:'./images/10_of_clubs.png'},
  {id:[11,0], imagePath:'./images/jack_of_clubs.png'},
  {id:[12,0], imagePath:'./images/queen_of_clubs.png'},
  {id:[13,0], imagePath:'./images/king_of_clubs.png'},

  {id:[1,1], imagePath:'./images/ace_of_hearts.png'},
  {id:[2,1], imagePath:'./images/2_of_hearts.png'},
  {id:[3,1], imagePath:'./images/3_of_hearts.png'},
  {id:[4,1], imagePath:'./images/4_of_hearts.png'},
  {id:[5,1], imagePath:'./images/5_of_hearts.png'},
  {id:[6,1], imagePath:'./images/6_of_hearts.png'},
  {id:[7,1], imagePath:'./images/7_of_hearts.png'},
  {id:[8,1], imagePath:'./images/8_of_hearts.png'},
  {id:[9,1], imagePath:'./images/9_of_hearts.png'},
  {id:[10,1], imagePath:'./images/10_of_hearts.png'},
  {id:[11,1], imagePath:'./images/jack_of_hearts.png'},
  {id:[12,1], imagePath:'./images/queen_of_hearts.png'},
  {id:[13,1], imagePath:'./images/king_of_hearts.png'},

  {id:[1,2], imagePath:'./images/ace_of_diamonds.png'},
  {id:[2,2], imagePath:'./images/2_of_diamonds.png'},
  {id:[3,2], imagePath:'./images/3_of_diamonds.png'},
  {id:[4,2], imagePath:'./images/4_of_diamonds.png'},
  {id:[5,2], imagePath:'./images/5_of_diamonds.png'},
  {id:[6,2], imagePath:'./images/6_of_diamonds.png'},
  {id:[7,2], imagePath:'./images/7_of_diamonds.png'},
  {id:[8,2], imagePath:'./images/8_of_diamonds.png'},
  {id:[9,2], imagePath:'./images/9_of_diamonds.png'},
  {id:[10,2], imagePath:'./images/10_of_diamonds.png'},
  {id:[11,2], imagePath:'./images/jack_of_diamonds.png'},
  {id:[12,2], imagePath:'./images/queen_of_diamonds.png'},
  {id:[13,2], imagePath:'./images/king_of_diamonds.png'},

  {id:[1,3], imagePath:'./images/ace_of_spades.png'},
  {id:[2,3], imagePath:'./images/2_of_spades.png'},
  {id:[3,3], imagePath:'./images/3_of_spades.png'},
  {id:[4,3], imagePath:'./images/4_of_spades.png'},
  {id:[5,3], imagePath:'./images/5_of_spades.png'},
  {id:[6,3], imagePath:'./images/6_of_spades.png'},
  {id:[7,3], imagePath:'./images/7_of_spades.png'},
  {id:[8,3], imagePath:'./images/8_of_spades.png'},
  {id:[9,3], imagePath:'./images/9_of_spades.png'},
  {id:[10,3], imagePath:'./images/10_of_spades.png'},
  {id:[11,3], imagePath:'./images/jack_of_spades.png'},
  {id:[12,3], imagePath:'./images/queen_of_spades.png'},
  {id:[13,3], imagePath:'./images/king_of_spades.png'}
]

const scoreElem1 = document.querySelector('.player1_points');
const scoreElem2 = document.querySelector('.player2_points');
const player1Container = document.querySelector('.player1_cards');
const player2Container = document.querySelector('.player2_cards');
const playContainer = document.querySelector('.play-cards');
const cribContainer = document.querySelector('.crib-cards');
var newround_flag=0;
var newgame_flag;
var Flag_legalcard=0;
var count = 0;
var firstroundcount;
const cardBackImgPath = './images/back.png'
var cardcounter;
const primaryColor = "white"

var savedPlayerName = localStorage.getItem('playerName');

// Make a GET request to the backend's current-game endpoint
fetch('http://localhost:8080/api/current-game')
    .then(response => response.json())
    .then(data => {
      // Log the received data here
      console.log(data);
      newround_flag = 1;
      newgame_flag = 0;
      firstroundcount = 0;
      const confirmation= window.confirm("Select two cards from each hand to add to the crib.");
      cardcounter = 0;
      updateGameState(data);
      Flag_legalcard = 0;
      // Use the received data in your frontend logic
    })
    .catch(error => {
      console.error('(GET)Error fetching data:', error);
      // Display an error message on the frontend in case of errors
    });

async function startNewGame() {
  try {
    const response = await fetch('http://localhost:8080/api/start-new-game', {
      method: 'POST',
      headers: {
        //'Content-Type': 'application/json',
        //Add any other headers if required
      },
      // If you need to send any data in the request body, include it here
      // body: JSON.stringify({ /* data */ }),
    })
        .then(response => response.json())
        .then(data => {
          // Log the received data here
          console.log(data);
          playContainer.innerHTML = '';
          cribContainer.innerHTML = '';
          newgame_flag = 1;
          Flag_legalcard=0
          updateGameState(data);
          // Use the received data in your frontend logic
        });
  } catch (error) {
    // Handle network errors or other exceptions
    console.error('Error starting a new game:', error);
    
  }

}
async function startNewRound() {
  if(gameState === "SHOW"){
    try {
      const response = await fetch('http://localhost:8080/api/start-new-round', {
        method: 'POST',
        headers: {
          //'Content-Type': 'application/json',
          //Add any other headers if required
        },
        // If you need to send any data in the request body, include it here
        // body: JSON.stringify({ /* data */ }),
      })
          .then(response => response.json())
          .then(data => {
            // Log the received data here
            console.log(data);
            playContainer.innerHTML = '';
            cribContainer.innerHTML = '';
            newround_flag = 1;
            Flag_legalcard=0
            updateGameState(data);
            // Use the received data in your frontend logic
          });
    } catch (error) {
      // Handle network errors or other exceptions
      console.error('Error starting a new game:', error);
    }
  }
}
async function showScore() {
  if(gameState === "SHOW"){
    try {
      const response = await fetch('http://localhost:8080/api/show-score', {
        method: 'POST',
        headers: {
          //'Content-Type': 'application/json',
          //Add any other headers if required
        },
        // If you need to send any data in the request body, include it here
        // body: JSON.stringify({ /* data */ }),
      })
          .then(response => response.json())
          .then(data => {
            // Log the received data here
            console.log(data);
            playContainer.innerHTML = '';
            cribContainer.innerHTML = '';
            Flag_legalcard=0
            updateGameState(data);
            // Use the received data in your frontend logic
          });
    } catch (error) {
      // Handle network errors or other exceptions
      console.error('Error starting a new game:', error);
    }
    const confirmation = window.confirm("Displaying new scores, adding points from the crib, and starting new round. Click OK to continue.");
    if(confirmation){
      playContainer.innerHTML = '';
      firstroundcount = 1;
      startNewRound();
    }
  }
}

// Function to update game state based on response
function updateGameState(data) {
  gameState = data.gameState;
  player1Hand = data.player1Hand;
  player2Hand = data.player2Hand;
  player1Points = data.player1Points;
  player2Points = data.player2Points;
  player1Won = data.Player1Won;
  player2Won = data.Player2Won;
  Player1HasLegalCards = data.Player1HasLegalCards;
  Player2HasLegalCards = data.Player2HasLegalCards;
  RunningSum = data.RunningSum;

  updateGamePage();
  // Update game.html or perform other actions based on the updated data
}

function updateGamePage(){
  //update their points on screen
  updateScore();
  //change players hands
  updateHands(player1Hand, player2Hand);

  if (gameState === "SHOW") {
    // Reveal cards in the crib
    //revealCribCards();
  }

  if(Player1HasLegalCards === true && Player2HasLegalCards === true && RunningSum === 0 && newround_flag === 0){
    const confirmation = window.confirm("Turn Completed, Refreshing Scores. Click OK to continue.");

    if(confirmation){
      playContainer.innerHTML = '';
      showScore();
    }
  }
  flag = 1;
}

function updateScore(){

  updateStatusElement(scoreElem1, "block", primaryColor, `Player 1 Points = <span class='badge'>${player1Points}</span>`)
  updateStatusElement(scoreElem2, "block", primaryColor, `Player 2 Points = <span class='badge'>${player2Points}</span>`)
}

function updateStatusElement(elem, display, color, innerHTML)
{
  elem.style.display = display

  if(arguments.length > 2)
  {
    elem.style.color = color
    elem.innerHTML = innerHTML
  }
}
// Reveal cards when game state is "Show"
function revealCribCards() {
  const cribCards = document.querySelectorAll('.in-crib-cards');
  cribCards.forEach(card => {
    card.style.display = 'block'; // Show the card
    cribCards.appendChild(card); // Append the card to the play area

  });
  const confirmation = window.confirm("Crib Revealed, moving on. Click OK to continue.");

  if (confirmation) {
    cribCards.innerHTML = '';
    playContainer.innerHTML = '';
  }
}

function sendToBackend(array){
  // Log the clicked card information
  cardcounter++;
  if (cardcounter == 4) {
    const confirmation = window.confirm("Now play cards! Click OK to continue.");
  }

  cardValue = array[0];
  cardSuit = array[1];
  console.log('BE Clicked Card:', cardValue, cardSuit);
  console.log('BE Game State: ', gameState);

  // Send the card information to the backend
  fetch('http://localhost:8080/api/play-card', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ value: cardValue, suit: cardSuit, gameStatus: gameState })
  })
      .then(response => {
        if (response.ok) {
          // If the card play was successful, perform another GET request or update game.html as needed
          return response.json();
        } else {
          throw new Error('Failed to play card');
        }
      })
      .then(data => {
        // Handle the response data from the backend
        console.log('Response from backend:', data);

        // Update game.html or perform actions based on the response data
        Flag_legalcard=0
        updateGameState(data);
      })
      .catch(error => {
        // Handle errors from the backend
        console.error('Error:', error);
      });
}
function updateHands(player1Cards, player2Cards) {
  // Select the player1 and player2 card containers in the HTML

  // Function to create a card element based on the card data
  function createCardElement(card, isPlayable = true) {
    const cardElement = document.createElement('div');
    cardElement.classList.add('player-card');
    
    if (isPlayable) {
      // Set the background image of the card element for playable cards
      cardElement.style.backgroundImage = `url(${card.imagePath})`;
    } else {
      // Set a different style for non-playable cards
      cardElement.textContent = `Card Not Playable`;
      //cardElement.style.backgroundImage = `url('./images/non_playable_card.png')`;
      //cardElement.classList.add('non-playable'); // Add a class for non-playable cards
    }
    return cardElement;
  }

  // Clear the previous content of player1 and player2 card containers
  player1Container.innerHTML = '';
  player2Container.innerHTML = '';

  if (Player1HasLegalCards === false && Player2HasLegalCards === false) {
    RunningSum = 0;
  }

  // Create and append card elements for a player
  function updatePlayerHand(playerContainer, cards) {
    cards.forEach(cardId => {
      const card1 = card.find(c => c.id[0] === cardId[0] && c.id[1] === cardId[1]);
      if (card1) {
        const isPlayable = card1.id[0] + RunningSum <= 31;
        const cardElement = createCardElement(card1, isPlayable);
        playerContainer.appendChild(cardElement);
        cardElement.addEventListener('click', () => handleCardClick(card1)); // Add click event listener

        if(!isPlayable){
          cardElement.addEventListener('click', () => {
            const confirmation = window.confirm("Card not playable. Click OK to continue.");
          });
        }
      }
    });
  }
  updatePlayerHand(player1Container, player1Cards);
  updatePlayerHand(player2Container, player2Cards);

  if (Player1HasLegalCards === false && Player2HasLegalCards === false) {
    const confirmation = window.confirm("No player has playable cards, moving on. Click OK to continue.");
    Flag_legalcard = 1;
    if(confirmation){
      playContainer.innerHTML = '';
      RunningSum = 0;
      showScore();
    }
  }
}

function handleCardClick(cardData) {
  const player1Hand = document.querySelector('.player1_cards'); // Player 1's hand container
  const player2Hand = document.querySelector('.player2_cards'); // Player 2's hand container

  function createCardElement(cardData) {
    const cardElement = document.createElement('div');
    cardElement.classList.add('player-card');
    cardElement.style.backgroundImage = `url(${cardData.imagePath})`; // Set card image
    return cardElement;
  }

  // Determine which player's hand was clicked
  const clickedCard = event.target;
  const playerHand = clickedCard.closest('.player-card').parentNode;

  const cardElement = createCardElement(cardData);

  if(firstroundcount === 0){
    console.log("if(newgame_flag === 0)");
    console.log(newgame_flag);
    console.log(newround_flag);
    console.log(cribContainer.innerHTML);
    // Check if already 4 cards in crib, if not, add to crib
    if (cribContainer.children.length <= 4) {
      // Add card to crib
      cardElement.classList.add('in-crib-cards');
      cribContainer.appendChild(cardElement);
      cardElement.style.display = 'none'; // Hide the selected card in crib
      console.log('FE Clicked Card:', cardData.id);
      console.log('FE Gamestate: ', gameState);
    }
    else if (playContainer.children.length <= 4) {
      // Check if already 4 cards in play-area, if not, add to play-area
      gameState = "PLAY";
      cardElement.classList.add('play-area-card');
      playContainer.appendChild(cardElement);
      console.log('FE Clicked Card:', cardData.id);
      console.log('FE Gamestate: ', gameState);
    }
  }
  else {
    console.log("else")
    console.log(newgame_flag);
    console.log(newround_flag);
    console.log(cribContainer.innerHTML);
    if (cribContainer.children.length <= 3) {
      // Add card to crib
      cardElement.classList.add('in-crib-cards');
      cribContainer.appendChild(cardElement);
      cardElement.style.display = 'none'; // Hide the selected card in crib
      console.log('FE Clicked Card:', cardData.id);
      console.log('FE Gamestate: ', gameState);
    }
    else if (playContainer.children.length <= 4) {
      // Check if already 4 cards in play-area, if not, add to play-area
      gameState = "PLAY";
      cardElement.classList.add('play-area-card');
      playContainer.appendChild(cardElement);
      console.log('FE Clicked Card:', cardData.id);
      console.log('FE Gamestate: ', gameState);
    }
  }

  if(player1Container.innerHTML.length === 0 && player2Container.innerHTML.length === 0){
    gameState = "SHOW";
    playContainer.innerHTML = '';
    const confirmation = window.confirm("All cards have been played. Click OK to continue.");
  }

  if(player1Won === true){
    const confirmation = window.confirm("Player 1 has Won!. Play 1 more game?");

    if(confirmation){
      startNewGame();
    }
  }
  if(player2Won === true){
    const confirmation = window.confirm("Player 2 has Won!. Play 1 more game?");

    if(confirmation){
      startNewGame();
    }
  }
  // Send clicked card to backend
  sendToBackend(cardData.id);
}