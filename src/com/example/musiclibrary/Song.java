package com.example.musiclibrary;

import java.io.File;
import java.net.URI;
import java.util.Date;

import android.media.MediaMetadataRetriever;
import android.util.Log;

public class Song extends MediaMetadataRetriever {
	
	public boolean hasAudio;
	
	public String title;
	public String artist;
	public String album;
	public String albumArtist;
	public String trackNumber;
	public String remixer;
	public String producer;
	public String featuring;
	public String genre;
	public String rating;
	public String year;
	public String filetype;
	public String duration;
	public String lyrics;
	public String fileSize;
	public String lastPlayed;
	public String dateAdded;
	public String playCount;
	public String path;
	// private ??? albumArt;

	private File musicFile;

	public Song() {
	} // Still needs to be prepared

	public Song(File file) { // completes the preparation
		setSource(file);
		initialize();
		path = file.getAbsolutePath();
	}

	public Song(String fileString) {
		File file = new File(fileString);
		setSource(file);
		initialize();
		path = fileString;
	}
	
	private void initialize(){
		hasAudio = getHasAudio(); // id3
		title = getTitle(); // id3
		artist = getArtist(); // id3
		album = getAlbum(); // id3
		albumArtist = getAlbumArtist(); // id3
		trackNumber = getTrackNumber(); // id3
		remixer = getRemixer(); // database or title
		producer = getProducer(); // database or title
		featuring = getFeaturing(); // database or title
		genre = getGenre(); // id3
		rating = getRating(); // database
		year = getYear(); // id3
		filetype = getFiletype(); // id3
		duration = getDuration(); // id3
		lyrics = getLyrics(); // ????
		fileSize = getFileSize(); // file
		lastPlayed = getLastPlayed(); // database
		dateAdded = getDateAdded(); // id3
		playCount = getPlayCount(); // database
	}

	public void setSource(File file) {
		musicFile = file;
		try {
			setDataSource(file.getAbsolutePath());
		} catch (IllegalArgumentException e) {
			Log.d(this.toString(), file.getAbsolutePath());
			e.printStackTrace();
		}
	}

	public Boolean getHasAudio() {
		if (this.extractMetadata(METADATA_KEY_HAS_AUDIO) != null) {
			return true;
		} else {
			return false;
		}
	}

	// for testing only*****************
	public Boolean getHasVideo() {
		if (this.extractMetadata(METADATA_KEY_HAS_VIDEO) != null) {
			return true;
		} else {
			return false;
		}
	}

	// TODO build in ability to write to meta data
	private String getFiletype() {
		return this.extractMetadata(METADATA_KEY_MIMETYPE);
	}

	private String getAlbum() {
		return this.extractMetadata(METADATA_KEY_ALBUM);
	}

	private String getArtist() {
		return this.extractMetadata(METADATA_KEY_ARTIST);
	}

	private String getTitle() {
		return this.extractMetadata(METADATA_KEY_TITLE);
	}

	private String getAlbumArtist() {
		return this.extractMetadata(METADATA_KEY_ALBUMARTIST);
	}

	private String getDateAdded() {
		return this.extractMetadata(METADATA_KEY_DATE);
	}

	private String getYear() {

		return this.extractMetadata(METADATA_KEY_YEAR);
	}

	private String getTrackNumber() {
		return this.extractMetadata(METADATA_KEY_CD_TRACK_NUMBER);
	}

	private String getDuration() {
		return this.extractMetadata(METADATA_KEY_DURATION);
	}

	private String getGenre() {
		return this.extractMetadata(METADATA_KEY_GENRE);
	}

	private String getPlayCount() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getLastPlayed() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getFileSize() {
		return String.valueOf(musicFile.length());
	}

	private String getLyrics() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getRating() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getFeaturing() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProducer() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getRemixer() {
		// TODO Auto-generated method stub
		return null;
	}

}
