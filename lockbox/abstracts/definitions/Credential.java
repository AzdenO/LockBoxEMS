package org.vigilance.lockbox.abstracts.definitions;

import org.vigilance.lockbox.abstracts.Secure;

/**
 * @author Declan R.A Wadsworth
 * @version 0.1
 * Base class that is extended from to form specific types of credentials such as bank details, online accounts as well as
 * pins and codes for specific things
 */
public class Credential {
    public String domain;//attribute to store name of credential such as bank name, name of website, name associated with a code
    public String group="default";//attribute to hold group tag of credential for user-defined groupings
    public boolean encrypted = false;//attribute to provide information on whether the credentials in question are safely stored

    public String[] getData(){
        return new String[]{"",""};
    }

    public void update(String[] data, Secure security){

    }

    public String toDialog(){
        return "";
    }

    public String genAsterix(int len){

        return "************";
    }

}
