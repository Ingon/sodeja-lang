package org.sodeja.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Resource {
	public String getName();

	public InputStream getInputStream();

	public OutputStream getOutputStream();

	public String getPath();

	public boolean exists();

	public static class Util {
		public static void closeInputStream(InputStream in) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					// consume1
				}
			}
		}

		public static void closeOutputStream(OutputStream out) {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					// consume1
				}
			}
		}

		public static byte[] loadData(Resource resource) throws IOException {
			InputStream is = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] data = new byte[16384];
				is = resource.getInputStream();
				int szRead = -1;
				while ((szRead = is.read(data)) != -1) {
					baos.write(data, 0, szRead);
				}
				return baos.toByteArray();
			} finally {
				closeInputStream(is);
			}
		}
	}
}
