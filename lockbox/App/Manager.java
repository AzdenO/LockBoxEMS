package org.vigilance.lockbox.App;
import javafx.scene.control.Button;
import javafx.util.Pair;
import org.vigilance.lockbox.abstracts.Secure;
import org.vigilance.lockbox.abstracts.definitions.*;
import org.vigilance.lockbox.abstracts.enums.DataType;
import org.vigilance.lockbox.abstracts.wrappers.CredentialWrapper;
import org.vigilance.lockbox.datapersistance.FileStore;
import org.vigilance.lockbox.datapersistance.SaveConfigurations;
import org.vigilance.lockbox.gui.DataButton;
import org.vigilance.lockbox.organisation.Group;
import org.vigilance.lockbox.utils.configs.Configuration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import static org.vigilance.lockbox.abstracts.enums.DataType.*;

/**
 * Java class to encapsulate a credential manager service, providing all functionality necessary in a single object to be
 * used in conjunction with front-end implementation
 * @author Declan R.A Wadsworth
 * @version 0.1
 */
public class Manager {
    public Secure security = new Secure();
    public CredentialWrapper<Account> accounts;
    public CredentialWrapper<BankCard> banks;
    public CredentialWrapper<Code> codes;
    public CredentialWrapper<UserFile> files;
    public CredentialWrapper<UserFile> images;

