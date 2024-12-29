package org.vigilance.lockbox.utils.checks;

import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InputCheck {
    public static String errorMsg=null;
    public static ArrayList<TextField> fields;
    public static String fieldBorderColour;

    public static boolean checkContents(){
        for(TextField field: fields){
            if(field.getText()==null || field.getText().equals("")){
                errorMsg = "A field has been left empty";
                field.setStyle("-fx-border-color: red");
                return false;
            }else{
                field.setStyle("-fx-border-color: "+fieldBorderColour);
            }
        }
        return true;
    }
    public static boolean checkFilePath(TextField field){
        File filecheck = new File(field.getText());
        if(filecheck.exists()){
            return true;
        }else{
            errorMsg="File(s) does not exist";
            return false;
        }
    }
    public static boolean checkFilePath(List<File> files){
        for(File file: files){
            if(!(file.exists())){
                errorMsg="One or more files do not exist";
                return false;
            }
        }
        return true;
    }

    private class checkUtils{

    }


}
