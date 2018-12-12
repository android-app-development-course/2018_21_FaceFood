package com.example.zzk.mainpage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class LoadNetImageView extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;

    public LoadNetImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... url) {

        String imageUrl = url[0];
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (Exception E) {

        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

}
