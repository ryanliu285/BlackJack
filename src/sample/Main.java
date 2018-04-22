package sample;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Integer.parseInt;
import static javafx.scene.paint.Color.*;


public class Main extends Application{
    private int totalNumber, player1Total,cardsDrawn; //total number value from cards drawn, Total value player 1 has drawn, number of cards that have been drawn
    private Card[] mainDeck; //deck containing all cards
    private Button hit, stand, split; //Buttons to hit, stand, split
    private Stage stage; //the stage or window
    private HBox cardRow,controlRow, bust,loss,win,noWin,splitBox1,splitBox2, controlRowSplit1,controlRowSplit2; //Horizontal layouts containing the cards, controls, bust display, loss display, win display, same score for both players display, display for a split, display for split controls
    private VBox game; //the vertical layout that contains all horizontal layouts
    private Text[] texts; //An array of premade texts that makes it easier to display which card is on the board
    private Text cardValues,handsWonText, moneyText,betText; //cardValuesText is the text that displays total number added up from the cards on the board, handsWonText is the text that displays the hands won, moneyText displays current money
    private int handsWon, money=1000; //number of hands won, money for betting
    private boolean dealer,busted; //dealer is false when the dealer is not playing, true when the dealer is playing. busted is true when someone has busted, false otherwise
    private TextField betNumber;//place where you enter amount you want to bet
    private int betAmount; //amount bet
    private Button hitSplit1, standSplit1,hitSplit2, standSplit2; //buttons after split

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage; //assigns primaryStage to stage so it can be accessed outside of start
        primaryStage.setTitle("BlackJack"); //sets title of window

        cardRow = new HBox(20); //Rows for content like cards/buttons
        cardRow.setMinHeight(500); //setting height for the row containing cards

        controlRow = new HBox(40); //Rows for content like cards/buttons
        controlRow.setMinHeight(100);//setting height for the row containing controls

        game = new VBox(10); //Vertical layout to store the rows inside

        texts = new Text[52]; //creates the array

        cardsDrawn = 2; //sets cards drawn to 2 for starting hand
        generateDeck(); //generates a new deck
        int i =0; //used to cycle through the while loop
        while(i<cardsDrawn) { //Texts are created from the name of the face card or the number of the card. The while loop stores the first i amount of cards texts inside the array texts.
            if(mainDeck[i].getFace()!=null){
                texts[i] = new Text(0, 0, mainDeck[i].getFace() + "\n" + mainDeck[i].getSuite());
            }else {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
            }
            texts[i].setFill(DODGERBLUE); //makes text colorful
            cardRow.getChildren().addAll(mainDeck[i].getGraphic(),texts[i]);
            i++;
        }

        hit = new Button("hit"); //button to hit
        stand = new Button("stand");//button to stand
        split = new Button("split");//button to split
        calculateTotal(); //calculates total for cardValue text
        cardValues = new Text("Total Value of Cards: " + totalNumber); //text that displays card value
        handsWonText = new Text("Hands Won: " + handsWon); //text that displays hands won
        moneyText = new Text("Money: $"+money); //text that displays money
        betText = new Text("Bet: "); //text that says bet
        betNumber = new TextField();
        betNumber.setPromptText("Please enter a number");

        hit.setOnAction(e->{ if((((betAmount=parseInt(betNumber.getText().toString()))<=money))&&(money>=betAmount)){ //checks if bet amount is correct
            betAmount=parseInt(betNumber.getText().toString());
            hit();}}); //when the button is clicked, hit() is fired
        stand.setOnAction(e->  {if((((betAmount=parseInt(betNumber.getText().toString()))<=money))&&(money>=betAmount)){ //checks if bet amount is correct
            betAmount=parseInt(betNumber.getText().toString());
            stand();}}); //when the button is clicked, stand() is fired
        split.setOnAction(e-> {if((((betAmount=parseInt(betNumber.getText().toString()))<=money))&&(money>=betAmount)){ //check if bet amount is correct
            betAmount=parseInt(betNumber.getText().toString());
            split();}});//when the button is clicked, split() is fired

        controlRow.getChildren().addAll(hit,stand,split,cardValues,handsWonText,moneyText,betText,betNumber); //adds all the buttons and text to the row for controls

