package adcar.com.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import adcar.com.cache.Cache;
import adcar.com.factory.Factory;

/**
 * Created by aditya on 04/02/16.
 */
public class Utility {

    public static SharedPreferences sp = (SharedPreferences) Factory.getInstance().get(Factory.SHARED_PREFERENCES);

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getDeviceId(){
        Context context = (Context)Factory.getInstance().get(Factory.BASE_CONTEXT);
         String androidId = Settings.Secure.getString(context.getContentResolver(),
                 Settings.Secure.ANDROID_ID);
         return androidId;
    }

    public static void saveToSharedPreference(String key, String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        Boolean didCommit = editor.commit();
        Log.i("VERSION", key + " " + didCommit);
    }

    public static String getFromSharedPreferences(String key){
        return sp.getString(key, null);
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, Integer adId) throws IOException {

        ContextWrapper cw = new ContextWrapper((Context)Factory.getInstance().get(Factory.BASE_CONTEXT));
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("ad_images", Context.MODE_WORLD_READABLE);
        // Create imageDir
        File adPath=new File(directory,adId+".jpg");
        Log.i("ADSSYNC", "file save path = " + adPath.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(adPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
                fos.close();
        }
        Cache.getCache().getImageMap().put(adId, bitmapImage);
        Log.i("IMAGE", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    public static synchronized Bitmap getFromInternalStorage(Integer adId){

        if(Cache.getCache().getImageMap().containsKey(adId)){
            return Cache.getCache().getImageMap().get(adId);
        }

        Log.i("GPSPROCESSOR", "image file read method start " + new Date());

        ContextWrapper cw = new ContextWrapper((Context)Factory.getInstance().get(Factory.BASE_CONTEXT));
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("ad_images", Context.MODE_PRIVATE);
        // Create imageDir
        File adPath=new File(directory,adId+".jpg");


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Log.i("GPSPROCESSOR", "imafe file read before decodeFile start " + new Date());
        Bitmap bitmap = BitmapFactory.decodeFile(adPath.getAbsolutePath(), options);
        Log.i("GPSPROCESSOR", "image file read method end " + new Date());

        Cache.getCache().getImageMap().put(adId, bitmap);
        return  bitmap;
    }

    public static Boolean isCharging(){
        Context context = (Context)Factory.getInstance().get(Factory.BASE_CONTEXT);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return  isCharging;
    }

    public static void sendMessageToHandler(Handler handler, Integer what, Object obj){
        Message msg = new Message();
        msg.obj = obj;
        msg.what = what;
        handler.sendMessage(msg);
    }
}
