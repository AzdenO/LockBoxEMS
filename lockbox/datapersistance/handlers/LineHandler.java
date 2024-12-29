package org.vigilance.lockbox.datapersistance.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LineHandler {
    private String linebuffer = "";
    private final String savepath;
    private FileWriter writer;
    private Scanner reader;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public LineHandler(String path, String data){
        this.savepath = path;
        this.linebuffer = data;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveToPath() throws IOException{
        this.writer = new FileWriter(this.savepath);
        this.writer.flush();
        this.writer.write(this.linebuffer);
        this.writer.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String loadFromPath() throws IOException{
        this.reader = new Scanner(new File(this.savepath));
        String read = this.reader.nextLine();
        this.reader.close();
        return read;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
