package com.waves.library.scan;

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

import com.waves.library.DatabaseHandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Starts scanning for audio files to add to database. Scans a specified folder,
 * entire file system, or single file. Use {@code getString()} with the desired
 * intent key to launch the service. Use the corresponding "SCHEME" to pass the
 * uri string that goes with the intent eg.:
 * <br><br>
 * {@code new Intent(getString(FULL_SCAN_BOUND), getString(FULL_SCAN_BOUND_SCHEME) + "/storage/emulated/0/music")}
 */
public class FileScanService extends Service {
	/**
	 * Start a bound service. This service will do a full scan and send a
	 * message back to the main thread each time an entry in the database is
	 * changed or an entry is added.
	 */
	public static int FULL_SCAN_BOUND = com.waves.R.string.intent_full_scan_bound;
	/**
	 * Scheme associated with FULL_SCAN_BOUND intent
	 */
	public static int FULL_SCAN_BOUND_SCHEME = com.waves.R.string.scheme_top_dir;
	/**
	 * Start an unbound service. The service is meant to run in this mode when
	 * the user is not looking at the library. This does not send messages back
	 * to the main thread until it is finished, and will be faster than the
	 * bound version.
	 */
	public static int FULL_SCAN_UNBOUND = com.waves.R.string.intent_full_scan_unbound;
	/**
	 * Scheme associated with FULL_SCAN_UNBOUND intent
	 */
	public static int FULL_SCAN_UNBOUND_SCHEME = com.waves.R.string.scheme_top_dir;
	/**
	 * Scan or rescan a single file into the database.
	 */
	public static int SINGLE_FILE = com.waves.R.string.intent_single_file;
	/**
	 * Scheme associated with SINGLE_FILE intent
	 */
	public static int SINGLE_FILE_SCHEME = com.waves.R.string.scheme_file;

	public DatabaseHandler dbHandler;

	public FileScanService() {
		dbHandler = new DatabaseHandler(getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new FullScanTask(new File("/storage/extSdCard/Music")))
				.start();

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
