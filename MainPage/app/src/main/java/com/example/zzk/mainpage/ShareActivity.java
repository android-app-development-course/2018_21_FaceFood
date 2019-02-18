package com.example.zzk.mainpage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyy.module.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import id.zelory.compressor.Compressor;
import com.example.cyy.util.BackEnd;

public class ShareActivity extends AppCompatActivity {

    private GridView gridView1;              //网格显示缩略图
    private Button btnRelease;               //发布按钮
    private ImageView backAction;
    private TextView releaseAction;
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private ArrayList<Uri> imageUri;         //图片uri,注意由于第一张不被包含，因此不包含第一张的uri，即图片数比imageItem少1
    private ArrayList<String> imageNames;   //系统内图片文件名，为了能正确识别后缀而使用
    private SimpleAdapter simpleAdapter;     //适配器
    private String userName;                 //用户名

    private ImageView ig;

    private Spinner spinner;
    private EditText etDynamic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_share);

        ig = findViewById(R.id.ig);

        btnRelease = findViewById(R.id.btnRelease);
        etDynamic  = findViewById(R.id.etDynamic);
        backAction = findViewById(R.id.ivBack);
        releaseAction = findViewById(R.id.evRelease);
        spinner=findViewById(R.id.SpSelectPlace);
        String[] locations = new String[]{"陶园","雍园","沁园"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //-----------------这里暂时不使用SharedPreferences
        /*
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userName = pref.getString("student_number","unknownName");
        */
        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("nickname", "匿名用户");

        /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */

        gridView1 = findViewById(R.id.imageGridView);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.add);        //获取添加的图片资源
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<>();
        map.put("itemImage",bmp);
        imageItem.add(map);
        imageUri = new ArrayList<>();
        imageNames = new ArrayList<>();

        simpleAdapter = new SimpleAdapter(this,imageItem,R.layout.griditem_addpic,
                new String[]{"itemImage"},new int[]{R.id.imageView1});

        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if(view instanceof ImageView && o instanceof Bitmap){
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap)o);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(imageItem.size()==6){
                    //除去了默认图片
                    Toast.makeText(ShareActivity.this,"图片数量不可超过9张",Toast.LENGTH_SHORT).show();
                }
                else if (i == 0){   //这里表示点击了gridView中的第i项
                    Toast.makeText(ShareActivity.this,"请选择需要添加的图片",Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,IMAGE_OPEN);
                }
                else {
                    dialog(i);
                }
            }
        });


        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //              uploadSingleImage((Bitmap) imageItem.get(0).get("itemImage"),"firstName.jpg");
                //               ig.setImageBitmap((Bitmap)imageItem.get(0).get("itemImage"));
                //               List<String> ass = new ArrayList<>();
                //               ass.add("没图片.jpg");
                //               ass.add("有图片.jpg");
//                uploadInfo(ass,2);

                /*
                Bitmap sin_bmp = (Bitmap) imageItem.get(0).get("itemImage");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                sin_bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                sin_bmp = BitmapFactory.decodeStream(is);
                ig.setImageBitmap(sin_bmp);
                */

                upload();
            }
        });

        backAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareActivity.this.finish();
            }
        });

        releaseAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        // -------- 请求权限 ----------
        if (ContextCompat.checkSelfPermission(ShareActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShareActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (ContextCompat.checkSelfPermission(ShareActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShareActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    //获取图片路径 响应startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //打开图片
        if(resultCode == RESULT_OK && requestCode == IMAGE_OPEN){
            Uri uri = data.getData();

            if(!TextUtils.isEmpty(uri.getAuthority())){
                //查询图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //如果没找到图片就返回
                if(null == cursor)return;

                //获取图片
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imageNames.add(pathImage.substring(pathImage.lastIndexOf("."),pathImage.length()));

                //----------压缩文件---------
                File imageFile = new File(pathImage);
                if(imageFile.exists()) {
                    Log.i("shareActivity", "file exists");
                }
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
//                int imageHeight = options.outHeight;
//                int imageWidth = options.outWidth;
                try {
                    File compressedFile = new Compressor(this)
                            .setMaxHeight(500)
                            .setMaxWidth(500)
                            .compressToFile(imageFile);
                    uri = Uri.fromFile(compressedFile);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                imageUri.add(uri);
                //----------------------------------
            }
        }
    }

    //刷新图片

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage))
        {
//            Bitmap addmap = BitmapFactory.decodeFile(pathImage);
            Uri addmap = imageUri.get(imageUri.size() - 1);
            HashMap<String,Object> map = new HashMap<>();
            map.put("itemImage",addmap);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem,R.layout.griditem_addpic,
                    new String[]{"itemImage"},
                    new int[]{R.id.imageView1});

            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object o, String s) {
                    if(view instanceof ImageView && o instanceof Uri){
                        ImageView i = (ImageView)view;
//                        i.setImageBitmap((Bitmap)o);
                        i.setImageURI((Uri)o);
                        return true;
                    }
                    else if(view instanceof ImageView && o instanceof Bitmap) {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageBitmap((Bitmap)o);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            pathImage = null;
        }
    }

    protected void dialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
        builder.setMessage("确定移除已添加的图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                imageItem.remove(position);
                imageUri.remove(position-1);
                imageNames.remove(position-1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    protected void uploadSingleImage(Uri bmpUri,String bmpName)
    {

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(bmpUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("student_number",userName);
        params.put("file",is,bmpName);
        client.post(BackEnd.ip+"/upload", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("uploadImage","succeed");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("uploadImage Fail",statusCode+"|||"+error.toString());
            }
        });
    }

    protected void uploadInfo(List<String> AllBmpNames,int picCount)
    {
        JSONArray imageNames  = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

            jsonObject.put("userID",UserInfo.getUser().getId());
            jsonObject.put("username",sharedPreferences.getString("nickname", "匿名用户"));            //这里后期需要处理名字
            jsonObject.put("location",spinner.getSelectedItem().toString());        //这里需要确认获取的值是否成功
            jsonObject.put("content",etDynamic.getText().toString());
            jsonObject.put("pictureCount",picCount);            //图片数量
            for(int i=0;i<picCount;i++) {
                imageNames.put(AllBmpNames.get(i));
            }
            jsonObject.put("picNames",imageNames);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String dateTime = new String(simpleDateFormat.format(date));

            jsonObject.put("time",dateTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("json",jsonObject.toString());

        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ShareActivity.this, BackEnd.ip+"/postNormal", entity, "application/json",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String json = new String(responseBody, "utf-8");
                            JSONObject jsonObject = new JSONObject(json);

                            String postStatus = jsonObject.getString("post");
                            if(postStatus.equals("success")) {
                                Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                                ShareActivity.this.finish();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }


    protected void upload()
    {
        List<String> picAllNames = new ArrayList<>();
        String picName = new String();
        int imageUriSize = imageUri.size();
        Date date;
        SimpleDateFormat simpleDateFormat;
        for(int i=0;i<imageUriSize;i++)
        {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = new Date(System.currentTimeMillis());
            String time = new String(simpleDateFormat.format(date));
            picName=new String(userName+i+time+imageNames.get(i));
            picName.trim();
            picName = picName.replace(' ','_');
            picName = picName.replace(":","_");
            picName = picName.replace("-","_");
            picAllNames.add(picName);

            uploadSingleImage(imageUri.get(i),picName);
        }

        uploadInfo(picAllNames,imageUriSize);
    }
}
