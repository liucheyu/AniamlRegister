package com.lcypj.animalregister;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ParseImage extends AsyncTask<String, Void, Bitmap> {


    View view;

    public ParseImage(View view){
        this.view = view;
    }


    Bitmap[] bitmap = new Bitmap[1];

    @Override
    protected Bitmap doInBackground(String... strings) {


        try{
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            int v = connection.getContentLength() >0 ? connection.getContentLength() : 0;
            if(v>0){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                bitmap[0] = BitmapFactory.decodeStream(in);

            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return bitmap[0];
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        ImageView animailImage = view.findViewById(R.id.animalImage);
        animailImage.setImageBitmap(bitmap);

    }
}
