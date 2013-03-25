package com.jessica.createwebsite;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.net.URL;

public class StartActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        SharedPreferences preferences = getSharedPreferences("prefs", ContextWrapper.MODE_PRIVATE);
        long id = preferences.getLong("downloadedId", -1);
        if (id == -1){
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getString(R.string.pdf_url)));
            id = dm.enqueue(request);
            preferences.edit().putLong("downloadedId", id).commit();
        } else {

        }

    }

    class DownloadFileAsyncTask extends AsyncTask<URL, Integer, Integer>{

        @Override
        protected Integer doInBackground(URL... params) {
            if (params != null && params.length > 0){
                URL url = params[0];
//                url.openConnection().
            }
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
