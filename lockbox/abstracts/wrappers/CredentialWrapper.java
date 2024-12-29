package org.vigilance.lockbox.abstracts.wrappers;

import org.vigilance.lockbox.App.Manager;
import org.vigilance.lockbox.abstracts.Secure;
import org.vigilance.lockbox.abstracts.definitions.*;
import org.vigilance.lockbox.abstracts.enums.DataType;
import org.vigilance.lockbox.datapersistance.FileStore;
import org.vigilance.lockbox.datapersistance.SaveConfigurations;
import org.vigilance.lockbox.organisation.Group;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import static org.vigilance.lockbox.abstracts.enums.DataType.FILES;
import static org.vigilance.lockbox.abstracts.enums.DataType.IMAGES;

/**
 * Public java class for encapsulating collections of different credential types as well as methods for manipulating
 * them
 * @param <Object> the credential object to provide an organising structure for
 * @author Declan R.A Wadsworth
 * @version 0.1
 */
public class CredentialWrapper<Object> {

    /**
     * Attribute to hold reference to wrappers credential type
     */
    private final DataType type;

    /**
     * Attribute for encrypting and decrypting wrapper data
     */
    private transient Secure security;

    /**
     * Attribute used to access loading and saving mechanisms
     */
    private final WrapperPersistence persistance;

