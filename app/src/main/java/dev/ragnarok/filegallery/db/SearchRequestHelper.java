package dev.ragnarok.filegallery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import dev.ragnarok.filegallery.db.column.FilesColumns;
import dev.ragnarok.filegallery.db.column.SearchRequestColumns;
import dev.ragnarok.filegallery.db.column.TagDirsColumns;
import dev.ragnarok.filegallery.db.column.TagOwnerColumns;


public class SearchRequestHelper extends SQLiteOpenHelper {

    private static final Object lock = new Object();
    private static volatile SearchRequestHelper instance;

    private SearchRequestHelper(Context context) {
        super(context, "search_queries.sqlite", null, 1);
    }

    public static SearchRequestHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SearchRequestHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSearchRequestTable(db);
        createTagOwnersTable(db);
        createTagDirsTable(db);
        createFilesTable(db);
    }

    private void createSearchRequestTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS [" + SearchRequestColumns.TABLENAME + "] (\n" +
                "  [" + BaseColumns._ID + "] INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "  [" + SearchRequestColumns.SOURCE_ID + "] INTEGER, " +
                "  [" + SearchRequestColumns.QUERY + "] TEXT, " +
                "  CONSTRAINT [] UNIQUE ([" + BaseColumns._ID + "]) ON CONFLICT REPLACE);";
        db.execSQL(sql);
    }

    private void createTagOwnersTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS [" + TagOwnerColumns.TABLENAME + "] (\n" +
                "  [" + BaseColumns._ID + "] INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "  [" + TagOwnerColumns.NAME + "] TEXT, " +
                "  CONSTRAINT [] UNIQUE ([" + BaseColumns._ID + "]) ON CONFLICT REPLACE);";
        db.execSQL(sql);
    }

    private void createTagDirsTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS [" + TagDirsColumns.TABLENAME + "] (\n" +
                "  [" + BaseColumns._ID + "] INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "  [" + TagDirsColumns.OWNER_ID + "] INTEGER, " +
                "  [" + TagDirsColumns.NAME + "] TEXT, " +
                "  [" + TagDirsColumns.PATH + "] TEXT, " +
                "  [" + TagDirsColumns.TYPE + "] INTEGER, " +
                "  CONSTRAINT [] UNIQUE ([" + BaseColumns._ID + "]) ON CONFLICT REPLACE);";
        db.execSQL(sql);
    }

    private void createFilesTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS [" + FilesColumns.TABLENAME + "] (\n" +
                "  [" + BaseColumns._ID + "] INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "  [" + FilesColumns.PARENT_DIR + "] TEXT, " +
                "  [" + FilesColumns.TYPE + "] INTEGER, " +
                "  [" + FilesColumns.IS_DIR + "] INTEGER, " +
                "  [" + FilesColumns.FILE_NAME + "] TEXT, " +
                "  [" + FilesColumns.FILE_PATH + "] TEXT, " +
                "  [" + FilesColumns.PARENT_NAME + "] TEXT, " +
                "  [" + FilesColumns.PARENT_PATH + "] TEXT, " +
                "  [" + FilesColumns.MODIFICATIONS + "] BIGINT, " +
                "  [" + FilesColumns.SIZE + "] BIGINT, " +
                "  [" + FilesColumns.CAN_READ + "] INTEGER, " +
                "  CONSTRAINT [] UNIQUE ([" + BaseColumns._ID + "]) ON CONFLICT REPLACE);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
