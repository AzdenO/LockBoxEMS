package org.vigilance.lockbox.abstracts.definitions;

import org.vigilance.lockbox.abstracts.Secure;

public class Note extends Credential{

    public String text;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Note(){

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Note(String strung){
        String[] split = strung.split("/--/");
        this.domain = split[0];
        this.text = split[1];
        this.group = split[2];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString(){return(this.domain+"/--/"+this.text+"/--/"+this.group+"//--//");}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void update(String[] info, Secure security){
        int counter=0;
        for(String data:info){
            if(data==null){
                counter++;
                continue;
            }
            if(counter==0){
                this.domain=info[counter];
            }
            if(counter==1){
                this.text=info[counter];
            }
            if(counter==2){
                this.group=info[counter];
            }
            counter++;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String toDialog(){
        return(this.domain);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String[] getData(){return new String[]{this.domain,this.text,this.group};}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
