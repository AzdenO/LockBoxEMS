package org.vigilance.lockbox.datapersistance;


import org.vigilance.lockbox.datapersistance.handlers.ByteHandler;
import org.vigilance.lockbox.datapersistance.handlers.LineHandler;
import org.vigilance.lockbox.datapersistance.handlers.TableHandler;

import java.util.ArrayList;

/**
 * Java class for handling saving and loading of data, and will be applicable to all services that have persistant data within the system
 * @author Declan R.A Wadsworth
 * @version 0.1
 */
public class FileStore {
    public String name;//attribute for naming a filestore for easier readability
    private String location;//constant for holding file name for where data will be stored
    private final SaveConfigurations savemethod;//constant for defining the way data is being saved
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public FileStore(String directory, SaveConfigurations config){
        this.location = directory;
        this.savemethod = config;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setLocation(String location){
        this.location = location;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveTable(ArrayList<String> data){//method for saving credential data
        TableHandler handler = new TableHandler(this.location);
        handler.saveToPath(data);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<String> loadTable(){
        TableHandler handler = new TableHandler(this.location);
        handler.loadFromPath();
        return(handler.databuffer);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveLine(String data){
        LineHandler handler = new LineHandler(this.location, data);
        try{
            handler.saveToPath();
        }catch(Exception e){

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String loadLine(){
        LineHandler handler = new LineHandler(this.location, null);
        String read = "";
        try{
            read = handler.loadFromPath();
        }catch(Exception e){

        }
        return read;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public byte[] loadBytes(){
        ByteHandler handler = new ByteHandler(this.location);
        return handler.loadFromPath();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveBytes(byte[] data){
        ByteHandler handler = new ByteHandler(this.location);
        handler.saveToPath(data);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
