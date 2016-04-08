package il.ac.huji.todolist;
import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;


/**
 * Created by Viner on 31/03/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Todo.TodoEntry.TABLE_NAME + " (" +
                    Todo.TodoEntry.COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    Todo.TodoEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    Todo.TodoEntry.COLUMN_DUE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Todo.TodoEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todo_db.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        Log.v("DBHelper: ", "onCreate");
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

