package org.vigilance.lockbox.abstracts.definitions;

import org.vigilance.lockbox.abstracts.Secure;

public class BankCard extends Credential{
    private String cardnum;
    private String secnum;
    private String expirdate;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BankCard(String name,String cardnum, String secnum, String expirdate, String group) {
        this.domain = name;
        this.cardnum = cardnum;
        this.secnum = secnum;
        this.expirdate = expirdate;
        if(group==null){
            this.group="default";
        }else{
            this.group=group;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BankCard(){

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BankCard(String strung){
        fromString(strung);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String[] getData(){
        String[] data = {this.domain,getCardnum(),getExpirdate(),getSecnum(),this.group};
        return data;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void update(String[] info, Secure security){
        this.domain = info[0];
        this.cardnum = info[1];
        this.expirdate = info[2];
        this.secnum = info[3];
        this.group=info[4];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getSecnum() {
        return secnum;
    }

    public void setSecnum(String secnum) {
        this.secnum = secnum;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getExpirdate() {
        return expirdate;
    }

    public void setExpirdate(String expirdate) {
        this.expirdate = expirdate;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString(){
        return(this.domain+"/--/"+this.cardnum+"/--/"+this.expirdate+"/--/"+this.secnum+"/--/"+this.group+"//--//");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void fromString(String card){
        String[] split = card.split("/--/");//split into actionable parts
        split[4].replaceAll("//--//","");//remove end marker
        this.domain = split[0];
        this.setCardnum(split[1]);
        this.setExpirdate(split[2]);
        this.setSecnum(split[3]);
        this.group=split[4];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toDialog(){
        String result = "";
        result+=this.domain+"\n"+this.cardnum+"\n"+this.expirdate+"\n***";
        return result;
    }
}
