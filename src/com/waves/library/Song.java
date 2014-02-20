package com.waves.library;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.waves.library.utils.ParseSong;

public class Song extends AudioFile {
	MediaMetadataRetriever aTag = new MediaMetadataRetriever();

//	Do I need all of this??? 
//	public boolean hasAudio;
	public String title; 
//	public String sortTitle; 
	public String artist;
//	public String sortArtist; 
	public String album; 
//	public String albumArtist;
//	public String trackNumber; public String remixer; public String producer;
//	public String featuring; public String genre; public String rating;
//	public String year; public String filetype; public String duration;
//	public String lyrics; public String fileSize; public String lastPlayed;
//	public String dateAdded; public String playCount; public String path;
//	public Bitmap albumArt;
	
	
	public Song() {}
	
	public Song(AudioFile af) {
		setSource(af);
	}

	public Song(File f, AudioHeader audioHeader, Tag tag) {
		super(f, audioHeader, tag);
		setSource(f);
	}

	public Song(String s, AudioHeader audioHeader, Tag tag) {
		super(s, audioHeader, tag);
		setSource(new File(s));
	}
	
	public void setSource(AudioFile af) {
		this.s
	}
	
	// TODO probably get rid of this, I was using it wrong
	public void setSource(File f) {
		// set jaudiotagger source
		this.file = f;
		this.tag = this.createDefaultTag();

		// set MediaMetadataRetriever source
		try {
			aTag.setDataSource(file.getAbsolutePath());
		} catch (IllegalArgumentException e) {
			Log.d(this.toString(), file.getAbsolutePath());
			e.printStackTrace();
		}
		
		// reset values
		artist = "";
		title = "";
		album = "";

		// set up database tags (maybe don't want to do all of this)
		/*
		 * hasAudio = getHasAudio(); // id3 title = getTitle(); // id3 artist =
		 * getArtist(); // id3 album = getAlbum(); // id3 albumArtist =
		 * getAlbumArtist(); // id3 trackNumber = getTrackNumber(); // id3
		 * remixer = getRemixer(); // database or title producer =
		 * getProducer(); // database or title featuring = getFeaturing(); //
		 * database or title genre = getGenre(); // id3 rating = getRating(); //
		 * database year = getYear(); // id3 filetype = getFiletype(); // id3
		 * duration = getDuration(); // id3 lyrics = getLyrics(); // id3
		 * fileSize = getFileSize(); // file lastPlayed = getLastPlayed(); //
		 * database dateAdded = getDateAdded(); // id3 playCount =
		 * getPlayCount(); // database
		 */
	}
	
	public void release() {
		artist = null;
		album = null;
		title = null;
		aTag.release();
	}

	public Boolean getHasAudio() {
		if (aTag.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) != null) {
			return true;
		} else {
			return false;
		}
	}

	// for testing only*****************
	public Boolean getHasVideo() {
		if (aTag.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO) != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getFiletype() {
		return aTag
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
	}

	public String getAlbum() {
		album = tag.getFirst(FieldKey.ALBUM);
		return album;
	}

	public String getSortAlbum() {
		return tag.getFirst(FieldKey.ALBUM_SORT);
	}

	public String getArtist() {
		artist = tag.getFirst(FieldKey.ARTIST);
		return artist;
	}

	public String getSortArtist() {
		return tag.getFirst(FieldKey.ARTIST_SORT);
	}

	public String getTitle() {
		title = tag.getFirst(FieldKey.TITLE);
		return title;
	}

	public String getSortTitle() {
		return tag.getFirst(FieldKey.TITLE_SORT);
	}

	public String getAlbumArtist() {
		return tag.getFirst(FieldKey.ALBUM_ARTIST);
	}

	public String getSortAlbumArtist() {
		return tag.getFirst(FieldKey.ALBUM_ARTIST_SORT);
	}

	public String getDateAdded() {
		return aTag.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
	}

	public String getYear() {
		return tag.getFirst(FieldKey.YEAR);
	}

	public String getTrackNumber() {
		return tag.getFirst(FieldKey.TRACK);
	}

	public String getDuration() {
		return aTag
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
	}

	public String getGenre() {
		return tag.getFirst(FieldKey.GENRE);
	}

	public String getPlayCount() {
		// TODO figure out a way to implement this
		return null;
	}

	public String getLastPlayed() {
		// TODO figure out how to implement this
		return null;
	}

	public String getFileSize() {
		return String.valueOf(file.length());
		// TODO might not really need this
	}

	public String getLyrics() {
		// might not want this in DB...
		return tag.getFirst(FieldKey.LYRICS);
	}

	public String getRating() {
		return tag.getFirst(FieldKey.RATING);
	}

	public String getFeaturing() {
		String s = tag.getFirst(FieldKey.CUSTOM1);
		if (s.isEmpty()) {
			if (title.isEmpty()) {
				getTitle();
			}
			if (artist.isEmpty()) {
				getArtist();
			}
			return ParseSong.featuring(title, artist);
		}
		else {
			return s;
		}
	}

	public String getProducer() {
		return tag.getFirst(FieldKey.PRODUCER);
	}

	public String getRemixer() {
		return tag.getFirst(FieldKey.REMIXER);
	}
	
	public String getPath() {
		return this.file.getPath();
	}
	
	public Bitmap getAlbumArt() {
		return null;
	}
	
	

}
