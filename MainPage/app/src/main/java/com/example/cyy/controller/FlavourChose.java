package com.example.cyy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zzk.mainpage.R;


public class FlavourChose extends AppCompatActivity  {
    TextView[] textView;
    SeekBar[] seekBars;

    protected void initView(){
        textView = new TextView[]{
                findViewById(R.id.cf_s),
                findViewById(R.id.cf_t),
                findViewById(R.id.cf_k),
                findViewById(R.id.cf_l),
        };
        seekBars = new SeekBar[]{
                findViewById(R.id.seekBar2),
                findViewById(R.id.seekBar3),
                findViewById(R.id.seekBar4),
                findViewById(R.id.seekBar5),
        };
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_flavour);
        initView();
        for(int i=0;i<seekBars.length;++i){
            final int index = i;
            seekBars[index].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textView[index].setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        findViewById(R.id.cf_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putInt("sour",Integer.valueOf(textView[0].getText().toString()));
                bundle.putInt("sweet",Integer.valueOf(textView[1].getText().toString()));
                bundle.putInt("bitter",Integer.valueOf(textView[2].getText().toString()));
                bundle.putInt("spicy",Integer.valueOf(textView[3].getText().toString()));
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        findViewById(R.id.cf_cancel).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
