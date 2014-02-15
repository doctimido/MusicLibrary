package com.waves.library;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.musiclibrary.R;

public class MusicLibraryMain extends Activity {
	TextView filePath, textAlbum, textArtist, textTitle;
	String loopDebug = "loopDebug";
	String loopTimer = "loopTimer";
	Song meta; // define at the beginning to prevent memory leaks

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_library_main);

		// use a different path for testing
		meta = new Song();
		File myDir = new File(getString(R.string.music_path));

		long startTime = System.nanoTime();
		PopulateLibrary lib = new PopulateLibrary(myDir);

		lib.populate();
		long stopTime = System.nanoTime();
		Log.d(this.loopTimer, "That took: " + (stopTime - startTime)
				+ " nanoseconds");

		// Log.d(this.loopTimer, "start");
		// fileLoop(myDir);
		// Log.d(this.loopTimer, "stop");

		// File[] fileArray = myDir.listFiles();
		// AudioMetadata meta = new AudioMetadata(fileArray[0]);

		// Log.d(this.toString(),meta.getAlbum());
		// Log.d(this.toString(),meta.getArtist());
		// Log.d(this.toString(),meta.getTitle());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_library_main, menu);
		return true;
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

				meta.setSource(fileEntry);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		meta.release();
	}

}
