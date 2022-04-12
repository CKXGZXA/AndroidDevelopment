package top.ckxgzxa.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        // 从数据库中获取数据

        SQLiteDatabase db = openOrCreateDatabase(this.getFilesDir().toString() + "/notes.db",
                MODE_PRIVATE, null);
        String sql = "select * from notes where _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        // 获取数据, topic和content
        String topic, content;
        cursor.moveToFirst();
        topic = cursor.getString(cursor.getColumnIndex("topic"));
        content = cursor.getString(cursor.getColumnIndex("note"));

        // 将topic给R.id.topic, content给R.id.content
        TextView topicTv = findViewById(R.id.detail_topic);
        topicTv.setText(topic);
        EditText contentEt = findViewById(R.id.detail_content);
        contentEt.setText(content);

        // 设置按钮save 的监听器
        findViewById(R.id.detail_save).setOnClickListener(v -> {
            // 获取topic和content
            String t = topicTv.getText().toString();
            String c = contentEt.getText().toString();
            // 更新数据库
            updateNote(t, c);
            // 发弹窗表示更新成功
            Toast.makeText(NoteDetailActivity.this, "更新成功", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK, null);
            finish();
        });

        // 设置按钮delete 的监听器
        findViewById(R.id.detail_delete).setOnClickListener(v -> {
            // 删除数据库
            deleteNote();
            // 发弹窗表示删除成功
            Toast.makeText(NoteDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK, null);
            finish();
        });
    }


    // 更新笔记
    @SuppressLint("SetTextI18n")
    public void updateNote(String topic, String content) {
        // 判断是否为空
        if (topic.trim().equals("") || content.trim().equals("")) {
            if (topic.trim().equals("")) {
                Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        SQLiteDatabase db = openOrCreateDatabase(this.getFilesDir().toString() + "/notes.db",
                MODE_PRIVATE, null);
        // 时间
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        // 更新数据库
        String sql = "update notes set topic = '" + topic + "', note = '" + content + "'," +
                " time = '" + time + "' where _id = " + id;
        db.execSQL(sql);
    }

    // 删除笔记
    public void deleteNote() {
        bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        SQLiteDatabase db = openOrCreateDatabase(this.getFilesDir().toString() + "/notes.db",
                MODE_PRIVATE, null);
        String sql = "delete from notes where _id = " + id;
        db.execSQL(sql);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK, null);
        }
        return super.onKeyDown(keyCode, event);
    }
}