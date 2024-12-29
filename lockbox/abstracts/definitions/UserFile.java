package org.vigilance.lockbox.abstracts.definitions;


import org.vigilance.lockbox.abstracts.Secure;
import org.vigilance.lockbox.abstracts.enums.DataType;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.vigilance.lockbox.abstracts.enums.DataType.FILES;
import static org.vigilance.lockbox.abstracts.enums.DataType.IMAGES;

public class UserFile extends Credential{

    /**
     * Attribute to hold files internal type (image, or file)
     */
    public DataType dataType;

    /**
     * Attribute to hold files type/extension
     */
    private String fileType;

    /**
     * Attribute to hold path. If
     */
    private String filePath;

    private String creationDate;

    public String getFileType() {
        return fileType;
    }

    public transient String destination;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserFile(String data){
        fromNotation(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor used for loading encrypted account.
     * @param name
     * @param group
     * @param type
     */
    public UserFile(String name, String group, String filePath, DataType type){
        this.domain = name;
        if(!(group==null)){
            this.group = group;
        }
        if(group==null){
            this.group="default";
        }
        this.dataType = type;
        String[] split= filePath.split("\\.");
        this.fileType = split[split.length-1];
        this.filePath=filePath;
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'-'HH:mm:ss");
        this.creationDate= format.format(time);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getFilePath(){
        return this.filePath;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to update file data, such as name or user defined grouping, no update is performed if the attributes
     * associated index is null
     * @param data represents an array of either null,string elements or a mix of the two (null represents no change in an attribute)
     */
    @Override
    public void update(String[] data, Secure security){
        if(!(data[0]==null)){
            this.domain = data[0];
        }
        if(!(data[1]==null)){
            if(!(data[1].equals(IMAGES) || data[1].equals(FILES))){
                this.dataType=dataType.valueOf(security.decrypt(data[1]));
            }else{
                this.dataType = DataType.valueOf(data[1]);
            }
        }
        if(!(data[2]==null)){
            this.group = data[2];
        }
        if(!(data[3]==null)){
            this.fileType = data[3];
        }
        if(!(data[4]==null)){
            this.filePath = data[4];
        }
        if(!(data[5]==null)){
            this.creationDate=data[5];
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Deterministic method to generate a name for a file that will be encrypted and stored to the application internally
     * @return the encrypted files path
     */
    public String generateInternalPath(){
        String result=System.getenv("APPDATA");//get application absolute path
        result+= "/LockData/Safe/UserF/";
        result+=genInternalName();
        return result;


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String genInternalName(){
        try{

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String hashable=this.domain+this.creationDate;
            byte[] data = digest.digest(hashable.getBytes(StandardCharsets.UTF_8));
            BigInteger value = new BigInteger(1,data);
            StringBuilder builder = new StringBuilder(value.toString());

            while(builder.length()<64){
                builder.insert(0,'0');
            }
            builder.append(".bin");
            return builder.toString();

        }catch(Exception e){

        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setToInternalPath(){
        this.filePath = generateInternalPath();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        String[] data = getData();
        return (data[0]+"/--/"+data[1]+"/--/"+data[2]+"/--/"+data[3]+"/--/"+data[4]+ "/--/"+data[5]+"//--//");


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void fromNotation(String datas){
        String[] data = datas.split("/--/");
        this.domain = data[0];
        this.dataType = DataType.valueOf(data[1]);
        this.fileType = data[3];
        this.filePath = data[4];
        this.creationDate=data[5];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    @Override
    public String[] getData(){
        String[] result = {this.domain,this.dataType.getNotation(),this.group,this.fileType,this.filePath,this.creationDate};
        return (result);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String toDialog(){
        return this.domain+"\n"
                +this.fileType;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getExternalName() {
        return this.domain+"."+this.fileType;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