    /**
     * Attribute for holding wrapper data
     */
    public ArrayList<Group<Object>> data = new ArrayList<>();
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Public constructor that assigns all attributes
     *
     * @param security represents the security object passed from Manager.java
     */
    public CredentialWrapper(Secure security, DataType type){
        this.security = security;
        this.type = type;
        this.persistance = this.new WrapperPersistence();
        //this.security.newKey("password");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void decryptGroupNames(){
        for(Group<Object> group : this.data){
            group.name = security.decrypt(group.name);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Group<Object> getGroup(String group){
        if(group==null || group.equals("All") || group.equals("null")){
            group="default";
        }
        int groupIndex = indexOfGroup(group);
        return this.data.get(groupIndex);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newGroup(String name){
        Group<Object> newG = new Group<>(name);
        this.data.add(newG);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteGroup(String name, boolean keepData, Manager locker){
        int index = indexOfGroup(name);
        if(keepData){
            Group<Object>  todelte = this.data.get(index);
            for(Object data: todelte){
                locker.lockSingle((Credential)data,true );
                ((Credential)data).group="default";
            }
            this.data.get(0).addAll(this.data.get(index));
        }else{
            if(this.type.equals(FILES) || this.type.equals(IMAGES)){
                Group<Object> todelete = this.data.get(index);
                for(Object file: todelete){
                    locker.lockSingle((Credential)file,true );
                    File remove = new File(((UserFile)file).generateInternalPath());
                    remove.delete();
                }
            }
        }
        this.data.remove(index);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Group<Object> getAll(){
        Group<Object> returning = new Group<>("na");//name not applicable
        for(Group<Object> grouping: this.data){
            returning.addAll(grouping);
        }
        return returning;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for adding a new credential object to the correct group
     * @param credential represents the credential object to append
     */
    public void newCredential(Object credential, Manager locker){
        locker.lockSingle((Credential) credential,((Credential)credential).encrypted);

        Credential reference = (Credential) credential;
        String selected;
        if(reference.group==null){
            reference.group = "default";
        }
        int groupNum = indexOfGroup(reference.group);
        this.data.get(groupNum).add(credential);

        if(credential instanceof UserFile){
            new FilePersistance().saveEncryptedFile((UserFile) credential);
        }



        locker.lockSingle((Credential) credential,((Credential)credential).encrypted);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for removing a specific credential object from the wrapper
     * @param credential represents the credential object we want to delete
     */
    public void deleteCredential(Object credential, Manager locker){
        Credential reference = (Credential) credential;
        if(reference.group==null){
            reference.group="default";
        }
        int groupNum = indexOfGroup(reference.group);
        if(groupNum==-1){
            groupNum=0;
        }
        if(credential instanceof UserFile){
            new FilePersistance().extractEncryptedFile((UserFile) credential);
        }
        this.data.get(groupNum).remove(credential);


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for fetching all data group names. Used for persistence
     * @return an array of strings equal to wrapper data group names
     */
    public ArrayList<String> getGroupNames(){
        ArrayList<String> names = new ArrayList<>();

        for(int groupNum = 0; groupNum< data.size();groupNum++){
            names.add(this.data.get(groupNum).name);
        }
        return names;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for fetching the index of a group in the data atribute, providing the groups name
     * @param arg represents the search term
     * @return the index of the group, or -1 if no match is found
     */
    private int indexOfGroup(String arg){
        if(arg.equals("default") || arg.equals("null")){
            return 0;
        }
        for(int groupNum=0;groupNum<this.data.size();groupNum++){
            if(this.data.get(groupNum).name.equals(arg)){
                return groupNum;
            }
        }
        return 0;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to move a data object from an origin group to another
     */
    public void migrateGroup(String destination, Object data){
        if(this.data.get(indexOfGroup(destination)).contains(data)){
            return;
        }
        this.data.get(indexOfGroup(destination)).add(data);
        this.data.get(indexOfGroup(((Credential)data).group)).remove(data);
        ((Credential) data).group = destination;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for saving wrapper data
     */
    public void save(){
        this.persistance.saveWrapper();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for loading wrapper data
     */
    public boolean load(){
        return(this.persistance.loadWrapper());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean getState(){
        boolean state = false;
        for(Group<Object> grouping: this.data){
            if(!(grouping.size()==0)){
                state = true;
            }
        }
        return state;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean lock(boolean action){
        if(action){
            for(Group<Object> group: this.data){
                for(Object data: group){
                    if(((Credential)data).encrypted==true){
                        continue;
                    }
                    String[] dataValues =((Credential)data).getData();
                    int counter=0;
                    for(String dataValue: dataValues){
                        dataValue = this.security.encrypt(dataValue);
                        dataValues[counter] = dataValue;
                        counter++;
                    }
                    ((Credential)data).update(dataValues, this.security);
                    ((Credential) data).encrypted=true;
                }
            }
            return true;
        }else{
            for(Group<Object> group: this.data){
                for(Object data: group){
                    if(((Credential)data).encrypted==false){
                        continue;
                    }
                    String[] dataValues =((Credential)data).getData();
                    int counter=0;
                    for(String dataValue: dataValues){
                        dataValue=this.security.decrypt(dataValue);
                        dataValues[counter] = dataValue;
                        counter++;
                    }
                    ((Credential)data).update(dataValues, this.security);
                    ((Credential) data).encrypted=false;
                }
            }
            return true;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getSample(Manager lockbox){
        for(Group<Object> grouping: this.data){
            for(Object data: grouping){
                lockbox.lockSingle((Credential)data,false);
                return ((Credential)data).domain;
            }
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<String> encryptAndGetGroupNames(){
        ArrayList<String> results = new ArrayList<>();
        for(Group<Object> group: this.data){
            String encrypted = this.security.encrypt(group.name);
            results.add(encrypted);
        }
        return results;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Private inner class for encapsulating methods to load and save wrapper data
     */
    private class WrapperPersistence{

        /**
         * Attribute for accessing general loading and saving mechanisms
         */
        private final FileStore storage;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Constructor which assigns directory value to objects file store attribute
         */
        public WrapperPersistence(){
            this.storage = new FileStore(assignSaveDir(), SaveConfigurations.TABLE);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private String assignSaveDir(){
            String userdir = System.getenv("APPDATA");
            switch(type){
                case ACCOUNTS:
                    return userdir+"\\LockData\\Safe\\Creds\\CrMa01.txt";

                case BANKS:
                    return userdir+"\\LockData\\Safe\\Creds\\CrMa02.txt";

                case CODES:
                    return userdir+"\\LockData\\Safe\\Creds\\CrMa03.txt";

                case FILES:
                    return userdir+"\\LockData\\Safe\\Creds\\CrMa04.txt";

                case IMAGES:
                    return userdir+"\\LockData\\Safe\\Creds\\CrMa05.txt";

                case NOTES:
                    return userdir+"\\LockData\\Safe\\Creds\\CrMa06.txt";


            }
            return "";
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Public method for saving wrapper data to its correct location, with every line of the file representing a
         * grouping
         */
        public boolean saveWrapper(){
            ArrayList<String> decompiled = new ArrayList<>();
            String groupline="";
            if(data.size()==0){
                return false;
            }
            decompiled.add(concatGroupNames());

            for(int g=0; g<data.size();g++){//for every group
                groupline = "";//reset group line storage
                if(data.get(g).size()==0){
                    groupline="empty";
                    decompiled.add(groupline);
                    continue;
                }
                for(int c=0; c<data.get(g).size();c++){
                    groupline+=data.get(g).get(c).toString();
                }
                decompiled.add(groupline);
            }
            String result = "";
            for(String decompile: decompiled){
                result+=decompile+"////--////";
            }
            this.storage.saveLine(result);
            return true;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Public method for retreving credential data from a file
         */
        public boolean loadWrapper(){
            String fetched = this.storage.loadLine();
            String[] decompiled;

            if(fetched==null || fetched.equals("")){
                data.add(new Group<>("default"));
                return false;
            }
            decompiled = fetched.split("////--////");

            if(decompiled.length<1 || decompiled[0]==null){
                data.add(new Group<>("default"));
                return false;
            }

            Group<Object> compiled;
            String[] groupNames = decompiled[0].split("/--/");
            int internalCount=0;

            for(int groupCounter=1;groupCounter<=groupNames.length;groupCounter++){//for every group
                if(groupCounter>groupNames.length){
                    break;
                }
                compiled = new Group<>(groupNames[internalCount]);
                try{
                    if(decompiled[groupCounter].equals("empty")){
                        data.add(compiled);
                        internalCount++;
                        continue;
                    }
                }catch(ArrayIndexOutOfBoundsException a){//empty group
                    data.add(compiled);
                    internalCount++;
                    continue;
                }
                if(decompiled[groupCounter]==null || decompiled[groupCounter] == ""){
                    return false;
                }
                String[] groupSplit=decompiled[groupCounter].split("//--//");


                for(int x=0;x<groupSplit.length;x++){
                    compiled.add(compileData(groupSplit[x]));
                }
                data.add(compiled);
                internalCount++;

            }
            return true;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Private method used in loading to instantiate the correct credential object type
         * @param strung represents the string notation of a credential object we wish to recompile
         * @return the correct credential object with the associated data
         */
        private Object compileData(String strung){
            switch(type){
                case ACCOUNTS:
                    Account generated = new Account(strung);
                    generated.encrypted=true;
                    return (Object) generated;

                case BANKS:
                    BankCard bgenerated = new BankCard(strung);
                    bgenerated.encrypted=true;
                    return (Object) bgenerated;

                case CODES:
                    Code cgenerated = new Code(strung);
                    cgenerated.encrypted=true;
                    return (Object) cgenerated;

                case FILES:
                    UserFile fgenerated = new UserFile(strung);
                    fgenerated.encrypted=true;
                    return (Object) fgenerated;

                case IMAGES:
                    UserFile igenerated = new UserFile(strung);
                    igenerated.encrypted=true;
                    return (Object) igenerated;

                case NOTES:
                    Note ngenerated = new Note(strung);
                    ngenerated.encrypted=true;
                    return (Object) ngenerated;
            }
            return null;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Private method used in saving to concat groupnames into a single string, including a seperator character sequence
         * to differentiate each name
         * @return a correctly formatted string
         */
        private String concatGroupNames(){
            String result = "";
            ArrayList<String> names = encryptAndGetGroupNames();
            for(String name: names){
                result+=name+"/--/";
            };
            return result;
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class FilePersistance{
        private transient FileStore fileStorage;//attribute for saving encrypted files
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void saveEncryptedFile(UserFile file){
            fileStorage = new FileStore(file.getFilePath(),SaveConfigurations.BYTE);
            byte[] data = fileStorage.loadBytes();
            data = Base64.getEncoder().encode(data);
            data = security.encryptBytes(data);
            File oldFile = new File(file.getFilePath());//create representation of old file
            oldFile.delete();//delete old file
            File newFile = new File(file.generateInternalPath());
            newFile.getParentFile().mkdirs();
            try{
                newFile.createNewFile();
            }catch(IOException i){

            }
            fileStorage = new FileStore(file.generateInternalPath(),SaveConfigurations.BYTE);
            fileStorage.saveBytes(data);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void extractEncryptedFile(UserFile file){

            File inputFile = new File(file.generateInternalPath());
            fileStorage = new FileStore(file.generateInternalPath(),SaveConfigurations.BYTE);
            File checkexistance = new File(file.generateInternalPath());
            if(!(checkexistance.exists())){
                return;
            }
            byte[] data = fileStorage.loadBytes();
            data = security.decryptBytes(data);
            data = Base64.getDecoder().decode(data);
            File outputFile;
            if(file.destination==null){
                String userD = System.getProperty("user.home");
                outputFile = new File(userD+"\\Downloads\\"+file.getExternalName());
            }else{
                outputFile = new File(file.destination+"\\"+file.getExternalName());
            }
            outputFile.getParentFile().mkdirs();
            try{
                FileOutputStream output;
                outputFile.createNewFile();
                output = new FileOutputStream(outputFile);
                output.write(data);
                output.close();
                inputFile.delete();//delete encrypted file after extraction
            }catch(Exception e){
                System.out.println(e);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
