import java.util.ArrayList;
import java.util.Random;

public class BlackJack {

    private class Card{
        String value;
        String type;
        

        Card(String value, String type){
            this.value = value;
            this.type = type;
        }

        public String toString(){
            return value + "-" + type;
        }


    }
    ArrayList<Card> deck;
    Random random = new Random();

    public BlackJack(){
        startGame();
    }

    public void startGame(){
        buildDeck();
        shuffleDeck();
    }

    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"}; // clubs, diamonds, hearts, spades

        // create deck with the given values and types

        for(int i = 0; i < values.length; i++){
            for(int j = 0; j < types.length; j++){
                Card card = new Card(values[i],types[j]);
                deck.add(card);
            }
        }
        System.out.println("BUILT DECK!!");
        System.out.println(deck);
    }

    public void shuffleDeck(){
        for(int i = 0; i < deck.size(); i++){
            int j = random.nextInt(deck.size()); //generates random num from 0-51
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }

}
