package org.vigilance.lockbox.utils.configs;

import org.vigilance.lockbox.abstracts.Secure;
import org.vigilance.lockbox.datapersistance.FileStore;
import org.vigilance.lockbox.datapersistance.SaveConfigurations;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Configuration {
    public String backgroundPath;
    public String colourHex;
    public String secondaryHex;
    public String temporar;
    public String nextLogWipe;
    public ArrayList<AccessLog> logs=new ArrayList<>();
    private transient FileStore storage = new FileStore(System.getenv("APPDATA")+"\\LockData\\log.bin", SaveConfigurations.BYTE);
    private transient Secure security;

    public String interval;

    public Configuration(Secure security){
        this.security = security;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void checkWipe(){
        if(nextLogWipe==null || nextLogWipe.equals("null")){
            updateConfig("2 Weeks");
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatting = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowForat = formatting.format(now);
        if(nowForat.equals(this.nextLogWipe)){
            logs.clear();
            updateConfig(this.interval);
        }
        else{
            String[] wipeSplit = nextLogWipe.split("-");
            String[] nowSplit = nowForat.split("-");
            int nowday = Integer.parseInt(nowSplit[2]);
            int wipeday = Integer.parseInt(wipeSplit[2]);
            int wipemonth = Integer.parseInt(wipeSplit[1]);
            int nowmonth = Integer.parseInt(nowSplit[1]);
            int wipeyear = Integer.parseInt(wipeSplit[0]);
            int nowyear = Integer.parseInt(nowSplit[0]);

            if(nowday>wipeday && nowmonth>=wipemonth){
                logs.clear();
                updateConfig(this.interval);
            }else if(nowmonth>wipemonth){
                logs.clear();
                updateConfig(this.interval);
            }else if(nowyear>wipeyear){
                logs.clear();
                updateConfig(this.interval);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadLogs(){
        byte[] data = storage.loadBytes();
        if(data==null){
            return;
        }
        String loaded = new String(data,StandardCharsets.UTF_8);
        String[] split = loaded.split("////--////");
        if(split.length==1){
            temporar=null;
        }else{
            temporar = split[1];
        }
        compileConfig(split[0]);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveLogs(){
        byte[] data = ((concatConfig()+"////--////"+concatLogs())).getBytes(StandardCharsets.UTF_8);
        storage.saveBytes(data);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String concatLogs(){
        String result="";
        if(this.logs.size()==0){
            return null;
        }
        for(AccessLog log: this.logs){
            result+=(log.toString()+"//--//");
        }
        result = security.encrypt(result);
        return result;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void compileLogs(){
        if(temporar==null){
            return;
        }
        if(temporar.equals("null")){
            return;
        }
        temporar = security.decrypt(temporar);
        if(temporar==null){
            return;
        }
        String[] logsSplit = temporar.split("//--//");
        for(String split: logsSplit){
            this.logs.add(new AccessLog(split));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String concatConfig(){
        return this.colourHex+"/--/"+this.backgroundPath+"/--/"+this.nextLogWipe+"/--/"+this.interval;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void compileConfig(String concat){
        String[] split = concat.split("/--/");
        this.colourHex = split[0];
        this.backgroundPath=split[1];
        this.nextLogWipe=split[2];
        this.interval=split[3];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateConfig(String selection){
        LocalDateTime intermediate = LocalDateTime.now();
        switch(selection){
            case "2 Weeks":
                intermediate = intermediate.plusDays(14);
                break;
            case "1 Month":
                intermediate=intermediate.plusMonths(1);
                break;
            case "2 Months":
                intermediate=intermediate.plusMonths(2);
                break;
            case "3 Months":
                intermediate=intermediate.plusMonths(3);
                break;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        this.nextLogWipe = format.format(intermediate);
        this.interval=selection;
        saveLogs();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
