package il.ac.huji.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/** TodoListManagerActivity class */
public class TodoListManagerActivity extends AppCompatActivity {

    static final int ADD_ACTIVITY = 1;
    ArrayList<ListRow> todoItems;
    ListViewAdapter adapter;
    DBHelper mDbHelper;
    SQLiteDatabase db;
    SimpleDateFormat dateFormat;

    /** TodoListManagerActivity onCreate function */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        todoItems = new ArrayList<ListRow>(0);

        // Set a new adapter for a custom 2-columns ListView
        adapter = new ListViewAdapter(this, todoItems);

        ListView listView = (ListView) findViewById(R.id.lstTodoItems);
        listView.setAdapter(adapter);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        mDbHelper = new DBHelper(this);
        db = mDbHelper.getWritableDatabase();

        Cursor c = db.query("todo", new String[]{"_id", "title", "due"}, null, null, null, null, "_id ASC");
        c.moveToFirst();
        long id;
        String title, dateStr;
        Date date;

        while (!c.isAfterLast()){
            id = c.getLong(0);
            title = c.getString(1);
            dateStr = c.getString(2);

            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException ex){
                date = null;
            }

            addTask(id, title, date);

                c.moveToNext();
        }

        registerForContextMenu(listView);
    }

    /** TodoListManagerActivity onCreateOptionsMenu function */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    /** Get get extras from AddNewTodoItemActivity using intent. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == ADD_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String title = intent.getStringExtra("title");
//                Date date = intent.getExtras("dueDate");
                Date date = (Date)intent.getSerializableExtra("dueDate");


                String due;

                if (date == null){
                    due = "No Due Date";
                } else {
                    due = dateFormat.format(date);
                }

                long id = saveTask(title, date);
                addTask(id, title, date);
            }
        }
    }

    /**
     * main menu options (add button)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAdd:

                Intent intent = new Intent(this, AddNewTodoItemActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("dueDate", "");
                startActivityForResult(intent, ADD_ACTIVITY);


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the user clicks the Add button.
     */
    public void addTask(long id, String title, Date dateObj) {

        String date;

        if (dateObj == null){
            date = "No Due Date";
        } else {
            date = dateFormat.format(dateObj);
        }

        if (!"".equals(title)){
            todoItems.add(new ListRow(id, title, date));
            adapter.notifyDataSetChanged();
        } else {
            Toast toast = Toast.makeText(this, "Task title missing", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when the user clicks the Add button.
     */
    public long saveTask(String title, Date dateObj) {

        String date;
        long id = -1;
        if (dateObj == null){
            date = "No Due Date";
        } else {
            date = dateFormat.format(dateObj);
        }

        if (!"".equals(title)){

            ContentValues values = new ContentValues();
//            values.put(Todo.TodoEntry.COLUMN_ID, id);
            values.put(Todo.TodoEntry.COLUMN_TITLE, title);
            values.put(Todo.TodoEntry.COLUMN_DUE, date);

            id = db.insert(Todo.TodoEntry.TABLE_NAME,
                    null,
                    values);
        }

        return id;
    }

    @Override
    /**
     * Set menu for long click on task.
     */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

        // Get the list
        ListView list = (ListView)v;

        // Get the list item position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;

        ListRow itemSelected = (ListRow)list.getItemAtPosition(position);

        menu.setHeaderTitle(itemSelected.txtTodoTitle);

        // Hide Call item on context menu
        if (!itemSelected.txtTodoTitle.startsWith("Call ")){
            menu.findItem(R.id.menuItemCall).setVisible(false);
        } else {
            menu.findItem(R.id.menuItemCall).setTitle(itemSelected.txtTodoTitle);
        }
    }


    @Override
    /**
     * Set function task long click menu.
     */
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = info.position;
        ListRow row = todoItems.get(position);

        long id = row._id;

        switch (item.getItemId()) {
            case R.id.menuItemDelete:

                db.delete(Todo.TodoEntry.TABLE_NAME, "_id LIKE ?", new String[]{Long.toString(id)});
                todoItems.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.menuItemCall:
                String numberStr = item.getTitle().toString().substring(5).trim();
                Uri number = Uri.parse("tel:" + numberStr);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);

            default:
                return super.onContextItemSelected(item);
        }
    }


}