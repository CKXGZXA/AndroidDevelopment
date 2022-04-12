package top.ckxgzxa.notepad;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CKXG
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 判断是否通过密码验证
        if (!MyApplication.isPassed()){
            // 跳转到密码验证界面
            Intent intent = new Intent(MainActivity.this, PwdActivity.class);
            startActivity(intent);
        }


        listView = this.findViewById(R.id.list);
        // 给listView设置点击事件
        listView.setOnItemClickListener(this);


        Button btn = findViewById(R.id.save);

        // 创建或打开数据库
        dbHelper = new MyDatabaseHelper(this,
                this.getFilesDir().toString() + "/notes.db", 1);

        showNotes();

        EditText topicEt = findViewById(R.id.new_topic);
        EditText noteEt = findViewById(R.id.new_note);
        btn.setOnClickListener(view -> {
            // 获取用户输入的数据
            String topic = topicEt.getText().toString();
            String note = noteEt.getText().toString();
            // 将数据插入数据库

            if (insertData(topic, note)) {
                // 清空输入框
                topicEt.setText("");
                noteEt.setText("");
            }
            showNotes();
        });
    }

    private boolean insertData(String topic, String note) {
        // 执行数据库插入操作
        // 判断是否为空
        if (topic.trim().equals("") || note.trim().equals("")) {
            if (topic.trim().equals("")) {
                Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        // 获取当前时间yyyy-MM-dd HH:mm
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);

        dbHelper.getWritableDatabase().execSQL("insert into notes(topic, note, time) values(?, ?, ?)",
                new String[]{topic, note, time});
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "insertData: " + topic + " " + note);
        return true;
    }



    private void showNotes() {
        // 从数据库中获取数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 使用db.query按time降序排列

        Cursor cursor = db.query("notes", null, null, null,
                null, null, "time desc, _id desc");
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.note, cursor,
                new String[]{"_id", "topic", "note", "time",},
                new int[]{R.id.note_no,R.id.note_topic, R.id.note_content, R.id.note_time},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时关闭数据库
        dbHelper.close();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Bundle bundle = new Bundle();
        // 拿到note_no中text的值
        TextView no = view.findViewById(R.id.note_no);
        bundle.putInt("id", Integer.parseInt(no.getText().toString()));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(MainActivity.this, NoteDetailActivity.class);
        Log.i("message", "onItemClick: " + no.getText().toString());
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                showNotes();
            }
        }
    }
}