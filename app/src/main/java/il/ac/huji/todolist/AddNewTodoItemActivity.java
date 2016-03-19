package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Viner on 18/03/2016.
 */
public class AddNewTodoItemActivity extends Activity{
    Intent intent;
    public void onCreate(Bundle unused){
        super.onCreate(unused);
        setContentView(R.layout.add_item_layout);
        intent = getIntent();
    }

    /**
     * OK button function - send extras from AddNewTodoItemActivity to TodoListManagerActivity
     * using intent, in order to add a new task.
     */
    public void okButton(View view) {

        EditText editText = (EditText) findViewById(R.id.txtTodo);
        DatePicker datePicker = (DatePicker)findViewById((R.id.datePicker));
        String title = editText.getText().toString();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth()+1) + "-" + datePicker.getYear();
        Date taskDate;
        try{
            taskDate = dateFormat.parse(date);
        } catch (java.text.ParseException ex){
            taskDate = null;
        }

        Intent intent = getIntent();
        intent.putExtra("title", title);
        intent.putExtra("dueDate", taskDate);
        this.setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Cancel button function - ignore input, close AddNewTodoItemActivity activity.
     */
    public void cancelButton(View view) {
        this.setResult(RESULT_CANCELED, intent);
        finish();
    }

}
