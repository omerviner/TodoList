package il.ac.huji.todolist;

import java.util.ArrayList;

/**
 * Created by Viner on 18/03/2016.
 */
public class ListRow {

    public String txtTodoTitle;
    public String txtTodoDueDate;
    public long _id;

    public ListRow(long id, String txtTodoTitle, String txtTodoDueDate){
        this._id = id;
        this.txtTodoDueDate = txtTodoDueDate;
        this.txtTodoTitle = txtTodoTitle;
    }
}
