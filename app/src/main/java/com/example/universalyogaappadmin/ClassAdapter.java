package com.example.universalyogaappadmin;

import static com.example.universalyogaappadmin.DatabaseHelper.CAPACITY_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.CLASS_TYPE_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.COLUMN_NAME_TIME;
import static com.example.universalyogaappadmin.DatabaseHelper.COURSE_ID_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.DAY_OF_WEEK_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.DESCRIPTION_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.PRICE_COLUMN;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClassAdapter extends BaseAdapter {
    Context context;
    JSONArray classList;
    LayoutInflater inflater;
    DatabaseHelper dbHelper;

    public ClassAdapter(Context appContext, JSONArray classList) {
        this.context = appContext;
        this.classList = classList;
        inflater = (LayoutInflater.from(appContext));
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return classList.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_class_list_view, null);

        TextView classContentTextView = (TextView) view.findViewById(R.id.classContent);
        JSONObject jsonObject;
        int courseId;
        String dayOfWeek, time, capacity, price, classType, description;
        try {
            jsonObject = classList.getJSONObject(i);
            courseId =jsonObject.getInt("id");
            dayOfWeek = jsonObject.getString(DAY_OF_WEEK_COLUMN);
            time = jsonObject.getString(COLUMN_NAME_TIME);
            capacity = jsonObject.getString(CAPACITY_COLUMN);
            price = jsonObject.getString(PRICE_COLUMN);
            classType = jsonObject.getString(CLASS_TYPE_COLUMN);
            description = jsonObject.getString(DESCRIPTION_COLUMN);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Default Day of the week: ");
        ForegroundColorSpan green = new ForegroundColorSpan(Color.RED);
        spannableStringBuilder.setSpan(green,
                0, 10, Spanned.SPAN_PRIORITY);
        String content = (spannableStringBuilder + dayOfWeek + "\n" +
                "Time: " + time + "\n" +
                "Capacity: " + capacity + "\n" +
                "Price: £" + price + "\n" +
                "Class: " + classType + "\n" +
                "Description: " + description);

        classContentTextView.setText(content);
        Button addClassButton = (Button) view.findViewById(R.id.addClassBtn);
        addClassButton.setOnClickListener(v -> {
            Intent classDatePage = new Intent(context, ClassInstanceDateActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("courseId", courseId);
            context.startActivity(classDatePage);
        });

        Button editCourseButton = (Button) view.findViewById(R.id.editCourseBtn);
        editCourseButton.setOnClickListener(v -> {
            Intent courseEdit = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("courseId", courseId);
            context.startActivity(courseEdit);
        });

        Button deleteCourseBtn = (Button) view.findViewById(R.id.deleteCourseBtn);
        deleteCourseBtn.setOnClickListener(v -> {
            dbHelper.deleteCourse(courseId);
            Intent classDatePage = new Intent(context, ClassInstanceList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(classDatePage);
        });

        Button instanceListBtn = (Button) view.findViewById(R.id.instanceListBtn);
        instanceListBtn.setOnClickListener(v -> {
            Intent instanceListPage = new Intent(context, InstanceList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("courseId", courseId);;
            context.startActivity(instanceListPage);
        });
        return view;
    }
}
