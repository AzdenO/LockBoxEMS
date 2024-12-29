package org.vigilance.lockbox.utils.configs;

import org.vigilance.lockbox.abstracts.enums.DataType;
import org.vigilance.lockbox.utils.enums.Origin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Public java class to define when lockbox data is accessed
 */
public class AccessLog {

    /**
     * Where access has originated from (atm either browser extension or applciation itself
     */
    private final Origin accessOrigin;

    /**
     * When the data was accessed
     */
    private final String accessTime;

    /**
     * The type of data that was accessed (A file or image or bank, etc.)
     */
    private final DataType accessType;

    /**
     * The data group that was accessed
     */
    private final String accessGroup;

    /**
     * The name of the specific data accessed
     */
    private final String accessData;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public AccessLog(Origin origin, DataType type, String group, String name){

        this.accessOrigin = origin;
        this.accessType = type;
        this.accessGroup = group;
        this.accessData = name;

        DateTimeFormatter formatting = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.accessTime = LocalDateTime.now().format(formatting);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public AccessLog(String strungLog){
        String[] logSplit = strungLog.split("/--/");
        this.accessOrigin = Origin.valueOf(logSplit[0]);
        this.accessTime = logSplit[1];
        this.accessType = DataType.valueOf(logSplit[2]);
        this.accessGroup = logSplit[3];
        this.accessData = logSplit[4];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString(){
        return (this.accessOrigin.toNotation()+"/--/"+this.accessTime+"/--/"+this.accessType.getNotation()+"/--/"+this.accessGroup+"/--/"+this.accessData);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Origin getAccessOrigin() {
        return accessOrigin;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public DataType getAccessType() {
        return accessType;
    }

    public String getAccessGroup() {
        return accessGroup;
    }

    public String getAccessData() {
        return accessData;
    }
}
