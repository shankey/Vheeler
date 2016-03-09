package adcar.com.adcar;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.List;

import adcar.com.cache.Cache;
import adcar.com.coordinates.CoordinateAlgorithms;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.gps.GoogleApiClientListener;
import adcar.com.handler.AdHandler;
import adcar.com.handler.AreaHandler;
import adcar.com.handler.CoordinatesHandler;
import adcar.com.handler.VersionHandler;
import adcar.com.model.Ad;
import adcar.com.model.Area;
import adcar.com.model.Areas;
import adcar.com.model.Coordinate;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.ImageDownload;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.polling.ScheduleReciever;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;


public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {


    GoogleApiClientListener googleApiClientListener = null;
    CoordinateDAO coordinateDAO = null;
    private GestureDetectorCompat mDetector;


    //UI elements
    ImageView ad_main = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);


        Log.i("GPS", "OnCreate Called");


        if(googleApiClientListener == null){
            googleApiClientListener = new GoogleApiClientListener(this);
            googleApiClientListener.start();
        }

        setScheduler();

        ad_main = (ImageView)findViewById(R.id.ad_main);
        if(Cache.LAST_AD!=null){
            Bitmap bm = null;
            try{
                bm = Utility.getFromInternalStorage(Cache.LAST_AD);
                ad_main.setImageBitmap(bm);
            }catch (Exception e){
                Log.i("VHEELER", "Could Not Retrieve Ad" + e.getStackTrace());
            }
        }

        coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);

        //When the backbuttons show, hide them
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            changeVisibility();

                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        googleApiClientListener.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        googleApiClientListener.disconnect();
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        getSupportActionBar().show();
        HideToolbar runner = new HideToolbar();
        runner.execute();
        Log.i("GESTURE", "double tap");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    private class HideToolbar extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2500,0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            // execution of result of Long time consuming operation
            getSupportActionBar().hide();
        }


    }

    public void setScheduler(){
        Log.i("SCHEDULER", "setting scheduler");
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, ScheduleReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 1000, pendingIntent);
        Toast.makeText(this, "Set Scheduler last line", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        changeVisibility();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);

                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void changeVisibility() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    //Getters and Setters

    public ImageView getAd_main() {
        return ad_main;
    }

    public void setAd_main(ImageView ad_main) {
        this.ad_main = ad_main;
    }
}
