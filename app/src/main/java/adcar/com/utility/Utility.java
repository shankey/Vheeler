package adcar.com.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static void saveToSharedPreference(String key, String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getFromSharedPreferences(String key){
        return sp.getString(key, null);
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, Integer areaId) throws IOException {

        ContextWrapper cw = new ContextWrapper((Context)Factory.getInstance().get(Factory.BASE_CONTEXT));
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("ad_images", Context.MODE_PRIVATE);
        // Create imageDir
        File adPath=new File(directory,areaId+".jpg");

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

        Log.i("IMAGE", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }
}
