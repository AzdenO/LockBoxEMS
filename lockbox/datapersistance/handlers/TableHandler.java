package org.vigilance.lockbox.datapersistance.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class TableHandler {
    public ArrayList<String> databuffer =new ArrayList<>();
    private FileWriter writer;
    private BufferedReader reader;
    private String savepath;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TableHandler(String savepath){
        this.savepath = savepath;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveToPath(ArrayList<String> data){
        try{
            this.writer = new FileWriter(savepath);
            for(String line: data){
                this.writer.write(line+System.lineSeparator());
            }
            this.writer.close();
        }catch(Exception e){
            ;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadFromPath(){
        String line = "";
        try{
            this.reader = new BufferedReader(new FileReader(new File(this.savepath).getAbsolutePath()));
        }catch(Exception e){
            System.out.print(e);
        }
        while(line!=null){
            try{
                line = this.reader.readLine();
                this.databuffer.add(line);
                this.reader.close();
            }catch(Exception e){

            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
