package com.waves.library;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

// TODO add playlist support
public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "musicLibrary";

	// Main library table
	private static final String TABLE_SONGS = "songs";
	private static final String KEY_SONG_ID = "songId";
	private static final String KEY_TITLE = "songTitle";
	private static final String KEY_ARTIST = "artist"; 
	private static final String KEY_ALBUM = "album";
	private static final String KEY_ALBUM_ARTIST = "albumArtist";
	private static final String KEY_TRACK_NUMBER = "trackNumber";
	private static final String KEY_REMIXER = "remixer";
	private static final String KEY_PRODUCER = "producer";
	private static final String KEY_FEATURING = "featuring";
	private static final String KEY_GENRE = "genre";
	private static final String KEY_RATING = "rating"; // out of 5
	private static final String KEY_YEAR = "year"; // date type
	private static final String KEY_FILETYPE = "fileType";
	private static final String KEY_DURATION = "duration";
	private static final String KEY_LYRICS = "lyrics";
	private static final String KEY_FILE_SIZE = "fileSize";
	private static final String KEY_SONG_PATH = "songPath";
	private static final String KEY_LAST_PLAYED = "lastPlayed";
	private static final String KEY_DATE_ADDED = "dateAdded";
	private static final String KEY_PLAY_COUNT = "playCount";
	//private static final String KEY_ALBUM_ART = "albumArt";

	// FILETYPES table
	private static final String TABLE_FILETYPES = "fileTypes";
	private static final String KEY_FILETYPE_ID = "filetypeId";
	
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
	public void onCreate(SQLiteDatabase db) {
		// @formatter:off
		String CREATE_FILETYPE_TABLE = "CREATE TABLE " + TABLE_FILETYPES + "("
			+ KEY_FILETYPE_ID
			+ "INTEGER	PRIMARY KEY		AUTOINCREMENT	NOT NULL," + KEY_FILETYPE
			+ "TEXT		NOT NULL		UNIQUE" + ")";

		String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
			+ KEY_SONG_ID 		+ " INTEGER		PRIMARY KEY		AUTOINCREMENT	NOT NULL,"
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
			+ KEY_FILETYPE	+ " INTEGER		NOT NULL		REFERENCES " + TABLE_FILETYPES 
				+ "(" + KEY_FILETYPE + ")," 
			+ KEY_DURATION		+ " INTEGER		NOT NULL		DEFAULT 0," 
			+ KEY_LYRICS		+ " TEXT		DEFAULT NULL,"
			+ KEY_FILE_SIZE		+ " INTEGER		NOT NULL," // not sure if this is necessary
			+ KEY_SONG_PATH		+ " TEXT		UNIQUE," // maybe this should be BLOB
			+ KEY_LAST_PLAYED 	+ " TEXT		DEFAULT NULL,"
			+ KEY_DATE_ADDED	+ " TEXT		DEFAULT CURRENT_TIMESTAMP," 
			+ KEY_PLAY_COUNT	+ " INTEGER		DEFAULT 0,"
			//+ KEY_ALBUM_ART		 + " TEXT		DEFAULT NULL" 
			+ ")";
		// @formatter:on
		
		db.execSQL(CREATE_FILETYPE_TABLE);
		db.execSQL(CREATE_SONGS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String DROP_FILETYPE_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILETYPES;
		String DROP_SONGS_TABLE = "DROP TABLE IF EXISTS " + TABLE_SONGS;

		db.execSQL(DROP_SONGS_TABLE);
		db.execSQL(DROP_FILETYPE_TABLE);
	}

	// Add new song to database
	public void addSong(Song song) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// songId
		values.put(KEY_TITLE, song.title);
		values.put(KEY_ARTIST, song.artist);
		values.put(KEY_ALBUM, song.album);
		values.put(KEY_ALBUM_ARTIST, song.getAlbumArtist());
		values.put(KEY_TRACK_NUMBER, song.getTrackNumber());
		values.put(KEY_REMIXER, song.getRemixer());
		values.put(KEY_PRODUCER, song.getProducer());
		values.put(KEY_FEATURING, song.getFeaturing());
		values.put(KEY_GENRE, song.getGenre());	
		values.put(KEY_RATING, song.getRating());
		values.put(KEY_YEAR, song.getYear());
		values.put(KEY_FILETYPE, song.getFiletype());
		values.put(KEY_DURATION, song.getDuration());
		values.put(KEY_LYRICS, song.getLyrics());
		values.put(KEY_FILE_SIZE, song.getFileSize());
		values.put(KEY_SONG_PATH, song.getPath());
		values.put(KEY_LAST_PLAYED, song.getLastPlayed());
		values.put(KEY_DATE_ADDED, song.getDateAdded());
		values.put(KEY_PLAY_COUNT, song.getPlayCount());
		//values.put(KEY_ALBUM_ART, song.albumArt);
		
		// Inserting Row
		db.insert(TABLE_SONGS, null, values);
		db.close(); // Closing database connection
	}

	// Returns a Song
	public Song getSongById(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SONGS, new String[] { KEY_SONG_ID,
				KEY_TITLE, KEY_ARTIST, KEY_ALBUM }, KEY_SONG_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		Song song = new Song(cursor.getString(1)); // needs to be changed

		return song;
	}

	// Returns a List of all songs
	public List<Song> getSetOfSongs() {
		return null;

	}

	// updates a Song with the modified info
	public int updateSong(Song song) {
		return 0;

	}

	// deletes a song record
	public void deleteSong(Song song) {

	}

}
