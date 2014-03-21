package com.waves.ui;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import com.example.musiclibrary.R;
import com.waves.library.DatabaseHandler;
import com.waves.library.scan.FileScanService;

public class SongList extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String[] musicFileTypes = { ".mp3" }; //, ".flac", ".wav", ".aac" };
	private AudioFile song; // define at the beginning to prevent memory leaks
	private DatabaseHandler dbHandler;
	private SimpleCursorAdapter adapter;
	private int listLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		listLength = 0;
		
		// Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);
		
        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
        
        String[] fromColumns = { "_id" };
        String[] filter = { "l%" };
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1
        
        Intent fullScanIntent = new Intent(this, FileScanService.class);        
        PendingIntent fullScanpi;
        
		
		// Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        adapter = new SimpleCursorAdapter(this, 
                android.R.layout.simple_list_item_1, 
                dbHandler.getTagList(DatabaseHandler.KEY_ARTIST, filter, true), 
                fromColumns, toViews, 0);
        setListAdapter(adapter);

        dbHandler.close();
		// TODO implement this asynchronously
			
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return null; //new CursorLoader();
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        adapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        adapter.swapCursor(null);
    }

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_library_main, menu);
		return true;
	}

	
	// File scanner (Maybe should become its own class)
	private void fullScan(File startDir) 
			throws CannotReadException, IOException, TagException, 
			ReadOnlyFileException, InvalidAudioFrameException {
		
		for (final File file : startDir.listFiles()) {
			if(listLength > 100) {
				return;
			} else if (file.isDirectory()) {
				// Log.d(this.loopDebug, fileEntry.toString());
				fullScan(file);
			} else {
				if (matchesExtension(file.getName(), musicFileTypes)) {
					listLength++;
					song = AudioFileIO.read(file);
					dbHandler.addSong(file, song.getTag(), song.getAudioHeader());
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

	}

}