        if(calculateTotal()==21){ //if the card total is 21, the user wins and it is displayed on the screen
            handsWon++;
            controlRow.getChildren().removeAll(hit,stand,split,cardValues,handsWonText);
            Text blackJackText = new Text("BLACK JACK! YOU WIN! YOU AUTOMATICALLY WIN $1000");
            money+=1000;
            Button blackJackButton = new Button("New Hand");
            blackJackButton.setOnAction(e -> newHand());
            handsWonText = new Text("Hands Won: " + handsWon); //text that displays hands won
            controlRow.getChildren().addAll(blackJackText,blackJackButton,handsWonText);
        }


        game.getChildren().addAll(cardRow,controlRow); //adding the rows to the Vertical layout

        primaryStage.setScene(new Scene(game, 1920, 600)); //sets Vertical layout as main layout
        primaryStage.show(); //shows the window
    }


    public int calculateTotal(){ //calculates the total value of cards drawn
        totalNumber = 0;
        for(int i = 0;i<cardsDrawn;i++){
            totalNumber += mainDeck[i].getNumber();
        }
        return totalNumber;
    }
    public Card draw(){ //draws a card
        cardsDrawn++;
        return mainDeck[cardsDrawn];
    }

    public void generateDeck(){ //generates a deck. could also be called shuffle
        Card[] deck = new Card[52]; //creates deck to store the 52 randomized cards
        Card[] available = new Card[52]; //creates deck to keep track of the cards not yet chosen
        int tempNumber; //Random number between 0 and the number of cards left in available
        int a =0;
            for(int x=0;x<13;x++) { //adds cards to the available deck in non randomized order
                for (int j = 0; j < 4; j++) {
                    for(int i = a; i <52; i++){
                        available[i] = new Card(x+1, j+1);
                    }
                    a++;
                }
            }
        int cardsLeft = 52; //amount of cards left
        for(int i = 0; i <52; i++){ //takes a random card, then switches the used value to the end of the available array, changes the scope of the temporary number, then continues until the deck is finished
            tempNumber=(int)(Math.random()*(cardsLeft));
            deck[i] = available[tempNumber];
            switchArray(tempNumber,cardsLeft-1,available);
            cardsLeft--;
        }
        mainDeck = deck; //assigns the deck to mainDeck
    }

    public void switchArray(int pos1,int pos2,Card[] array){ //switches the value of pos 2 to pos1, then makes the value in pos2 null, as we do not need it anymore
        array[pos1] = array[pos2];
        array[pos2] = null;
    }


    private void split() {//checks if the numbers are the same, then splits the deck if they are the same
        if(cardsDrawn==2&&(mainDeck[0].getNumber()==mainDeck[1].getNumber())){
            cardsDrawn/=2;
            stage.hide();
            game.getChildren().removeAll(cardRow,controlRow);
            splitBox1 = new HBox(20);
            splitBox2 = new HBox(20);
            controlRowSplit1 = new HBox(20);
            controlRowSplit2 = new HBox(20);
            hitSplit1 = new Button("hit"); //button to hit
            standSplit1 = new Button("stand");//button to stand
            hitSplit2 = new Button("hit"); //button to hit
            standSplit2 = new Button("stand");//button to stand
            hitSplit1.setOnAction(e-> hitSplit()); //when the button is clicked, hit() is fired
            standSplit1.setOnAction(e-> standSplit()); //when the button is clicked, stand() is fired
            hitSplit2.setOnAction(e-> hitSplit()); //when the button is clicked, hit() is fired
            standSplit2.setOnAction(e-> standSplit()); //when the button is clicked, stand() is fired
            controlRowSplit1.getChildren().addAll(hitSplit1,standSplit1);
            controlRowSplit2.getChildren().addAll(hitSplit2,standSplit2);
            cardRow.getChildren().removeAll(mainDeck[1].getGraphic(),texts[1]);
            splitBox1.getChildren().addAll(mainDeck[1].getGraphic(),texts[1]);
            splitBox2.getChildren().addAll(mainDeck[0].getGraphic(),texts[0]);
            game.getChildren().addAll(splitBox1,controlRowSplit1,splitBox2,controlRowSplit2);
        }
    }

    private void standSplit() {
        stage.hide();
        dealer = true;
        player1Total = totalNumber;
        totalNumber = 0;
        game.getChildren().removeAll(splitBox2,splitBox1);
        cardRow = new HBox(20); //Rows for content like cards/buttons
        cardRow.setMinHeight(500); //setting height for the row containing cards

        game = new VBox(10); //Vertical layout to store the rows inside

        texts = new Text[52];

        cardsDrawn = 2; //sets cards drawn to 2 for starting hand
        generateDeck(); //generates a new deck
        int i =0;
        while(i<cardsDrawn) { //does the same thing that is in start(). It draws the cards onto the table, this time for the dealer
            if(mainDeck[i].getFace()!=null){
                texts[i] = new Text(0, 0, mainDeck[i].getFace() + "\n" + mainDeck[i].getSuite());
            }else {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
            }
            texts[i].setFill(DODGERBLUE);
            cardRow.getChildren().addAll(mainDeck[i].getGraphic(),texts[i]);
            i++;
        }
        game.getChildren().addAll(cardRow);//adds cards to the layout
        stage.setScene(new Scene(game,1920,600)); //creates window
        stage.show();//shows window
        while(calculateTotal()<17) { //while the dealer does not have 17 in card value, he continues to hit
            hit();
            if(busted = true){ //if the dealer busts, the function stops. Everything else has already been displayed in hit()
                return;
            }
        }
        if(calculateTotal()>player1Total){//if the dealer ends up with a higher score than the player, the player is met with a loss screen
            loss = new HBox(70);
            Text lossText = new Text("DEALER GOT A HIGHER SCORE! YOU LOSE!");
            Button lossButton = new Button("New Hand");
            lossButton.setOnAction(e -> newHand());
            money-=betAmount;
            calculateTotal();
            cardValues = new Text("Total Value of Cards: " + totalNumber);
            loss.getChildren().addAll(lossText, lossButton, cardValues,handsWonText);
            game.getChildren().addAll(loss);
        }else if(calculateTotal()==player1Total){
            noWin = new HBox(70);
            Text noText = new Text("YOU AND THE DEALER GOT THE SAME SCORE. NO ONE WINS.");
            Button noButton = new Button("New Hand");
            noButton.setOnAction(e -> newHand());
            calculateTotal();
            cardValues = new Text("Total Value of Cards: " + totalNumber);
            noWin.getChildren().addAll(noText, noButton, cardValues,handsWonText);
            game.getChildren().addAll(noWin);
        }
        else{ //if the dealer does not end up with a higher score, the player wins, and is met with a victory screen
            win = new HBox(70);
            Text winText = new Text("YOU GOT A HIGHER SCORE! YOU WIN!");
            handsWon++;
            money+=betAmount;
            handsWonText = new Text("Hands Won: " + handsWon);
            Button winButton = new Button("New Hand");
            winButton.setOnAction(e -> newHand());
            calculateTotal();
            cardValues = new Text("Total Value of Cards: " + totalNumber);
            win.getChildren().addAll(winText, winButton, cardValues,handsWonText);
            game.getChildren().addAll(win);
        }
    }

    private void hitSplit() {
        //draws a card and calculates whether the drawer has busted. It redraws the board after.
        if(!dealer) { //if the drawer is not the dealer
            stage.hide();
            int i = 0;
            while (i < cardsDrawn) {
                cardRow.getChildren().removeAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            draw();
            i = 0;
            while (i < cardsDrawn) {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
                texts[i].setFill(DODGERBLUE);
                cardRow.getChildren().addAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            game.getChildren().removeAll(splitBox1, splitBox2);
            if (calculateTotal() > 21) { //busted
                for (int x = 0; x < cardsDrawn; x++) {
                    if ((mainDeck[x].getFace() != null) && (mainDeck[x].getFace().equals("Ace"))) {
                        mainDeck[x].setNumber(1);
                        if (calculateTotal() < 21) {
                            return;
                        }
                    }
                }
                bust = new HBox(70);
                Text bustText = new Text("BUST! DEALER WINS!");
                money-=betAmount;
                Button bustButton = new Button("New Hand");
                bustButton.setOnAction(e -> newHand());
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                bust.getChildren().addAll(bustText, bustButton, cardValues);
                game.getChildren().addAll(cardRow, bust);
            } else { //not busted
                controlRow.getChildren().remove(cardValues);
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                controlRow.getChildren().add(cardValues);
                game.getChildren().addAll(cardRow, controlRow);
            }
            stage.show();
        }else{ //if the drawer is the dealer
            stage.hide();
            int i = 0;
            while (i < cardsDrawn) {
                cardRow.getChildren().removeAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            draw();
            i = 0;
            while (i < cardsDrawn) {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
                texts[i].setFill(DODGERBLUE);
                cardRow.getChildren().addAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            game.getChildren().removeAll(splitBox1, splitBox2);
            if (calculateTotal() > 21) { // dealer busted
                for (int x = 0; x < cardsDrawn; x++) {
                    if ((mainDeck[x].getFace() != null) && (mainDeck[x].getFace().equals("Ace"))) {
                        mainDeck[x].setNumber(1);
                        if (calculateTotal() < 21) {
                            return;
                        }
                    }
                }
                bust = new HBox(70);
                Text bustText = new Text("DEALER BUSTED! YOU WIN!");
                money+=betAmount;
                handsWon++;
                handsWonText = new Text("Hands Won: " + handsWon);
                Button bustButton = new Button("New Hand");
                bustButton.setOnAction(e -> newHand());
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                bust.getChildren().addAll(bustText, bustButton, cardValues,handsWonText);
                game.getChildren().addAll(cardRow, bust);
                busted = true;
            } else { //not busted
                controlRow.getChildren().remove(cardValues);
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                controlRow.getChildren().add(cardValues);
                game.getChildren().addAll(cardRow, controlRow);
            }
            stage.show();
        }
    }

    private void stand() {
        betAmount=parseInt(betNumber.getText().toString());
        stage.hide();
        dealer = true;
        player1Total = totalNumber;
        totalNumber = 0;
        game.getChildren().removeAll(cardRow,controlRow);
        cardRow = new HBox(20); //Rows for content like cards/buttons
        cardRow.setMinHeight(500); //setting height for the row containing cards

        game = new VBox(10); //Vertical layout to store the rows inside

        texts = new Text[52];

        cardsDrawn = 2; //sets cards drawn to 2 for starting hand
        generateDeck(); //generates a new deck
        int i =0;
        while(i<cardsDrawn) { //does the same thing that is in start(). It draws the cards onto the table, this time for the dealer
            if(mainDeck[i].getFace()!=null){
                texts[i] = new Text(0, 0, mainDeck[i].getFace() + "\n" + mainDeck[i].getSuite());
            }else {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
            }
            texts[i].setFill(DODGERBLUE);
            cardRow.getChildren().addAll(mainDeck[i].getGraphic(),texts[i]);
            i++;
        }
        game.getChildren().addAll(cardRow);//adds cards to the layout
        stage.setScene(new Scene(game,1920,600)); //creates window
        stage.show();//shows window
        while(calculateTotal()<17) { //while the dealer does not have 17 in card value, he continues to hit
                hit();
                if(busted = true){ //if the dealer busts, the function stops. Everything else has already been displayed in hit()
                    return;
                }
        }
        if(calculateTotal()>player1Total){//if the dealer ends up with a higher score than the player, the player is met with a loss screen
            loss = new HBox(70);
            Text lossText = new Text("DEALER GOT A HIGHER SCORE! YOU LOSE!");
            Button lossButton = new Button("New Hand");
            lossButton.setOnAction(e -> newHand());
            money-=betAmount;
            calculateTotal();
            cardValues = new Text("Total Value of Cards: " + totalNumber);
            loss.getChildren().addAll(lossText, lossButton, cardValues,handsWonText);
            game.getChildren().addAll(loss);
        }else if(calculateTotal()==player1Total){
            noWin = new HBox(70);
            Text noText = new Text("YOU AND THE DEALER GOT THE SAME SCORE. NO ONE WINS.");
            Button noButton = new Button("New Hand");
            noButton.setOnAction(e -> newHand());
            calculateTotal();
            cardValues = new Text("Total Value of Cards: " + totalNumber);
            noWin.getChildren().addAll(noText, noButton, cardValues,handsWonText);
            game.getChildren().addAll(noWin);
        }
        else{ //if the dealer does not end up with a higher score, the player wins, and is met with a victory screen
            win = new HBox(70);
            Text winText = new Text("YOU GOT A HIGHER SCORE! YOU WIN!");
            handsWon++;
            money+=betAmount;
            handsWonText = new Text("Hands Won: " + handsWon);
            Button winButton = new Button("New Hand");
            winButton.setOnAction(e -> newHand());
            calculateTotal();
            cardValues = new Text("Total Value of Cards: " + totalNumber);
            win.getChildren().addAll(winText, winButton, cardValues,handsWonText);
            game.getChildren().addAll(win);
        }
    }

    private void hit() {//draws a card and calculates whether the drawer has busted. It redraws the board after.
        if(!dealer) { //if the drawer is not the dealer
            stage.hide();
            int i = 0;
            while (i < cardsDrawn) {
                cardRow.getChildren().removeAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            draw();
            i = 0;
            while (i < cardsDrawn) {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
                texts[i].setFill(DODGERBLUE);
                cardRow.getChildren().addAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            game.getChildren().removeAll(cardRow, controlRow);
            if (calculateTotal() > 21) { //busted
                for (int x = 0; x < cardsDrawn; x++) {
                    if ((mainDeck[x].getFace() != null) && (mainDeck[x].getFace().equals("Ace"))) {
                        mainDeck[x].setNumber(1);
                        if (calculateTotal() < 21) {
                            return;
                        }
                    }
                }
                bust = new HBox(70);
                Text bustText = new Text("BUST! DEALER WINS!");
                money-=betAmount;
                Button bustButton = new Button("New Hand");
                bustButton.setOnAction(e -> newHand());
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                bust.getChildren().addAll(bustText, bustButton, cardValues);
                game.getChildren().addAll(cardRow, bust);
            } else { //not busted
                controlRow.getChildren().remove(cardValues);
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                controlRow.getChildren().add(cardValues);
                game.getChildren().addAll(cardRow, controlRow);
            }
            stage.show();
        }else{ //if the drawer is the dealer
            stage.hide();
            int i = 0;
            while (i < cardsDrawn) {
                cardRow.getChildren().removeAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            draw();
            i = 0;
            while (i < cardsDrawn) {
                texts[i] = new Text(0, 0, mainDeck[i].getNumber() + "\n" + mainDeck[i].getSuite());
                texts[i].setFill(DODGERBLUE);
                cardRow.getChildren().addAll(mainDeck[i].getGraphic(), texts[i]);
                i++;
            }
            game.getChildren().removeAll(cardRow, controlRow);
            if (calculateTotal() > 21) { // dealer busted
                for (int x = 0; x < cardsDrawn; x++) {
                    if ((mainDeck[x].getFace() != null) && (mainDeck[x].getFace().equals("Ace"))) {
                        mainDeck[x].setNumber(1);
                        if (calculateTotal() < 21) {
                            return;
                        }
                    }
                }
                bust = new HBox(70);
                Text bustText = new Text("DEALER BUSTED! YOU WIN!");
                money+=betAmount;
                handsWon++;
                handsWonText = new Text("Hands Won: " + handsWon);
                Button bustButton = new Button("New Hand");
                bustButton.setOnAction(e -> newHand());
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                bust.getChildren().addAll(bustText, bustButton, cardValues,handsWonText);
                game.getChildren().addAll(cardRow, bust);
                busted = true;
            } else { //not busted
                controlRow.getChildren().remove(cardValues);
                calculateTotal();
                cardValues = new Text("Total Value of Cards: " + totalNumber);
                controlRow.getChildren().add(cardValues);
                game.getChildren().addAll(cardRow, controlRow);
            }
            stage.show();
        }
    }

    private void newHand() { //new game or hand
        totalNumber = 0;
        cardsDrawn = 0;
        dealer = false;
        busted = false;
        game.getChildren().removeAll(win,loss,noWin);
        start(stage);
    }
}
