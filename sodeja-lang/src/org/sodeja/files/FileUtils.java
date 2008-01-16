package org.sodeja.files;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class FileUtils {
	public static byte[] readFile(File file) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[16384];
		int szReaded = -1;
		while((szReaded = is.read(buffer)) != -1) {
			out.write(buffer, 0, szReaded);
		}
		
		return out.toByteArray();
	}

	public static String readFully(String file) throws IOException {
		return readFully(new FileReader(file));
	}
	
	public static String readFully(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(reader);
		
		for(String str = br.readLine();str != null;str = br.readLine()) {
			sb.append(str);
			sb.append("\r\n");
		}
		
		return sb.toString();
	}
}
