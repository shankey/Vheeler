package adcar.com.adcar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import adcar.com.cache.Cache;
import adcar.com.coordinates.CoordinateAlgorithms;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.gps.AndroidGpsListener;
import adcar.com.gps.GoogleApiClientListener;
import adcar.com.handler.AdHandler;
import adcar.com.handler.AreaHandler;
import adcar.com.handler.CoordinatesHandler;
import adcar.com.handler.VersionHandler;
import adcar.com.model.Ad;
import adcar.com.model.Area;
import adcar.com.model.Areas;
import adcar.com.model.Coordinate;
import adcar.com.model.CoordinateBatchEntity;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.ImageDownload;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;


public class SettingsActivity extends AppCompatActivity {


    GoogleApiClientListener googleApiClientListener = null;
    CoordinateDAO coordinateDAO = null;
    public static SettingsActivity activity;

    //UI elements
    EditText area = null;
    EditText latitude = null;
    EditText longitude = null;
    EditText time = null;
    Button flush_coordinates_btn = null;
    Button show_coordinates_btn = null;
    Button network_call_btn = null;
    Button internet_check_btn = null;
    Button sync_area_btn = null;
    Button get_image_btn = null;
    Button check_area_btn = null;
    Button check_versions_btn = null;
    Button show_areas_btn = null;
    Button show_ads_btn = null;
    Button display_ad_btn = null;
    ImageView ad_view = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        AndroidGpsListener.setSettingsActivity(activity);
        Log.i("GPS", "OnCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //setScheduler();

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.GONE
//        );


        //UI elements initialization
        area = (EditText)findViewById(R.id.area_text);
        latitude = (EditText)findViewById(R.id.latitude_text);
        longitude = (EditText)findViewById(R.id.longitude_text);
        time = (EditText)findViewById(R.id.time_text);
        flush_coordinates_btn = (Button)findViewById(R.id.flush_coordinates_btn);
        network_call_btn = (Button)findViewById(R.id.network_call_btn);
        internet_check_btn = (Button)findViewById(R.id.internet_check_btn);
        sync_area_btn = (Button)findViewById(R.id.sync_area_id);
        get_image_btn = (Button)findViewById(R.id.get_image_btn);
        check_area_btn = (Button)findViewById(R.id.check_area_btn);
        check_versions_btn = (Button)findViewById(R.id.check_versions_btn);
        show_ads_btn = (Button)findViewById(R.id.show_ads_btn);
        show_areas_btn = (Button)findViewById(R.id.show_areas_btn);
        display_ad_btn = (Button)findViewById(R.id.display_ad_btn);
        ad_view = (ImageView)findViewById(R.id.ad_view);

        coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);

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

        flush_coordinates_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoordinateBatchEntity coordinateBatchEntity = new CoordinateBatchEntity();
                coordinateBatchEntity.setLi(coordinateDAO.getCoordinates());
                coordinateBatchEntity.setDeviceId(Cache.deviceId);
                new CoordinatesHandler().sendCoordinatesToServer(coordinateBatchEntity);
            }
        });

        display_ad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = null;
                try {
                    bitmap = Utility.getFromInternalStorage(1);
                    ad_view.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        sync_area_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AreaHandler().SyncAreas();
            }
        });

        show_coordinates_btn = (Button)findViewById(R.id.show_coordinates_btn);
        show_coordinates_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TableActivity.class);
                intent.putExtra("ACTION", 1);
                startActivity(intent);
            }
        });

        network_call_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                        UrlPaths.GET_AREAS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Gson gson = new GsonBuilder().create();
                                Areas areas = gson.fromJson(response.toString(), Areas.class);
                                AreaDAO areaDAO = new AreaDAO(getBaseContext());
                                for(Area area : areas.getAreas()){
                                    areaDAO.addArea(area);
                                }

                                Log.i("RESPONSE", "bhai mast" + areas.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.i("RESPONSE", error.toString());
                            }
                        });
                NetworkManager.getInstance(getBaseContext()).addToRequestQueue(csr);
            }
        });

        internet_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isNetworkAvailable(getBaseContext())){
                    Utility.saveToSharedPreference(Strings.VERSION_AD, "0");
                    Toast.makeText(getBaseContext(), "Internet Available" + Utility.getDeviceId(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), "Internet NOT Available", Toast.LENGTH_LONG).show();
                }
            }
        });


        get_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdHandler adHandler = new AdHandler();
                Ad ad = new Ad();
                ad.setAreaId(1);
                ad.setStatus(0);
                ad.setUrl("http://developer.chrome.com/extensions/examples/api/idle/idle_simple/sample-128.png");
                new ImageDownload().downloadImage(ad);
            }
        });

        check_area_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coordinate co = new Coordinate();
                co.setLatitude(1.6785);
                co.setLongitude(4.567345);
                Toast.makeText(getApplicationContext(), ""+CoordinateAlgorithms.getInsideAreaId(co), Toast.LENGTH_LONG).show();
            }
        });

        check_versions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VersionHandler().SyncVersions();
            }
        });

        show_ads_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TableActivity.class);
                intent.putExtra("ACTION", 2);
                startActivity(intent);
            }
        });

        show_areas_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TableActivity.class);
                intent.putExtra("ACTION", 3);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

//    public void setScheduler(){
//        Log.i("SCHEDULER", "setting scheduler");
//        Calendar cal = Calendar.getInstance();
//        Intent intent = new Intent(this, ScheduleReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 1000, pendingIntent);
//        Toast.makeText(this, "Set Scheduler last line", Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        changeVisibility();

    }

    public static SettingsActivity getSettingsActivityInstance(){
        return activity;
    }

    private void changeVisibility(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    //Getters and Setters
    public EditText getLatitude() {
        return latitude;
    }

    public void setLatitude(EditText latitude) {
        this.latitude = latitude;
    }

    public EditText getLongitude() {
        return longitude;
    }

    public void setLongitude(EditText longitude) {
        this.longitude = longitude;
    }

    public EditText getTime() {
        return time;
    }

    public void setTime(EditText time) {
        this.time = time;
    }

    public EditText getArea() {
        return area;
    }

    public void setArea(EditText area) {
        this.area = area;
    }

    public ImageView getAd_view() {
        return ad_view;
    }

    public void setAd_view(ImageView ad_view) {
        this.ad_view = ad_view;
    }
}
