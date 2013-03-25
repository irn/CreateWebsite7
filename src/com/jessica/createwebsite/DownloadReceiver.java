package com.jessica.createwebsite;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import com.artifex.mupdfdemo.MuPDFActivity;

import java.io.File;
import java.io.FileNotFoundException;

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
                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(pdfIntent);
                        }
                    }
                    cursor.close();
                }

            }


        }
    }

    protected String convertMediaUriToPath(Context context, Uri uri) {
        String [] proj={MediaStore.Files.FileColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
