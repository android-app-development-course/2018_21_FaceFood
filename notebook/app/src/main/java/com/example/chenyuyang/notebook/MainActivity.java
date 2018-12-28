package com.example.chenyuyang.notebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenyuyang.notebook.module.mybdHelper;
import com.example.chenyuyang.notebook.module.note;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static boolean hasDBIni=false;

    TabLayout tab=null;
    ViewPager viewPager=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab=(TabLayout)findViewById(R.id.tab);
        viewPager=(ViewPager)findViewById(R.id.htab_viewpager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(!MainActivity.hasDBIni) {
            getApplicationContext().deleteDatabase("note.db");
            mybdHelper dbHelper = new mybdHelper(this, "note.db", null, 1);
            note.setDB(dbHelper.getWritableDatabase());

            if(checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE")!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"},1);
            }
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                String sdCard = Environment.getExternalStorageDirectory().getPath();
                mediaPlayer.setDataSource(sdCard + File.separator +"Download"+File.separator + "m.mp3");
                mediaPlayer.prepare(); //准备播放
                mediaPlayer.start(); //播放
            }catch (Exception e){
                Toast.makeText(this,"播放背景音乐失败",Toast.LENGTH_LONG).show();
            }


            MainActivity.hasDBIni=true;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                addNote();
            }
        });

        setSupportActionBar(toolbar);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext()));
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            addNote();
            return true;
        }
        else if (id==R.id.action_search){
            Context c = MainActivity.this;
            AlertDialog.Builder b = new AlertDialog.Builder(c);
            b.setTitle("输入标题");
            final EditText editText = new EditText(c);
            b.setView(editText);
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fuzzy = editText.getText().toString();
                    Intent intent = new Intent(MainActivity.this,searchResult.class);
                    intent.putExtra("fuzzy",fuzzy);
                    startActivity(intent);
                }
            });
            b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ;
                }
            });
            b.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNote(){
        try {
            Intent intent = new Intent(MainActivity.this, noteEditor.class);
            startActivity(intent);
        }catch (Exception e){
            e.toString();
        }
    }
}

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        mFragmentTitleList.add(("公开日记本"));
        mFragmentTitleList.add("私有日记本");
        mFragmentList.add( new publicFragment());
        mFragmentList.add(new publicFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
