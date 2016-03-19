package il.ac.huji.todolist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/** TodoListManagerActivity class */
public class TodoListManagerActivity extends AppCompatActivity {

    static final int ADD_ACTIVITY = 1;
    ArrayList<ListRow> todoItems;
    ListViewAdapter adapter;

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

                addTask(title, date);

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
    public void addTask(String title, Date dateObj) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String date;

        if (dateObj == null){
            date = "No Due Date";
        } else {
            date = dateFormat.format(dateObj);
        }

        if (!"".equals(title)){
            todoItems.add(new ListRow(title, date));
            adapter.notifyDataSetChanged();
        }
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

        switch (item.getItemId()) {
            case R.id.menuItemDelete:
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