package org.vigilance.lockbox.utils.enums;

public enum Origin {
    EXTENSION("Extension"),
    APPLICATION("APPLICATION");

    private String origin;

    Origin(String origin){
        this.origin = origin;
    }

    public String toNotation(){
        return origin;
    }
}
