package com.waves.ui;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.musiclibrary.R;
import com.waves.library.DatabaseHandler;
import com.waves.library.Song;

public class SongList extends ListActivity {
	
	private static final String[] musicFileTypes = { ".mp3" }; //, ".flac", ".wav", ".aac" };
	private Song song; // define at the beginning to prevent memory leaks
	private DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_library_main);
		
		dbHandler = new DatabaseHandler(this.getApplicationContext());
		fullScan(new File("/storage/extSdCard/Music"));
		
		// TODO figure this out
		this.setListAdapter(new CursorAdapter(this,
				dbHandler.getTagList(DatabaseHandler.KEY_TITLE),
				DatabaseHandler.KEY_TITLE, false));		
		ListView lv = getListView();
		

		lv.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	              int position, long id) {
	        	  // Launch new activity upon selection
	          }
	        });


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_library_main, menu);
		return true;
	}

	private void fullScan(File startDir) {

		for (final File fileEntry : startDir.listFiles()) {
			if (fileEntry.isDirectory()) {
				// Log.d(this.loopDebug, fileEntry.toString());
				fullScan(fileEntry);
			} else {
				if (matchesExtension(fileEntry.getName(), musicFileTypes)) {
					Log.d("loopDebug", fileEntry.getPath());
					song.setSource(fileEntry);
					dbHandler.addSong(song);
				}
			}
		}
	}
	
	private boolean matchesExtension(String input, String[] items) {
		Pattern p;
		Matcher m;

		input = input.substring(input.lastIndexOf('.'));

		for (int i = 0; i < items.length; i++) {
			p = Pattern.compile(items[i], Pattern.CASE_INSENSITIVE);
			m = p.matcher(input);
			if (m.matches())
				return m.matches();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		song.release();
	}

}
