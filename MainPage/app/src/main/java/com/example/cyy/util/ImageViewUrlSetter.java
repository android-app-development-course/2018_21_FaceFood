package com.example.cyy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;

import javax.xml.datatype.Duration;

public class ImageViewUrlSetter extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Context context;

    public ImageViewUrlSetter(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }
    public void set(String...url){
        this.execute(url);
    }
    private String getFileNameFromUrl(String Url){
        for(int i=Url.length()-1;i>=0;--i){
            if(Url.charAt(i)=='/'){
                return Url.substring(i+1,Url.length());
            }
        }
        return Url;
    }
    private boolean isFileDateOut(File f){
        if(!f.exists()) return true;
        Date lastChanged = new Date(f.lastModified());
        long duration = new Date().getTime()-lastChanged.getTime();
        double durationInMin = duration/(1000*60);
        return durationInMin > 2;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        String fileNameStored =  getFileNameFromUrl(urldisplay);
        String parent = "/data/data/com.example.zzk.mainpage/files" ;
        Bitmap mIcon11 = null;
        try {
            File f = new File(parent,fileNameStored);
            if(isFileDateOut(f)){ //如果超时就更新一下
                InputStream in = new java.net.URL(urldisplay).openStream();
                FileOutputStream out = context.openFileOutput(fileNameStored,Context.MODE_PRIVATE);
                byte[] bucket = new byte[1024*1024];
                int length;
                while((length=in.read(bucket))>0)
                    out.write(bucket,0,length);
                in.close();
                out.close();
            }
            InputStream in = context.openFileInput(getFileNameFromUrl(urldisplay));
            mIcon11 =  BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
