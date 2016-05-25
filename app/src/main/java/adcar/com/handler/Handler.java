package adcar.com.handler;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

import adcar.com.factory.Factory;
import adcar.com.network.NetworkManager;

/**
 * Created by aditya on 15/02/16.
 */
public class Handler {

    public static Gson gson = new GsonBuilder().create();
    public static NetworkManager networkManager = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);
    public static SharedPreferences sp = (SharedPreferences) Factory.getInstance().get(Factory.SHARED_PREFERENCES);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
}
