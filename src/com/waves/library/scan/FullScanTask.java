package com.waves.library.scan;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import com.waves.library.utils.DbUtils;

public class FullScanTask extends FileScanService implements Runnable {
	/**
	 * Start a bound service. This service will do a full scan and send a
	 * message back to the main thread each time an entry in the database is
	 * changed or an entry is added.
	 */
	public static String FULL_SCAN_BOUND_INTENT = new String(
			"com.waves.library.scan.FULL_SCAN_BOUND_INTENT");
	/**
	 * Start an unbound service. The service is meant to run in this mode when
	 * the user is not looking at the library. This does not send messages back
	 * to the main thread until it is finished, and will be faster than the
	 * bound version.
	 */
	public static String FULL_SCAN_UNBOUND_INTENT = new String(
			"com.waves.library.scan.FULL_SCAN_UNBOUND_INTENT");
	/**
	 * Scan or rescan a single file into the database.
	 */
	public static String SINGLE_FILE_INTENT = new String(
			"com.waves.library.scan.SINGLE_FILE_INTENT");

	private File _startDir;

	public AudioFile song;

	FullScanTask(File startDir) {
		_startDir = startDir;
	}

	@Override
	public void run() {
		try {
			fullScan(_startDir);
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

	private void fullScan(File startDir) throws CannotReadException,
			IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {

		for (final File file : startDir.listFiles()) {
			int listLength = 0;
			String[] musicFileTypes = { "mp3" };
			if (listLength > 100) {
				stopSelf();
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
				if (DbUtils.matchesExtension(file.getName(), musicFileTypes)) {
					listLength++;
					song = AudioFileIO.read(file);
					dbHandler.addSong(file, song.getTag(),
							song.getAudioHeader());
				}
			}
		}
	}

}
