package com.example.chenyuyang.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chenyuyang.notebook.module.note;

import java.util.Date;

public class noteEditor extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        Intent intent = getIntent();
        if (intent.hasExtra("note")) {//如果有note传进来
            final note anote =(note)intent.getSerializableExtra("note");
             ((EditText) findViewById(R.id.title)).setText(anote.getTitle());
             ((EditText) findViewById(R.id.content)).setText(anote.getContent());
            ((Button)findViewById(R.id.delete)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.commit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ((EditText) findViewById(R.id.title)).getText().toString();
                    String content = ((EditText) findViewById(R.id.content)).getText().toString();
                    anote.setContent(content);
                    anote.setTitle(title);
                    anote.storeMe();

                    Intent intentTurn = new Intent(noteEditor.this, MainActivity.class);
                    startActivity(intentTurn);
                }
            });
            ((Button)findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        anote.removeMe();
                    }catch (Exception e){
                        Log.e("cyy","删除错误");
                    }
                    Intent intentTurn = new Intent(noteEditor.this, MainActivity.class);
                    startActivity(intentTurn);
                }
            });

        } else {
            ((Button)findViewById(R.id.delete)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.commit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ((EditText) findViewById(R.id.title)).getText().toString();
                    String content = ((EditText) findViewById(R.id.content)).getText().toString();
                    note me = new note(title, content, new Date());
                    me.storeMe();
                    Intent intentTurn = new Intent(noteEditor.this, MainActivity.class);
                    startActivity(intentTurn);
                }
            });
        }
    }
}
