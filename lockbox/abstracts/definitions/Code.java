package org.vigilance.lockbox.abstracts.definitions;

import org.vigilance.lockbox.abstracts.Secure;

public class Code extends Credential{
    private String code;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Code(String code, String name, String group) {
        this.code = code;
        this.domain = name;
        if(group==null){
            this.group="default";
        }else{
            this.group=group;
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Code (String strung){
        String[] split = strung.split("/--/");
        this.domain = split[0];
        this.code = split[1];
        this.group = split[2];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString(){
        return(this.domain+"/--/"+this.code+"/--/"+this.group+"//--//");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                counter++;
            }
            if(counter==1){
                this.code = info[counter];
                counter++;
            }
            if(counter==2){
                this.group = info[counter];
                counter++;
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String toDialog(){
        return(this.domain+"\n"
                +genAsterix(this.code.length()));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String[] getData(){
        return new String[]{this.domain,this.code,this.group};
    }
}
