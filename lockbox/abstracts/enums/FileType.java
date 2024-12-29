package org.vigilance.lockbox.abstracts.enums;

public enum FileType {
    TXT(".txt"),
    PDF(".pdf"),
    PNG(".png"),
    JPG(".jpg"),
    ZIP(".zip");

    private String type;

    FileType(String type){
        this.type = type;
    }

    public String getNotation(){
        return type;
    }

}
