package com.waves.library;

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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Starts scanning for audio files to add to database. Scans a specified folder,
 * entire file system, or single file.
 */
public class FileScanService extends Service {
	public AudioFile song;
	public DatabaseHandler dbHandler;

	public FileScanService() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			public void run() {
				try {
					fullScan(new File("/storage/extSdCard/Music"));
				} catch (CannotReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadOnlyFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidAudioFrameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

		return 0;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	// File scanner (Maybe should become its own class)
	private void fullScan(File startDir) throws CannotReadException,
			IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {

		for (final File file : startDir.listFiles()) {
			int listLength = 0;
			String[] musicFileTypes = { "mp3" };
			if (listLength > 100) {
				return;
			} else if (file.isDirectory()) {
				// Log.d(this.loopDebug, fileEntry.toString());
				fullScan(file);
			} else {
				/*
				 * TODO filter for MIME type. We don't want unplayables sneaking
				 * in give a warning for unplayables maybe? this could do a
				 * preliminary scan and allow others to be caught with
				 * exceptions
				 */
				if (matchesExtension(file.getName(), musicFileTypes)) {
					listLength++;
					song = AudioFileIO.read(file);
					dbHandler.addSong(file, song.getTag(),
							song.getAudioHeader());
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
}
