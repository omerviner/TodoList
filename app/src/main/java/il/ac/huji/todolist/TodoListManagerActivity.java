package il.ac.huji.todolist;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TodoListManagerActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        todoItems = new ArrayList<String>(0);

         adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, todoItems){
             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 TextView v = (TextView)super.getView(position, convertView, parent);
                 if (position%2 == 0) {
                     v.setTextColor(Color.RED);
                 } else {
                     v.setTextColor(Color.BLUE);
                 }

                 return v;
             }
         };


        ListView listView = (ListView) findViewById(R.id.lstTodoItems);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAdd:

                EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);

                String addTxt = edtNewItem.getText().toString();
                if (!"".equals(addTxt)){
                    todoItems.add(addTxt);
                    edtNewItem.setText("");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Task is empty", Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the user clicks the Add button.
     */
    public void addItem(View view) {
        EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);

        String addTxt = edtNewItem.getText().toString();
        if (!"".equals(addTxt)){
            todoItems.add(addTxt);
            edtNewItem.setText("");
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

        menu.setHeaderTitle(list.getItemAtPosition(position).toString());

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
                Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}