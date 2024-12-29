package org.vigilance.lockbox.abstracts.definitions;

import org.vigilance.lockbox.abstracts.Secure;

import java.util.ArrayList;
/**
 * public java class to encapsulate data and operations relating to bank accounts
 * @author Declan R.A Wadsworth
 * @version 0.1
 */
public class BankAccount extends Credential{
    public String accnum;
    public String sortcode;
    public String customernumber;
    public String password;
    private ArrayList<BankCard> cards = new ArrayList<>();
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BankAccount(String accnum,String code,String cusNum,String pass, String name){
        this.accnum = accnum;
        this.sortcode = code;
        this.customernumber = cusNum;
        this.password=pass;
        this.domain = name;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BankAccount(String strung){
        fromString(strung);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for adding bank card to a bank account
     * @param card
     */
    public void addCard(BankCard card){
        this.cards.add(card);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for removing a bank card from a bank account
     * @param toremove card to be removed
     */
    public void removeCard(BankCard toremove){
        for(BankCard card: this.cards){
            if(card.getCardnum().equals(toremove.getCardnum())){
                this.cards.remove(card);
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<BankCard> getCards(){
        return this.cards;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method to convert bank account into file-writable notation
     * @return a string that reflects the bank object passed
     */
    @Override
    public String toString(){
        //////////
        //Convert cards to writable format
        String strcards = "";
        for(BankCard card: this.cards){
            strcards+=(card.toString());
        }
        strcards+="/--/";

        //////////
        //Convert account
        return(this.domain+"/--/"+this.password+"/--/"+this.group+"/--/"+this.accnum+"/--/"+this.sortcode+"/--/"+this.customernumber+"///"+strcards);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for retrieving a bank object from string notation
     * @param stringed is the bank account in string format
     */
    public void fromString(String stringed){
        //////////
        //Seperate cards from other account information and expand cards
        String[] init_split = stringed.split("///");
        String[] card_split=null;
        try{
            card_split = init_split[1].split("/--/");
        }catch(ArrayIndexOutOfBoundsException i){
            //no cards
        }


        //////////
        //Convert and retrieve cards
        ArrayList<BankCard> retrieved = new ArrayList<>();
        BankCard card;
        if(card_split!=null){
            for(String strung: card_split){
                card  = new BankCard();
                card.fromString(strung);
                retrieved.add(card);
            }
        }
        String[] info_split = init_split[0].split("/--/");
        this.domain = info_split[0];
        this.password = info_split[1];
        this.group = info_split[2];
        this.accnum = info_split[3];
        this.sortcode = info_split[4];
        this.customernumber = info_split[5];
        this.cards = retrieved;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for updating account data
     * @param info
     */
    @Override
    public void update(String[] info, Secure security) {
        int counter = 0;
        for (String data : info) {
            if (data == null) {
                counter++;
                continue;
            }
            if (counter == 0) {

            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for retrieving account data for editing
     * @return a String array with the objects data
     */
    @Override
    public String[] getData(){
        String[] data = new String[5+((this.cards.size())*5)];
        data[0] = this.domain;
        data[1] = this.accnum;
        data[2] = this.sortcode;
        data[3] = this.customernumber;
        data[4] = this.password;
        return data;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String toDialog(){
        return (this.domain+"\n"
                +this.accnum+"\n"
                +this.sortcode+"\n"
                +this.customernumber+"\n"
                +this.password+"\n"
                +this.cards.size());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
