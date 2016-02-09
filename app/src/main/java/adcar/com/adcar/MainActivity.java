package adcar.com.adcar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import adcar.com.cache.Cache;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.gps.GoogleApiClientListener;
import adcar.com.handler.AdHandler;
import adcar.com.handler.AreaHandler;
import adcar.com.handler.CoordinatesHandler;
import adcar.com.model.Ad;
import adcar.com.model.Area;
import adcar.com.model.Areas;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.ImageDownload;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.polling.ScheduleReciever;
import adcar.com.utility.Utility;


public class MainActivity extends ActionBarActivity {


    GoogleApiClientListener googleApiClientListener = null;
    CoordinateDAO coordinateDAO = null;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Factory.getInstance().initialize(getApplicationContext());
        Cache.getCache().initialize(getApplicationContext());
        Log.i("GPS", "OnCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(googleApiClientListener == null){
            googleApiClientListener = new GoogleApiClientListener(this);
            googleApiClientListener.start();
        }

        setScheduler();


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

        coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);


        flush_coordinates_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CoordinatesHandler().sendCoordinatesToServer(coordinateDAO.getCoordinates());
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
                Intent intent = new Intent(MainActivity.this, TableActivity.class);
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
                    Toast.makeText(getBaseContext(), "Internet Available", Toast.LENGTH_LONG).show();
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

    public void setScheduler(){
        Log.i("SCHEDULER", "setting scheduler");
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, ScheduleReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 1000, pendingIntent);
        Toast.makeText(this, "Set Scheduler last line", Toast.LENGTH_LONG).show();
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
}
