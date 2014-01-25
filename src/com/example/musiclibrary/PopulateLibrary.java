package com.example.musiclibrary;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public final class PopulateLibrary {
	public static final String[] musicFileTypes = { ".mp3", ".flac", ".wav",
			".aac" };

	private Song meta;
	private File topDirectory;

	public PopulateLibrary() {
	}

	public PopulateLibrary(String file) {
		meta = new Song();
		topDirectory = new File(file);
	}

	public PopulateLibrary(File file) {
		meta = new Song();
		topDirectory = file;
	}

	public void setTopDirectory(String file) {
		// can this cause a memory leak?
		topDirectory = new File(file);
	}

	public void populate() {
		fileLoop(topDirectory);
	}

	private void fileLoop(File startDir) {

		for (final File fileEntry : startDir.listFiles()) {
			if (fileEntry.isDirectory()) {
				// Log.d(this.loopDebug, fileEntry.toString());
				fileLoop(fileEntry);
			} else {
				// create entry in the database:
				// uri or path album_artist artist album title genre time
				// file_type & maybe others
				if (matchesExtension(fileEntry.getName(), musicFileTypes)) {
					Log.d("loopDebug", fileEntry.getPath());
					meta.setSource(fileEntry);
				}
				// Log.d(this.loopDebug,meta.getTitle());
				// return fileEntry;
			}
		}
		// return null;
	}

	private boolean matchesExtension(String input, String[] items) {
		Pattern p;
		Matcher m;
		
		input = input.substring(input.lastIndexOf('.'));
		
		for (int i = 0; i < items.length; i++) {
			p = Pattern.compile(items[i],Pattern.CASE_INSENSITIVE);
			m = p.matcher(input);
			if (m.matches())
				return m.matches();
		}
		return false;
	}

}
