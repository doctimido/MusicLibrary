package com.waves.library.scan;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import android.R;

import com.waves.library.utils.DbUtils;

public class FullScanTask extends FileScanService implements Runnable {
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
				 * in. Give a warning for unplayables maybe? this could do a
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
