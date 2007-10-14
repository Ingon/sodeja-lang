package org.sodeja.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
}
