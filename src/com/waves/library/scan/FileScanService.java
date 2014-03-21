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
 * entire file system, or single file.
 */
public class FileScanService extends Service {
	public DatabaseHandler dbHandler;

	public FileScanService() {
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
