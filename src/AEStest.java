import java.io.File;

public class AEStest {
	
	
	
	public static void main(String[] args) throws Exception {

		File myFile = new File("creds.json");
		//instantiate encryption class
		AES_file_encryption my_encrypt = new AES_file_encryption("ka","password123");

		//instantiate JSON classes
		JSON_Reader J_read = new JSON_Reader();
		JSON_Writer J_write = new JSON_Writer();

		//writing into "creds.json" file  - return void
		J_write.addEntry("www.github.com","username","password123");
		J_write.export("creds");


		//reading "creds.json" file
		J_read.read("creds.json");   // - return ArrayList

		//call encryption on "creds.json"
		my_encrypt.encrypt(myFile);  //return true if success else false
		my_encrypt.decrypt(myFile);  //return true if success else false



		//Some extra testing below - IGNORE this
		/*
		//byte[] current = my_encrypt.getByteIv();
		//System.out.println(current);

		String str = "[B@30b8a058";
		my_encrypt.setIV(str.getBytes());
		System.out.println(my_encrypt.getByteIv());
		my_encrypt.decrypt(myFile);
		*/

	}
}