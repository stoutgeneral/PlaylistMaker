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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDataBase_Impl extends AppDataBase {
  private volatile FavoriteDao _favoriteDao;

  private volatile PlaylistDao _playlistDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_entity` (`trackId` INTEGER NOT NULL, `trackName` TEXT NOT NULL, `artistName` TEXT NOT NULL, `trackTime` TEXT NOT NULL, `artworkUrl100` TEXT NOT NULL, `collectionName` TEXT NOT NULL, `releaseDate` TEXT NOT NULL, `primaryGenreName` TEXT NOT NULL, `country` TEXT NOT NULL, `previewUrl` TEXT NOT NULL, `insertTimeStamp` INTEGER NOT NULL, PRIMARY KEY(`trackId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_entity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT, `description` TEXT, `uri` TEXT, `tracks` TEXT, `trackCounter` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '79b2df3d76a35d671966787388a1f6b3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorite_entity`");
        db.execSQL("DROP TABLE IF EXISTS `playlist_entity`");
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
        _columnsFavoriteEntity.put("trackName", new TableInfo.Column("trackName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("artistName", new TableInfo.Column("artistName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("trackTime", new TableInfo.Column("trackTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("artworkUrl100", new TableInfo.Column("artworkUrl100", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("collectionName", new TableInfo.Column("collectionName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("releaseDate", new TableInfo.Column("releaseDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("primaryGenreName", new TableInfo.Column("primaryGenreName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteEntity.put("previewUrl", new TableInfo.Column("previewUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
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
        _columnsPlaylistEntity.put("trackCounter", new TableInfo.Column("trackCounter", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylistEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylistEntity = new TableInfo("playlist_entity", _columnsPlaylistEntity, _foreignKeysPlaylistEntity, _indicesPlaylistEntity);
        final TableInfo _existingPlaylistEntity = TableInfo.read(db, "playlist_entity");
        if (!_infoPlaylistEntity.equals(_existingPlaylistEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist_entity(com.example.playlistmaker.data.db.entity.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylistEntity + "\n"
                  + " Found:\n" + _existingPlaylistEntity);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "79b2df3d76a35d671966787388a1f6b3", "106f6608cc92269e6a7abbd8604a4815");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorite_entity","playlist_entity");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `favorite_entity`");
      _db.execSQL("DELETE FROM `playlist_entity`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
}
