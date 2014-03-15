package com.waves.library;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.waves.library.utils.DbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

// TODO add playlist support
public class DatabaseHandler extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "musicLibrary";
	
	// Read/Write
	public static final boolean KEY_READABLE = false;
	public static final boolean KEY_WRITEABLE = true;

	// Main library table
	public static final String TABLE_SONGS = "songs";
	public static final String KEY_SONG_ID = "songId";	// makes it easier for cursor loader classes
	public static final String KEY_TITLE = "songTitle";
	public static final String KEY_ARTIST = "artist"; 
	public static final String KEY_ALBUM = "album";
	public static final String KEY_ALBUM_ARTIST = "albumArtist";
	public static final String KEY_TRACK_NUMBER = "trackNumber";
	public static final String KEY_REMIXER = "remixer";
	public static final String KEY_PRODUCER = "producer";
	public static final String KEY_FEATURING = "featuring";
	public static final String KEY_GENRE = "genre";
	public static final String KEY_RATING = "rating"; // out of 5
	public static final String KEY_YEAR = "year"; // date type
	public static final String KEY_FILETYPE = "fileType";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_LYRICS = "lyrics";
	public static final String KEY_FILE_SIZE = "fileSize";
	public static final String KEY_SONG_PATH = "songPath";
	public static final String KEY_LAST_PLAYED = "lastPlayed";
	public static final String KEY_DATE_ADDED = "dateAdded";
	public static final String KEY_PLAY_COUNT = "playCount";
	//public static final String KEY_ALBUM_ART = "albumArt";

	private SQLiteDatabase db;
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase _db) {
		// @formatter:off
		String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
			+ KEY_SONG_ID 		+ " INTEGER		PRIMARY KEY		AUTOINCREMENT		NOT NULL,"
			+ KEY_TITLE 		+ " TEXT		NOT NULL," 
			+ KEY_ARTIST		+ " TEXT		DEFAULT NULL," 
			+ KEY_ALBUM 		+ " TEXT		DEFAULT NULL,"
			+ KEY_ALBUM_ARTIST	+ " TEXT		DEFAULT NULL," 
			+ KEY_TRACK_NUMBER	+ " INTEGER		DEFAULT	NULL," 
			+ KEY_REMIXER		+ " TEXT		DEFAULT NULL," 
			+ KEY_PRODUCER		+ " TEXT		DEFAULT NULL," 
			+ KEY_FEATURING		+ " TEXT		DEFAULT NULL," 
			+ KEY_GENRE 		+ " TEXT		DEFAULT NULL,"
			+ KEY_RATING 		+ " INTEGER		DEFAULT NULL," 
			+ KEY_YEAR			+ " TEXT		DEFAULT NULL," 
			+ KEY_FILETYPE		+ " TEXT		NOT NULL,"
			+ KEY_DURATION		+ " INTEGER		NOT NULL		DEFAULT 0," 
			+ KEY_LYRICS		+ " TEXT		DEFAULT NULL,"
			+ KEY_FILE_SIZE		+ " INTEGER		NOT NULL," // not sure if this is necessary
			+ KEY_SONG_PATH		+ " TEXT		UNIQUE," // maybe this should be BLOB
			+ KEY_LAST_PLAYED 	+ " TEXT		DEFAULT NULL,"
			+ KEY_DATE_ADDED	+ " TEXT		DEFAULT CURRENT_TIMESTAMP," 
			+ KEY_PLAY_COUNT	+ " INTEGER		DEFAULT 0"
			+ ")";
		// @formatter:on
		
		_db.execSQL(CREATE_SONGS_TABLE);
		db = _db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
		String DROP_SONGS_TABLE = "DROP TABLE IF EXISTS " + TABLE_SONGS;

		_db.execSQL(DROP_SONGS_TABLE);
		db = _db;
	}

	// Add new song to database
	public void addSong(File f, Tag tag, AudioHeader head) {
		openDb();

		ContentValues values = new ContentValues();
	
		values.put(KEY_TITLE, tryField(tag, 
				new FieldKey[] { FieldKey.TITLE_SORT, FieldKey.TITLE }));
		values.put(KEY_ARTIST, tryField(tag, 
				new FieldKey[] { FieldKey.ARTIST_SORT, FieldKey.ARTIST }));
		values.put(KEY_ALBUM, tryField(tag,
				new FieldKey[] { FieldKey.ALBUM_SORT, FieldKey.ALBUM }));
		values.put(KEY_ALBUM_ARTIST, tryField(tag,
				new FieldKey[] { FieldKey.ALBUM_ARTIST_SORT, FieldKey.ALBUM_ARTIST }));
		values.put(KEY_TRACK_NUMBER, tag.getFirst(FieldKey.TRACK));
		values.put(KEY_REMIXER, tag.getFirst(FieldKey.REMIXER));
		values.put(KEY_PRODUCER, tag.getFirst(FieldKey.PRODUCER));
		values.put(KEY_FEATURING, tag.getFirst(FieldKey.CUSTOM1)); // TODO this stuff again
		values.put(KEY_GENRE, tag.getFirst(FieldKey.GENRE));	
		values.put(KEY_RATING, tag.getFirst(FieldKey.RATING));
		values.put(KEY_YEAR, tag.getFirst(FieldKey.YEAR));
		values.put(KEY_FILETYPE, head.getFormat());
		values.put(KEY_DURATION, head.getTrackLength());
		values.put(KEY_LYRICS, tag.getFirst(FieldKey.LYRICS));
		values.put(KEY_SONG_PATH, f.getPath());
		values.put(KEY_FILE_SIZE, f.length());
		
		// Insert and update rows
		db.insertWithOnConflict(TABLE_SONGS, null, values,  SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void closeDb() {
		if (db.isOpen()) {
			db.close();
		}
	}
	
	public void openDb() {
		if (db == null) {
			db = this.getReadableDatabase();
		}else if (!db.isOpen()) {
			db = this.getReadableDatabase();
		}
	}
	
	private String tryField(Tag tag, FieldKey[] id) {
		String s = new String();
		
		for (int i = 0; i<id.length; i++) {
			s = tag.getFirst(id[i]);
			if (!s.isEmpty()) {
				return s;
			}
		}
		
		return null;
	}

	// Returns a Song
	public AudioFile getSongById(int id) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		openDb();
		Cursor cursor = db.query(TABLE_SONGS, new String[] { KEY_SONG_PATH },
				KEY_SONG_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		// gets the path from the cursor
		AudioFile song = AudioFileIO.read(new File(cursor.getString(0)));
		return song;
	}
	

	// Returns a List of filtered songs
	public Cursor getTagList(String key, String[] filter) {
		Cursor c;
		openDb();
		db = this.getReadableDatabase();
		c = db.query(TABLE_SONGS, new String[] { key },
				key + "=?", filter, null, null, key + " DESC" );

		return c;
	}
	
	// Returns a List of all songs
	public Cursor getTagList(String[] key) {
		Cursor c;
		openDb();

		c = db.rawQuery("SELECT ? AS _id,  ? FROM ? ", // TODO add sort clause
				DbUtils.concat(key, new String[] { KEY_SONG_ID, TABLE_SONGS  }));

//		c = db.query(TABLE_SONGS, new String[] { key },
//				key, null, null, null, key + " DESC" );
		return c;
	}

	// updates a Song with the modified info
	public void updateSong(AudioFile song) {
		
	}

	// deletes a song record
	public void deleteSong(AudioFile song) {

	}
	
}
