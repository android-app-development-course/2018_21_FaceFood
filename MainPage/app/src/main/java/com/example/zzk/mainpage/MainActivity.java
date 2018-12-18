package com.example.zzk.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.example.cyy.controller.InfoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private List<Fragment> fragments;
    private static final String[] TAGS = {"home", "account"};
    private int prePos;
    private String PRE = "PREPOS";

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                switchFragment(0);
                return true;
            case R.id.navigation_dashboard:
                Intent intent = new Intent(this, ShareActivity.class);
                startActivity(intent);
                return false;
            case R.id.navigation_notifications:
                switchFragment(1);
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {

            prePos = 0;
            fragments = new ArrayList<>();
            fragments.add(new HomeTabFragment());
            fragments.add(new InfoFragment());
        }
        else {

            prePos = savedInstanceState.getInt(PRE);
            fragments = new ArrayList<>();
            HomeTabFragment homeFragment = (HomeTabFragment) getSupportFragmentManager().findFragmentByTag(TAGS[0]);
            InfoFragment accountFragment = (InfoFragment) getSupportFragmentManager().findFragmentByTag(TAGS[1]);
            fragments.add(homeFragment);
            fragments.add(accountFragment);
        }

        setDefaultFragment(prePos);
    }

    private void setDefaultFragment(int pos){

        Fragment fragment = fragments.get(pos);

        if(fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragments.get(prePos), TAGS[pos]).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PRE, prePos);
    }

    private void switchFragment(int pos) {

        Fragment currentFragment = fragments.get(pos);
        Fragment previousFragment = fragments.get(prePos);

        getSupportFragmentManager().beginTransaction().hide(previousFragment).commit();

        if(currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, currentFragment, TAGS[pos]).commit();
        }

        prePos = pos;
    }


}