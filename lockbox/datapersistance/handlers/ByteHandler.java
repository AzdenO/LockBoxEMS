package org.vigilance.lockbox.datapersistance.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteHandler {
     private final String filePath;

    public ByteHandler(String filePath){
        this.filePath = filePath;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public byte[] loadFromPath(){
        try{
            FileInputStream reading = new FileInputStream(this.filePath);
            byte[] data = reading.readAllBytes();
            reading.close();
            return data;
        }catch(Exception f){

        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveToPath(byte[] data){
        try{
            FileOutputStream saving = new FileOutputStream(this.filePath);
            saving.write(data);
            saving.close();
        }catch(FileNotFoundException e){
            System.out.println("file not found");
        }catch(IOException i){
            System.out.println("IO exception");
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
