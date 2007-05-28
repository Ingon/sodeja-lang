package org.sodeja.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileResource implements Resource {

	private File file;

	public FileResource(String file) {
		this(new File(file));
	}

	public FileResource(File file) {
		if (file == null || !file.exists()) {
			throw new ResourceNotFoundException("Resource " + file + " can not be found"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		this.file = file;
	}

	public boolean exists() {
		return file.exists();
	}

	public InputStream getInputStream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException(e);
		}
	}

	public OutputStream getOutputStream() {
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException(e);
		}
	}

	public String getName() {
		return file.getName();
	}

	public String getPath() {
		return file.getAbsolutePath();
	}
}
