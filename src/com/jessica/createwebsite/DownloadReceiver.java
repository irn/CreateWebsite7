package com.jessica.createwebsite;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.artifex.mupdfdemo.MuPDFActivity;

/**
* Created with IntelliJ IDEA.
* User: ruslan
* Date: 25.03.13
* Time: 22:30
* To change this template use File | Settings | File Templates.
*/
public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //To change body of implemented methods use File | Settings | File Templates.
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            Bundle bundle = intent.getExtras();

            if (bundle != null){
                long downloadedId = bundle.getLong("extra_download_id");
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadedId);
                Cursor cursor = dm.query(query);
                if (cursor != null){
                    cursor.moveToFirst();
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL){
                        String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        if (filename != null){
                            Intent pdfIntent = new Intent(context, MuPDFActivity.class);
                            pdfIntent.setAction(Intent.ACTION_VIEW);
                            pdfIntent.setData(Uri.parse(filename));
                            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(pdfIntent);
                        }
                    }
                    cursor.close();
                }

            }


        }
    }

}
