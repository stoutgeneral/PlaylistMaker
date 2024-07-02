package com.example.playlistmaker.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.playlistmaker.data.db.dao.FavoriteDao;
import com.example.playlistmaker.data.db.dao.FavoriteDao_Impl;
import com.example.playlistmaker.data.db.dao.PlaylistDao;
import com.example.playlistmaker.data.db.dao.PlaylistDao_Impl;
import com.example.playlistmaker.data.db.dao.PlaylistTrackDao;
import com.example.playlistmaker.data.db.dao.PlaylistTrackDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDataBase_Impl extends AppDataBase {
  private volatile FavoriteDao _favoriteDao;

  private volatile PlaylistDao _playlistDao;

  private volatile PlaylistTrackDao _playlistTrackDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_entity` (`trackId` INTEGER NOT NULL, `trackName` TEXT, `artistName` TEXT, `trackTimeMillis` INTEGER, `artworkUrl100` TEXT, `collectionName` TEXT, `releaseDate` TEXT, `primaryGenreName` TEXT, `country` TEXT, `previewUrl` TEXT, `insertTimeStamp` INTEGER NOT NULL, PRIMARY KEY(`trackId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_entity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT, `description` TEXT, `uri` TEXT, `tracks` TEXT, `trackTimeMillis` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_track_entity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `playlistId` INTEGER NOT NULL, `trackId` INTEGER NOT NULL, `trackName` TEXT, `artistName` TEXT, `trackTimeMillis` INTEGER, `artworkUrl100` TEXT, `collectionName` TEXT, `releaseDate` TEXT, `primaryGenreName` TEXT, `country` TEXT, `previewUrl` TEXT, FOREIGN KEY(`playlistId`) REFERENCES `playlist_entity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_track_entity_playlistId` ON `playlist_track_entity` (`playlistId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3be4f559c883ee4796ec64b6c76be1e7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorite_entity`");
        db.execSQL("DROP TABLE IF EXISTS `playlist_entity`");
        db.execSQL("DROP TABLE IF EXISTS `playlist_track_entity`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFavoriteEntity = new HashMap<String, TableInfo.Column>(11);
        _columnsFavoriteEntity.put("trackId", new TableInfo.Column("trackId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("trackName", new TableInfo.Column("trackName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("artistName", new TableInfo.Column("artistName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("trackTimeMillis", new TableInfo.Column("trackTimeMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("artworkUrl100", new TableInfo.Column("artworkUrl100", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("collectionName", new TableInfo.Column("collectionName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("releaseDate", new TableInfo.Column("releaseDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("primaryGenreName", new TableInfo.Column("primaryGenreName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("country", new TableInfo.Column("country", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("previewUrl", new TableInfo.Column("previewUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("insertTimeStamp", new TableInfo.Column("insertTimeStamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoriteEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoriteEntity = new TableInfo("favorite_entity", _columnsFavoriteEntity, _foreignKeysFavoriteEntity, _indicesFavoriteEntity);
        final TableInfo _existingFavoriteEntity = TableInfo.read(db, "favorite_entity");
        if (!_infoFavoriteEntity.equals(_existingFavoriteEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_entity(com.example.playlistmaker.data.db.entity.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavoriteEntity + "\n"
                  + " Found:\n" + _existingFavoriteEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylistEntity = new HashMap<String, TableInfo.Column>(6);
        _columnsPlaylistEntity.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistEntity.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistEntity.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistEntity.put("uri", new TableInfo.Column("uri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistEntity.put("tracks", new TableInfo.Column("tracks", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistEntity.put("trackTimeMillis", new TableInfo.Column("trackTimeMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylistEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylistEntity = new TableInfo("playlist_entity", _columnsPlaylistEntity, _foreignKeysPlaylistEntity, _indicesPlaylistEntity);
        final TableInfo _existingPlaylistEntity = TableInfo.read(db, "playlist_entity");
        if (!_infoPlaylistEntity.equals(_existingPlaylistEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist_entity(com.example.playlistmaker.data.db.entity.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylistEntity + "\n"
                  + " Found:\n" + _existingPlaylistEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaylistTrackEntity = new HashMap<String, TableInfo.Column>(12);
        _columnsPlaylistTrackEntity.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("playlistId", new TableInfo.Column("playlistId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("trackId", new TableInfo.Column("trackId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("trackName", new TableInfo.Column("trackName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("artistName", new TableInfo.Column("artistName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("trackTimeMillis", new TableInfo.Column("trackTimeMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("artworkUrl100", new TableInfo.Column("artworkUrl100", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("collectionName", new TableInfo.Column("collectionName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("releaseDate", new TableInfo.Column("releaseDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("primaryGenreName", new TableInfo.Column("primaryGenreName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("country", new TableInfo.Column("country", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistTrackEntity.put("previewUrl", new TableInfo.Column("previewUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistTrackEntity = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPlaylistTrackEntity.add(new TableInfo.ForeignKey("playlist_entity", "CASCADE", "NO ACTION", Arrays.asList("playlistId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPlaylistTrackEntity = new HashSet<TableInfo.Index>(1);
        _indicesPlaylistTrackEntity.add(new TableInfo.Index("index_playlist_track_entity_playlistId", false, Arrays.asList("playlistId"), Arrays.asList("ASC")));
        final TableInfo _infoPlaylistTrackEntity = new TableInfo("playlist_track_entity", _columnsPlaylistTrackEntity, _foreignKeysPlaylistTrackEntity, _indicesPlaylistTrackEntity);
        final TableInfo _existingPlaylistTrackEntity = TableInfo.read(db, "playlist_track_entity");
        if (!_infoPlaylistTrackEntity.equals(_existingPlaylistTrackEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist_track_entity(com.example.playlistmaker.data.db.entity.PlaylistTrackEntity).\n"
                  + " Expected:\n" + _infoPlaylistTrackEntity + "\n"
                  + " Found:\n" + _existingPlaylistTrackEntity);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3be4f559c883ee4796ec64b6c76be1e7", "32f95fac54df0667f1741de75af28388");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorite_entity","playlist_entity","playlist_track_entity");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `favorite_entity`");
      _db.execSQL("DELETE FROM `playlist_entity`");
      _db.execSQL("DELETE FROM `playlist_track_entity`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaylistDao.class, PlaylistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaylistTrackDao.class, PlaylistTrackDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FavoriteDao getFavoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public PlaylistDao getPlaylistDao() {
    if (_playlistDao != null) {
      return _playlistDao;
    } else {
      synchronized(this) {
        if(_playlistDao == null) {
          _playlistDao = new PlaylistDao_Impl(this);
        }
        return _playlistDao;
      }
    }
  }

  @Override
  public PlaylistTrackDao getPlaylistTrackDao() {
    if (_playlistTrackDao != null) {
      return _playlistTrackDao;
    } else {
      synchronized(this) {
        if(_playlistTrackDao == null) {
          _playlistTrackDao = new PlaylistTrackDao_Impl(this);
        }
        return _playlistTrackDao;
      }
    }
  }
}
