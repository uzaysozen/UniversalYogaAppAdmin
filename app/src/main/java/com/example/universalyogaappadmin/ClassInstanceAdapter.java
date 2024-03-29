package com.example.universalyogaappadmin;

import static com.example.universalyogaappadmin.DatabaseHelper.COMMENTS_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.COURSE_ID_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.DATE_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.TEACHER_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.INSTANCE_USER_ID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClassInstanceAdapter extends BaseAdapter {
    Context context;
    JSONArray instanceList;
    LayoutInflater inflater;
    DatabaseHelper dbHelper;

    public ClassInstanceAdapter(Context appContext, JSONArray instanceList) {
        this.context = appContext;
        this.instanceList = instanceList;
        inflater = (LayoutInflater.from(appContext));
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return instanceList.length();
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
        view = inflater.inflate(R.layout.activity_class_instance_list_view, null);

        TextView instanceContentTextView = (TextView) view.findViewById(R.id.instanceText);
        JSONObject jsonObject;
        int instanceId;
        String userID, date, teacherName, comments, courseId;
        try {
            jsonObject = instanceList.getJSONObject(i);
            instanceId = jsonObject.getInt("id");
            userID = jsonObject.getString(INSTANCE_USER_ID);
            date = jsonObject.getString(DATE_COLUMN);
            teacherName = jsonObject.getString(TEACHER_COLUMN);
            comments = jsonObject.getString(COMMENTS_COLUMN);
            courseId = jsonObject.getString(COURSE_ID_COLUMN);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String content = ("Date: " + date + "\n"
                + "Teacher Name: " + teacherName + "\n"
                + "Comments: " + comments + "\n");

        instanceContentTextView.setText(content);
        return view;
    }
}