    public CredentialWrapper<Note> notes;
    public Configuration config;
    public boolean verified = false;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor that only fetches log information, initialisation of attributes occurs after password verification
     */
    public Manager(){
        this.config = new Configuration(this.security);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void extensionManager(){

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newGroup(DataType type, String group){
        switch(type){
            case ACCOUNTS:
                this.accounts.newGroup(group);
                break;

            case CODES:
                this.codes.newGroup(group);
                break;

            case BANKS:
                this.banks.newGroup(group);
                break;

            case FILES:
                this.files.newGroup(group);
                break;

            case IMAGES:
                this.images.newGroup(group);
                break;

            case NOTES:
                this.notes.newGroup(group);
                break;

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteGroup(String removing, DataType type, boolean keepData){
        switch(type){
            case ACCOUNTS:
                this.accounts.deleteGroup(removing,keepData,this);
                break;

            case CODES:
                this.codes.deleteGroup(removing,keepData,this);
                break;
            case BANKS:
                this.banks.deleteGroup(removing,keepData,this);
                break;
            case FILES:
                this.files.deleteGroup(removing,keepData,this);
                break;
            case IMAGES:
                this.images.deleteGroup(removing,keepData,this);
                break;
            case NOTES:
                this.notes.deleteGroup(removing, keepData,this);
                break;
        }
        save();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean loadWrapper(){
        this.accounts = new CredentialWrapper<>(this.security, ACCOUNTS);
        if(!this.accounts.load()){
            //this.accounts.newGroup("default");
        }

        this.banks = new CredentialWrapper<>(this.security, BANKS);
        if(!this.banks.load()){
            //this.banks.newGroup("default");
        }
        this.codes = new CredentialWrapper<>(this.security, CODES);
        if(!(this.codes.load())){
            //this.codes.newGroup("default");
        }
        this.files = new CredentialWrapper<>(this.security, FILES);
        if(!(this.files.load())){
            //this.files.newGroup("default");
        }
        this.images = new CredentialWrapper<>(this.security, IMAGES);
        if(!(this.images.load())){
            //this.images.newGroup("default");
        }
        this.notes = new CredentialWrapper<>(this.security,NOTES);
        if(!(this.notes.load())){
            //ghhghg
        }





        return true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteData(DataType type, Object data){
        lockSingle((Credential) data,true);
        switch(type){
            case ACCOUNTS:
                this.accounts.deleteCredential((Account) data,this);
                break;

            case CODES:
                this.codes.deleteCredential((Code) data,this);
                break;

            case BANKS:
                this.banks.deleteCredential((BankCard) data,this);
                break;

            case FILES:
                this.files.deleteCredential((UserFile) data,this);
                break;

            case IMAGES:
                this.images.deleteCredential((UserFile) data,this);
                break;

            case NOTES:
                this.notes.deleteCredential((Note) data, this);
                break;

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<String> groupNamesFromType(DataType type){
        switch(type){
            case FILES:
                return files.getGroupNames();

            case IMAGES:
                return images.getGroupNames();

            case ACCOUNTS:
                return accounts.getGroupNames();

            case CODES:
                return  codes.getGroupNames();

            case BANKS:
                return banks.getGroupNames();

            case NOTES:
                return notes.getGroupNames();

        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void migrateData(String groupDest, Object data, DataType type){

        switch(type){
            case ACCOUNTS:
                this.accounts.migrateGroup(groupDest,(Account) data);
                break;

            case CODES:
                this.codes.migrateGroup(groupDest,(Code) data);
                break;

            case BANKS:
                this.banks.migrateGroup(groupDest,(BankCard) data);
                break;

            case FILES:
                this.files.migrateGroup(groupDest,(UserFile) data);
                break;

            case IMAGES:
                this.images.migrateGroup(groupDest,(UserFile) data);
                break;

            case NOTES:
                this.notes.migrateGroup(groupDest,(Note) data);
                break;

        }
        lockSingle((Credential) data,false);
        save();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void save(){
        lockAll(true);
        this.accounts.save();
        this.banks.save();
        this.codes.save();
        this.files.save();
        this.images.save();
        this.notes.save();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<String> getGroups(DataType type){
        ArrayList<String> groups=new ArrayList<>();
        switch(type){
            case ACCOUNTS:
                return this.accounts.getGroupNames();

            case CODES:
                return this.codes.getGroupNames();

            case BANKS:
                return this.banks.getGroupNames();

            case FILES:
                return this.files.getGroupNames();

            case IMAGES:
                return this.images.getGroupNames();

            case NOTES:
                return this.notes.getGroupNames();

        }
        return groups;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Credential> search(String arg){
        SearchEngine searcher = new SearchEngine();
        return searcher.searchAll(arg);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addGroup(String newGroup, DataType type){
        switch(type){
            case ACCOUNTS:
                this.accounts.data.add(new Group<>(newGroup));
                break;

            case BANKS:
                this.banks.data.add(new Group<>(newGroup));
                break;

            case CODES:
                this.codes.data.add(new Group<>(newGroup));
                break;

            case FILES:
                this.files.data.add(new Group<>(newGroup));
                break;

            case IMAGES:
                this.images.data.add(new Group<>(newGroup));
                break;

            case NOTES:
                this.notes.data.add(new Group<>(newGroup));
                break;

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean verify(String password){
        String sample="";
        if(this.accounts.getState()){
            sample = this.accounts.getSample(this);
        }else if(this.banks.getState()){
            sample = this.banks.getSample(this);
        }else if(this.codes.getState()){
            sample = this.codes.getSample(this);
        }else if(this.files.getState()){
            sample = this.files.getSample(this);
        }else if(this.images.getState()){
            sample = this.images.getSample(this);
        }else{
            sample = this.notes.getSample(this);
        }
        if(security.verify(password, sample)){
            this.verified=true;
            return true;
        }else{
            return false;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void lockAll(boolean action){
        this.accounts.lock(action);
        this.banks.lock(action);
        this.codes.lock(action);
        this.files.lock(action);
        this.images.lock(action);
        this.notes.lock(action);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void decryptAllGroupNames(){
        this.accounts.decryptGroupNames();
        this.codes.decryptGroupNames();
        this.banks.decryptGroupNames();
        this.files.decryptGroupNames();
        this.images.decryptGroupNames();
        this.notes.decryptGroupNames();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void lockFromCollection(ArrayList<Button> buttons, boolean action){
        for(Button button: buttons){
            lockSingle((((Credential)((DataButton<Object>) button).data)), action);
            button.setText(((Credential)((DataButton<Object>) button).data).toDialog());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean initialisationCheck(){

        if(!(checkSaveDirExists())){
            File saveDir = new File(System.getenv("APPDATA")+"\\LockData\\Safe");
            saveDir.mkdirs();
            File tableDir = new File(System.getenv("APPDATA")+"\\LockData\\Safe\\Creds");
            tableDir.mkdirs();
            File fileDir = new File(System.getenv("APPDATA")+"\\LockData\\Safe\\UserF");
            fileDir.mkdirs();
            return false;
        }

        if(!this.accounts.getState()&&!this.codes.getState()&&!this.banks.getState()&&!this.files.getState()&&!this.images.getState()&&!this.notes.getState()){
            return false;
        }
        this.config.loadLogs();
        return  true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean checkSaveDirExists(){
        Path saveDirCheck = Paths.get(System.getenv("APPDATA")+"\\LockData");
        return Files.exists(saveDirCheck);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Credential lockSingle(Credential data, boolean action){
        if(data==null){
            return data;
        }
        if(!action){
            if(data.encrypted){
                return data;
            }
            String[] dataValues = data.getData();
            int counter = 0;
            for(String value: dataValues){
                value = this.security.encrypt(value);
                dataValues[counter] = value;
                counter++;
            }
            data.encrypted=true;
            data.update(dataValues, this.security);
        }else{
            if(!data.encrypted){
                return data;
            }
            String[] dataValues = data.getData();
            int counter = 0;
            for(String value: dataValues){
                value = this.security.decrypt(value);
                dataValues[counter] = value;
                counter++;
            }
            data.encrypted=false;
            data.update(dataValues, this.security);
        }
        return  data;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newPassword(String password, boolean init){
        lockAll(false);
        decryptFilesInPlace();
        this.security.newPassword(password);
        lockAll(true);
        encryptFilesInPlace();
        config.saveLogs();
        if(init){
            save();
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void decryptFilesInPlace(){
        //////////////////////////////////////////////
        File userFPath = new File(System.getenv("APPDATA")+"/LockData/Safe/UserF");
        File[] contents = userFPath.listFiles();
        for(File enc: contents){
            byte[] loadedData = new FileStore(enc.getAbsolutePath(), SaveConfigurations.BYTE).loadBytes();
            loadedData = security.decryptBytes(loadedData);
            loadedData = Base64.getDecoder().decode(loadedData);
            new FileStore(enc.getAbsolutePath(),SaveConfigurations.BYTE).saveBytes(loadedData);
        }
        //////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void encryptFilesInPlace(){
        File userFPath = new File(System.getenv("APPDATA")+"/LockData/Safe/UserF");
        File[] contents = userFPath.listFiles();
        for(File enc: contents){
            byte[] loadedData = new FileStore(enc.getAbsolutePath(), SaveConfigurations.BYTE).loadBytes();
            loadedData = Base64.getEncoder().encode(loadedData);
            loadedData = security.encryptBytes(loadedData);
            new FileStore(enc.getAbsolutePath(),SaveConfigurations.BYTE).saveBytes(loadedData);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void exit(){
        lockAll(true);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Private inner class used to perform searches of user data. The search works through a process named sequence
     * matching; in which every character of the search term is checked against characters in the currently searched data
     * object domain value. If no match is found then capitalisation of the search term character is inverted.
     *
     * The highest number of matching characters in a row is called a match value. Any data with a match value greater than
     * 0 (1 is counted as a match as the first letter in a sequence does not contribute to the match value) is added as
     * a pair with its value. The results are sorted to have data with the highest match values placed in the front
     * of the results array, in descending order from there.
     */
    private class SearchEngine{
        private ArrayList<Pair<Integer,Credential>> results = new ArrayList<>();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public ArrayList<Credential> searchAll(String arg){
            if(arg.equals("") || arg==null){
                return null;
            }
            results.clear();
            ArrayList<Credential> data = new ArrayList<>();
            data.addAll(accounts.getAll());
            for(Credential dataObject: data){
                lockSingle(dataObject, true);
                sequenceMatcher(arg, dataObject);
            }
            data.clear();
            data.addAll(banks.getAll());
            for(Credential dataObject: data){
                lockSingle(dataObject, true);
                sequenceMatcher(arg, dataObject);
            }
            data.clear();
            data.addAll(codes.getAll());
            for(Credential dataObject: data){
                lockSingle(dataObject, true);
                sequenceMatcher(arg, dataObject);
            }
            data.clear();
            data.addAll(files.getAll());
            for(Credential dataObject: data){
                lockSingle(dataObject, true);
                sequenceMatcher(arg, dataObject);
            }
            data.clear();
            data.addAll(images.getAll());
            for(Credential dataObject: data){
                lockSingle(dataObject, true);
                sequenceMatcher(arg, dataObject);
            }
            data.clear();
            data.addAll(notes.getAll());
            for(Credential dataObject: data){
                lockSingle(dataObject, true);
                sequenceMatcher(arg, dataObject);
            }
            return sortByMatchValue(retrieveUpperBound());



        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void sequenceMatcher(String arg, org.vigilance.lockbox.abstracts.definitions.Credential data){
            String currentElementTerm = data.domain;
            int stoppingCondition = new Utils().lowestLength(arg, currentElementTerm)-1;
            char[] term = arg.toCharArray();
            char[] currentElement = currentElementTerm.toCharArray();
            int matchValue = sequenceProgresser(0,stoppingCondition,term,currentElement,0, false);
            if(matchValue>0){
                results.add(new Pair<>(matchValue,data));
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private int sequenceProgresser(int currentChar,int endChar ,char[] term, char[] currentElement, int matchValue, boolean previous){
            if(currentChar==endChar){
                return matchValue;
            }

            if(term[currentChar]==currentElement[currentChar] && previous){
                matchValue+=1;
            }else if(term[currentChar]==currentElement[currentChar] && !previous){
                previous=true;
            }

            if(!(term[currentChar]==currentElement[currentChar])){
                char current = term[currentChar];
                if(Character.isLowerCase(current)){
                    current = Character.toUpperCase(current);
                }else{
                    current = Character.toLowerCase(current);
                }

                if(current==currentElement[currentChar] && previous){
                    matchValue+=1;
                }else if(current==currentElement[currentChar] && !previous){
                    previous=true;
                }else{
                    previous=false;
                }
            }

            if(currentChar==endChar){
                return matchValue;
            }else{
                currentChar++;
                matchValue=sequenceProgresser(currentChar,endChar,term,currentElement,matchValue,previous);
            }
            return matchValue;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private ArrayList<Credential> sortByMatchValue(int upperBound){
            ArrayList<Credential> result = new ArrayList<>();
            while(upperBound>0){
                for(Pair<Integer,Credential> pair: results){
                    if(pair.getKey()==upperBound){
                        result.add(pair.getValue());
                    }
                }
                upperBound--;
            }
            return  result;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private int retrieveUpperBound(){
            int upperBound=0;
            for(Pair<Integer, Credential> pair: results){
                if(pair.getKey()>upperBound){
                    upperBound = pair.getKey();
                }
            }
            return  upperBound;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class Utils{
        public int lowestLength(String term1, String term2){
            if(term1.length()<term2.length()){
                return  term1.length();
            }else{
                return term2.length();
            }
        }

        public DataType classAssignment(Credential data){
            if(data instanceof Account){
                return ACCOUNTS;
            }else if(data instanceof BankCard){
                return BANKS;
            }else if(data instanceof Code){
                return CODES;
            }else if(data instanceof UserFile){
                if(((UserFile)data).dataType.equals(FILES)){
                    return FILES;
                }else{
                    return IMAGES;
                }
            }else if(data instanceof Note){
                return NOTES;
            }
            return  DEFAULT;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
