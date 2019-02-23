package com.example.zzk.mainpage;

        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextUtils;
        import android.text.TextWatcher;
        import android.view.Gravity;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.Window;
        import android.widget.EditText;
        import android.widget.TextView;

        import butterknife.BindView;
        import butterknife.ButterKnife;



/**

 * Created by WW on 2017/2/23.

 * 评论对话框

 */

public class CommentDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.tv_commit)
    TextView tv_commit;//提交


    @BindView(R.id.et_comment)
    EditText et_comment;//评论内容

//    @Bind(R.id.tv_location)
//
//    TextView tv_location;//定位

//    @Bind(R.id.view_line)
//
//    View view_line;//竖线
//
//    @Bind(R.id.ib_delete)
//
//    ImageButton ib_delete;//删除按钮
//
//    @Bind(R.id.cb_anonymous)
//
//    CheckBox cb_anonymous;//匿名

    private Context context;
    private OnCommitListener listener;



    public CommentDialog(Context context) {

        this(context, R.style.inputDialog);
        this.context = context;
        Window window =this.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);//出现在底部

        }


    }

    public CommentDialog(Context context, int themeResId) {
        super(context, themeResId);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment_layout);

        ButterKnife.bind(this);

        initListener();
    }



    private void initListener() {

        //设置显示对话框时的返回键的监听
        this.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0)
                    CommentDialog.this.cancel();
                return false;
            }

        });

        //设置EditText内容改变的监听
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tv_commit.setBackgroundColor(Color.parseColor("#008000"));
                    tv_commit.setClickable(true);
                }else {
                    tv_commit.setBackgroundColor(Color.parseColor("#ff888888"));
                    tv_commit.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //提交
        tv_commit.setOnClickListener(this);


    }

    public void setOnCommitListener(OnCommitListener listener) {
        this.listener = listener;
    }

    //数据回调接口
    public interface OnCommitListener {
        void onCommit(EditText et, View v);//提交数据//该接口在DetailedActivity.java中使用
        // void onGetLocation();//定位
        // void onDeleteLocation();//删除定位
        // void onAnonymousChecked(CompoundButton buttonView, boolean isChecked);
    }



//    public void setOnCommitListener(OnCommitListener listener) {
//
//        this.listener = listener;
//
//    }



//    public interface OnCommitListener {
//        void onCommit(EditText et, View v);//提交数据
//
//        void onAnonymousChecked(CompoundButton buttonView, boolean isChecked);
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                //tv_commit.setText("aa");
                if (null != listener) {
                    String strName = et_comment.getText().toString();
                    if(strName.length()!=0){
                        listener.onCommit(et_comment, v);
                        CommentDialog.this.cancel();
                    }
                    else
                        break;
                }
                break;
        }

    }



//    public void setLocationState(boolean state, String str) {
//
//        if (state) {//定位状态
//
//            tv_location.setCompoundDrawables(DrawableUtil.
//
//                    setDrawableLeft(context, R.mipmap.icon_location_bg_pressed), null, null, null);
//
//            view_line.setVisibility(View.VISIBLE);
//
//            ib_delete.setVisibility(View.VISIBLE);
//
//            tv_location.setText(str);
//
//        } else {
//
//            //设置drawableLeft
//
//            tv_location.setCompoundDrawables(DrawableUtil.
//
//                    setDrawableLeft(context, R.mipmap.icon_location_bg), null, null, null);
//
//            view_line.setVisibility(View.GONE);
//
//            ib_delete.setVisibility(View.GONE);
//
//            tv_location.setText("点击获取位置");
//
//        }
//
//    }

}
