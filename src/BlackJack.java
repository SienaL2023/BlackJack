import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

        public int getValue(){
            // checking to see if value = a j k q
            if("AJQK".contains(value)){
                if(value == "A")
                    return 11;
                else
                    return 10;
            }
            else
                return Integer.parseInt(value);
        }
        
        public boolean isAce(){
            if(value == "A")
                return true;
            else
                return false;
        }

        public String getImagePath(){ // returning the path to a specific card
            return "./cards/" + toString() + ".png";
        }


    }
    ArrayList<Card> deck;
    Random random = new Random();

    // dealer hand
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;
    // ace value -> default 11, but can choose to change to 1
    // player hand
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // GUI window
    int boardWidth = 600;
    int boardHeight = 600;

    int cardWidth = 110; // ratio = 1/1.4
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try{
                Image hiddenCardImage = new ImageIcon(getClass().getResource("./back.png")).getImage();
                if(!stayButton.isEnabled()){  // stya button hit
                    hiddenCardImage = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImage, 20,20, cardWidth, cardHeight, null);     
            }
            catch (Exception e){
                e.printStackTrace();
            }

            // draw dealer;s hand
            for(int i = 0; i<dealerHand.size(); i++){
                Card card = dealerHand.get(i);
                // pulling imagge of specifc card
                Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
            }

            // draw player's hand
            for(int i = 0; i < playerHand.size(); i++){
                Card card = playerHand.get(i);
                Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 320, cardWidth, cardHeight, null);
            }

            // win/lose display and conditions
            if(!stayButton.isEnabled()){
                dealerSum = reduceDealerAce();
                playerSum = reducePlayerAce();
                System.out.println("STAY: ");
                System.out.println(dealerSum);
                System.out.println(playerSum);
                String message = "";
                if(playerSum <= 21){
                    if(dealerSum <= 21){
                        if ((playerSum) == (dealerSum))
                            message = "You tied!";
                        else if((playerSum) > (dealerSum)){
                            message = "You won!!!!!!!!";
                        }
                        else if(playerSum < dealerSum)
                            message = "You lost!";
                    }
                    else
                        message = "You win!!!!!!!!";
                    
                }
                else
                    message = "You lost!";
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString(message, 220,250);
                resetButton.setEnabled(true);
            }
            
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton ("Stay");
    JButton resetButton = new JButton("Reset");

    public BlackJack(){
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(240, 199, 233));
        frame.add(gamePanel);

        hitButton.setFocusable(false); // can button be controlled by keybaord or not
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        resetButton.setFocusable(false);
        resetButton.setEnabled(false);
        buttonPanel.add(resetButton);

        // hit button
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                // get new card  <--> remove from deck
                // new total sum
                // ace counter
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                if(card.isAce()){
                    playerAceCount += 1;
                }
                playerHand.add(card);
                if(reducePlayerAce() >= 21) // disables hit button
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                // dealer's turn to get a card
                while(dealerSum < 17){
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    if(card.isAce()){
                        dealerAceCount += 1;
                    }
                    dealerHand.add(card);
                }
                gamePanel.repaint();

            }
        });

        // reset button
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                resetButton.setEnabled(false);
                startGame();
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();


    }

    public void startGame(){
        buildDeck();
        shuffleDeck();

        // logic 

        // dealer hand
        dealerHand = new ArrayList<Card>(); // create new arraylist
        dealerSum = 0;
        dealerAceCount = 0;
       
        // removes a card from deck, hiddenCard holds onto the removed card
        hiddenCard = deck.remove(deck.size()-1);

        dealerSum += hiddenCard.getValue();
        if(hiddenCard.isAce())
            dealerAceCount += 1;


        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        if(card.isAce())
            dealerAceCount += 1;

        dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        // players hand
        System.out.println("PLAYER:");

        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;
        for(int i = 0; i <2; i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            if(card.isAce())
                playerAceCount += 1;
            playerHand.add(card);
        }
        

        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
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

        System.out.println("AFTER SHUFFLE:");
        System.out.println(deck);
    }

    public int reducePlayerAce(){  // return the new sum
        while(playerSum > 21 && playerAceCount > 0){
            playerSum -= 10;
            playerAceCount -= 1;
        }
        playerAceCount = 0;
        return playerSum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        dealerAceCount = 0;
        return dealerSum;
    }

}
