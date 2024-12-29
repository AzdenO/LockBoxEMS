package org.vigilance.lockbox.abstracts.enums;

/**
 * Public enumerator to define all types the app deals with
 * @author Declan R.A Wadsworth
 * @version 0.1
 */
public enum DataType {
    BANKS("BANKS"),//bank accounts
    ACCOUNTS("ACCOUNTS"),//online accounts
    CODES("CODES"),//general codes/pins
    FILES("FILES"),//files such as .pdf, .txt,
    IMAGES("IMAGES"),

    NOTES("NOTES"),
    DEFAULT("DEFAULT");//images (files such as .png, .jpeg, etc.)

    private String type;

    DataType(String type) {
        this.type = type;
    }

    public String getNotation(){
        return type;
    }
}
