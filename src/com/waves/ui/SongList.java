package com.waves.ui;

import java.io.File;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.musiclibrary.R;
import com.waves.library.DatabaseHandler;
import com.waves.library.PopulateLibrary;
import com.waves.library.Song;

public class SongList extends ListActivity {
	TextView filePath, textAlbum, textArtist, textTitle;
	String loopDebug = "loopDebug";
	String loopTimer = "loopTimer";
	Song meta; // define at the beginning to prevent memory leaks

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_library_main);
		
		DatabaseHandler dbh;
		Cursor c;
		Context xt;
		CursorAdapter cad = null;
		
		// TODO full database to list
		SqliteDatabase db = DatabaseHandler()
		
		this.setListAdapter(cad);		


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
