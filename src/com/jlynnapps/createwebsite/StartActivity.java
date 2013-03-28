package com.jlynnapps.createwebsite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.artifex.mupdfdemo.MuPDFActivity;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class StartActivity extends Activity
{
    private static final String PDF_FILE = "caw.pdf";

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
                if (!StartActivity.this.isFinishing())
                    openPdfFile();
            }
        }, 2000);

    }


    public static void startViewDocument(Context context, String path){

        Intent pdfIntent = new Intent(context, MuPDFActivity.class);
        pdfIntent.setAction(Intent.ACTION_VIEW);
        pdfIntent.setData(Uri.parse(path));
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(pdfIntent);
    }

    private void openPdfFile(){
        Intent intent = new Intent(this, MuPDFActivity.class);

        File f = new File (getCacheDir(), PDF_FILE);

        try {
            if (!f.exists()){
                f.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(f);
                FileChannel outChannel = fileOutputStream.getChannel();
                InputStream is = getAssets().open(PDF_FILE);
                ReadableByteChannel inChannel = Channels.newChannel(is);
                long len = is.available();

                outChannel.transferFrom(inChannel, 0,  len);
                fileOutputStream.flush();
                outChannel.close();
                fileOutputStream.close();

                inChannel.close();
                is.close();
            }

            intent.setData(Uri.fromFile(f));
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
