package org.vigilance.lockbox.abstracts.definitions;

import org.vigilance.lockbox.abstracts.Secure;

/**
 * Java class to encapsulate a set of user credentials, including methods to decrypt information in an account, as well
 * as encrypt new accounts created by a user.
 * @author Declan R.A Wadsworth
 * @version 0.1
 */
public class Account extends Credential {//all attributes stored as ciphertext
    public String email;//attribute to hold an associated email
    public String username = null;//attribute to hold associated username, can be null if not necessary
    public String password;//attribute to hold the associated password
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Account(){

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *constructor to take plaintext data for an account, encrypt it and instantiate the new account object
     */
    public Account(String d, String e, String u, String p, String g){
        this.domain = d;
        this.email = e;
        this.username = u;
        this.password = p;
        if(g==null){
            this.group="default";
            return;
        }
        this.group = g;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Account(String stringedaccount){
        String[] split = stringedaccount.split("/--/");
        this.domain = split[0];
        this.email = split[1];
        this.username = split[2];
        this.password = split[3];
        this.group = split[4];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method to parse account object to a string to be written to a file
     * @return a string with correct configuration in relation to the account passed
     */
    @Override
    public String toString(){
        return(this.domain+"/--/"+this.email+"/--/"+this.username+"/--/"+this.password+"/--/"+this.group+"//--//");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for updating information for an account
     * @param info represents a string of size 5 containing updating information, or nulls for where there is no change
     */
    @Override
    public void update(String[] info, Secure security){
        int counter=0;
        for(String data: info){
            if(data==null){
                counter++;
                continue;
            }

            if(counter==0){
                this.domain = info[counter];
            }
            if(counter==1){
                this.email = info[counter];
            }
            if(counter==2){
                this.username = info[counter];
            }
            if(counter==3){
                this.password = info[counter];
            }
            if(counter==4){
                this.group = info[counter];
            }
            counter++;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Public method for retrieving account data
     * @return an array of the objects data
     */
    @Override
    public String[] getData(){
        String[] data = new String[5];
        data[0] = this.domain;
        data[1] = this.email;
        data[2] = this.username;
        data[3] = this.password;
        data[4] = this.group;

        return data;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String toDialog(){
        String result="";
        result+=this.domain+"\n";
        if(this.username!=null){
            result+=this.username+"\n";
        }
        result+=this.email+"\n";
        result+=genAsterix(this.password.length())+"\n";
        return result;
    }

}
