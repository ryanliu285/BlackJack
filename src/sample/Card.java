package sample;


import javafx.scene.shape.Rectangle;

public class Card {
    public int number, suiteId; //number is number of the card, suiteId is the suite. 1 is diamonds, 2 is clubs, 3 is hearts, 4 is spades.
    public Rectangle graphic;
    public String face;

    public Card(int number,int suite){
        if(number>10) {
            this.number = 10;
            if(number == 11){
                face = "Jack";
            }
            else if(number == 12){
                face = "Queen";
            }
            else if(number == 13){
                face = "King";
            }
        }else if(number ==1){
           this.number = 11;
           face = "Ace";
        }
        else {
            this.number = number;
        }
        suiteId = suite; //sets this card suite to given suite
        this.graphic = new Rectangle(100,170);//creates card graphic
    }

    public int getNumber(){
        return number;
    }

    public String getSuite(){
        if(suiteId == 1){
            return "diamonds";
        }
        else if (suiteId ==2){
            return "clubs";
        }
        else if (suiteId == 3){
            return "hearts";
        }
        else{
            return "spades";
        }
    }

    public void setNumber(int number){
        this.number = number;
    }

    public Rectangle getGraphic(){
        return graphic;
    }

    public String getFace(){
        return face;
    }
}
