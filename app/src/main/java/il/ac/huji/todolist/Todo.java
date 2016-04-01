package il.ac.huji.todolist;

import android.provider.BaseColumns;

/**
 * Created by Viner on 31/03/2016.
 */
public final class Todo {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Todo() {}

    /* Inner class that defines the table contents */
    public static abstract class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DUE = "due";
    }
}
