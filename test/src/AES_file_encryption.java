import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileWriter;

//TEST COMMIT 333333


//                                    SOURCES AND REFERENCES USE


//            		https://www.novixys.com/blog/java-aes-example/#:~:text=The%20cor
//           		e%20java%20libraries%20provide,just%20the%20core%20java%20API.

//            		https://www.baeldung.com/java-aes-encryption-decryption

//------------------------------------ START OF CODING PROGRAM ----------------------------------------------//

public class AES_file_encryption {
	public static void main(String[] args) throws NoSuchAlgorithmException,
	InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, 
	IOException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		
		byte[] iv = new byte[16];
		SecureRandom srandom = new SecureRandom();
		srandom.nextBytes(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		

		// for now, lets just create a simple, random secret key
		// in the future, we can also load a key and save a key
		KeyGenerator KeyGen= KeyGenerator.getInstance("AES");
		SecretKey skey = KeyGen.generateKey();
		
		File textFile = new File("og.json");
		File encryptFile = new File("creds.json");
		File decryptFile = new File("decrypt.json");
		
		encrypt_file(textFile,ivspec,skey );
		//decrypt_file(encryptFile,decryptFile, skey, ivspec);
		//decrypt_file(encryptFile,decryptFile,skey,ivspec);
		
		
		
		//line in JSON file
		//{"website":"guguru.com","credentials":{"username":"usr","password":"tron82","salt":"bdjeklspa"}}
	}
	
	
	//helper function, given 2 file, source and destination,
	//copy content of source into destination
	private static void copy_file(File source, File destination) throws IOException {
		
		FileInputStream src = new FileInputStream(source);
		FileOutputStream dest = new FileOutputStream(destination);
		
		
		int bytesRead;
		//read the buffer, if reach -1 then we have reach end of file.
		//reading file in 1024 byte chunks, 1 chunk at a 
		while ((bytesRead = src.read()) != -1 ) {
			dest.write(bytesRead);
		}
		src.close();
		dest.close();
		
	}
	

	//a method for encrypting a file
	public static void encrypt_file(File input_file,IvParameterSpec ivspec, SecretKey skey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, InvalidAlgorithmParameterException, 
			IOException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ci.init(Cipher.ENCRYPT_MODE,skey,ivspec);
		
		
		
		FileInputStream file_stream = new FileInputStream(input_file);
		
	
		File tempFile = new File("tempfile.json");
		if (tempFile.createNewFile()) {
			System.out.println("file create success");
		}else {
			System.out.println("error");
		}
		
		FileOutputStream out_file = new FileOutputStream(tempFile);
		
		
		//instead of reading entire file into memory, we break file into buffer, then encrypt those buffer.
		//a byte is 8 bits
		//byte[1024] means a byte array of 1024 byte
		byte[] buffer = new byte[1024];  
		int bytesRead;
		//read the buffer, if reach -1 then we have reach end of file.
		//reading file in 1024 byte chunks, 1 chunk at a 
		while ((bytesRead = file_stream.read(buffer)) != -1 ) {
			
			//crypto Cipher object uses 2 method to encrypt data
			//to encrypt a single chunk of data, simply call doFinal() with the data to encrypt
			//to encrypt multiple blocks of data, call update() for each block of data,
			//the doFinal() for the final chunk
			//source: http://tutorials.jenkov.com/java-cryptography/cipher.html
			
			byte[] output = ci.update(buffer,0,bytesRead);
			out_file.write(output);
		}
		
		byte[] finalBytes = ci.doFinal();
		out_file.write(finalBytes);
		
		file_stream.close();
		out_file.close();
		//process of encryption is now complete
		
		
		//to copy encrypted code into the original file

		FileWriter clear = new FileWriter(input_file);
		clear.flush();
		clear.close();
		
		copy_file(tempFile,input_file);
		
		tempFile.delete();//delete temporary file
		
		
		//an error message will occur when try to open
		
	}

	public static void decrypt_file(File decrypt_file, SecretKey skey, IvParameterSpec iv) throws 
	IllegalBlockSizeException, BadPaddingException, 
	InvalidKeyException, InvalidAlgorithmParameterException,
	NoSuchAlgorithmException, NoSuchPaddingException, IOException {
	
		Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ci.init(Cipher.DECRYPT_MODE,skey,iv);
		
		//to decrypt the file, we don't do the reverse of encryption, because once the file is lock
		//we can't simply open it to decrypt chunk by chunk
		//instead we just use cipher DECRYPT MODE
		
		FileInputStream in_file = new FileInputStream(decrypt_file);
		
		File tempFile = new File("tempfile.json");
		if (tempFile.createNewFile()) {
			System.out.println("file create success");
		}else {
			System.out.println("error");
		}
		
		FileOutputStream out_file = new FileOutputStream(tempFile);
		
		
		
		
		byte[] buffer = new byte[1024];
		int bytesRead;
		//read the buffer, if reach -1 then we have reach end of file.
		//reading file in 1024 byte chunks, 1 chunk at a time
		while ((bytesRead = in_file.read(buffer)) != -1 ) {
			byte[] output = ci.update(buffer,0,bytesRead);
			out_file.write(output);
		}
		
		byte[] finalBytes = ci.doFinal();
		out_file.write(finalBytes);
		
		in_file.close();
		out_file.close();
		//decryption is now complete
		
		
		FileWriter clear = new FileWriter(decrypt_file);
		clear.flush();
		clear.close();
		copy_file(tempFile,decrypt_file);
		tempFile.delete();
		
	}

}