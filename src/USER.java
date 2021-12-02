import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class USER {
    String master_password;
    String login;
    AES_file_encryption encrypt;
    JSON_Writer j_write;
    JSON_Reader j_read;
    File vault;



    public USER(String login, String password){
        this.login = login;
        this.master_password = password;
        this.encrypt = new AES_file_encryption(login,password);
        this.j_write = new JSON_Writer();
        this.j_read = new JSON_Reader();
        this.vault = null;

    }

    public USER(){
        this.j_write = new JSON_Writer();
        this.j_read = new JSON_Reader();
    }
    //store login and master password into a file
    public boolean storeUser(){
        byte[] iv = encrypt.getByteIv();
        j_write.initANDstoreUser(this.login,iv);
        return true;
    }
    //read from login database and find any existing user login/matching password
    public String lookupUser() {
        ArrayList ar = null;
        try {
            ar = j_read.read("master_creds.json");
        } catch (Exception e) {
            System.out.println("file not found");
        }

        for (int i = 0; i < ar.size(); i++) {
            // For loop that iterates 3 times--one for each index in the array for each ArrayList index
            for (int j = 0;j < 2;j++){
                //String[] x = ar.get(i);

            }
            // Newline
            System.out.println();
        }

        return "testing phase";
    }
    public void add_database(String website, String web_login, String web_password){

    }
    public void encrypt_database(){
        if (encrypt.encrypt(this.vault) ==true){
            System.out.println("encryption sucessful");
        }else{
            System.out.println("encryption fail");
        }
    }





}
