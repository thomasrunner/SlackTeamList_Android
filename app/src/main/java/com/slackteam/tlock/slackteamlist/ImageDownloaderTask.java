package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

//Async Image Downloader
public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String fileurl = "";
    private Context appcontext;
    public ImageDownloaderTask(ImageView imageView, Context context) {
        appcontext = context;
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        fileurl = params[0];
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {

                    imageView.setImageBitmap(bitmap);

                    //Basic File Caching hashCode for filename
                    String filename=String.valueOf(fileurl.hashCode()).replace("-","");
                    String extStorageDirectory = appcontext.getFilesDir().toString();
                    OutputStream outStream = null;

                    File file = new File(extStorageDirectory + "/members/","m_" + filename + ".jpg" );
                    if(!file.exists()) {
                        file.getParentFile().mkdirs();
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        file.delete();
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        OutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.close();
                    } catch (Exception t) {
                        file.delete();
                        //throw t;
                    }

                } else {
                    imageView.setImageResource(R.drawable.reload_50px);
                }
            }
        }
    }

    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            return BitmapFactory.decodeStream(new FileInputStream(f),null,o);

        } catch (FileNotFoundException e) {}
        return null;
    }


    private Bitmap downloadBitmap(String url) {

        //Basic File Caching hashCode for filename
        String filename=String.valueOf(url.hashCode()).replace("-", "");
        String extStorageDirectory = appcontext.getFilesDir().toString();
        //OutputStream outStream = null;

        File file = new File(extStorageDirectory + "/members/","m_" +  filename + ".jpg" );

        //from Cache first
        Bitmap b = decodeFile(file);
        if (b != null)
            return b;

        //from web
        try {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    b = BitmapFactory.decodeStream(inputStream);
                    return b;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                ///Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}