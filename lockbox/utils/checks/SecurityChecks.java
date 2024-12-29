package org.vigilance.lockbox.utils.checks;

public class SecurityChecks {
    public static String errorMsg=null;

    public static boolean checkNewPass(String newPass){
        if(newPass.length()<10){
            errorMsg = "Password must be atleast 10 characters long";
            return false;
        }
        return true;
    }
}
