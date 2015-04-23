package com.unccstudio.gems.gems;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;


public class DemoActivity extends ActionBarActivity {
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.logo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.hide();

        VideoView vv = (VideoView)this.findViewById(R.id.videoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.gemtest3_mobile720p;
        vv.setVideoURI(Uri.parse(uri));
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) vv.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        vv.setLayoutParams(params);
        vv.start();
    }

    public void videoPlayer(String path, String fileName, boolean autoplay){
        //get current window information, and set format, set it up differently, if you need some special effects
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //the VideoView will hold the video
        VideoView videoHolder = new VideoView(this);
        //MediaController is the ui control howering above the video (just like in the default youtube player).
        videoHolder.setMediaController(new MediaController(this));
        //assing a video file to the video holder
        videoHolder.setVideoURI(Uri.parse(path+"/"+fileName));
        //get focus, before playing the video.
        videoHolder.requestFocus();
        if(autoplay){
            videoHolder.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
