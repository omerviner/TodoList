/**
 * Created by Viner on 18/03/2016.
 */

package il.ac.huji.todolist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ListViewAdapter: ListView for double-column row
 */
public class ListViewAdapter extends ArrayAdapter<ListRow> {

    public ListViewAdapter(Context context, ArrayList<ListRow> list) {
        super(context, android.R.layout.simple_list_item_1, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ListRow row = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row, parent, false);
        }

        // Lookup view for data population
        TextView txtTodoTitle = (TextView) convertView.findViewById(R.id.txtTodoTitle);
        TextView txtTodoDueDate = (TextView) convertView.findViewById(R.id.txtTodoDueDate);


        // null or empty duedate case
        if (row.txtTodoDueDate == null || row.txtTodoDueDate.isEmpty()){
            row.txtTodoDueDate = "No due date";
        }

        String dateTxt = row.txtTodoDueDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String now = dateFormat.format(new Date());

        try{

            Date taskDate = dateFormat.parse(dateTxt);
            Date nowDate = dateFormat.parse(now);

            if (taskDate.before(nowDate)){
                Log.v("dateTxt", "" + dateTxt);
                Log.v("now", now.toString());
                txtTodoDueDate.setTextColor(Color.RED);
                txtTodoTitle.setTextColor(Color.RED);
            } else {
                txtTodoDueDate.setTextColor(Color.DKGRAY);
                txtTodoTitle.setTextColor(Color.DKGRAY);
            }

        } catch (java.text.ParseException ex){
        }

        // Populate the data into the template view using the data object
        txtTodoTitle.setText(row.txtTodoTitle);
        txtTodoDueDate.setText(row.txtTodoDueDate);
        // Return the completed view to render on screen
        return convertView;

    }

}
