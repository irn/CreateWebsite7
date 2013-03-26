package com.jessica.createwebsite;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.artifex.mupdfdemo.MuPDFActivity;

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
        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (id == -1){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getString(R.string.pdf_url)));
            id = dm.enqueue(request);
            preferences.edit().putLong("downloadedId", id).commit();
        } else {
            openStoredFile(dm, id);
            finish();
        }

    }

    private void openStoredFile(DownloadManager dm, long id){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = dm.query(query);
        if (cursor != null){
            cursor.moveToFirst();
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_SUCCESSFUL){
                String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                if (filename != null){
                    Intent pdfIntent = new Intent(this, MuPDFActivity.class);
                    pdfIntent.setAction(Intent.ACTION_VIEW);
                    pdfIntent.setData(Uri.parse(filename));
                    pdfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pdfIntent);
                }
            }
            cursor.close();
        }
    }

}
