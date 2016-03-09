package adcar.com.adcar;

import android.app.Application;

import adcar.com.cache.Cache;
import adcar.com.factory.Factory;
import adcar.com.handler.VersionHandler;

/**
 * Created by aditya on 04/03/16.
 */
public class VheelerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Factory.getInstance().initialize(getApplicationContext());
        Cache.getCache().initialize(getApplicationContext());

        new VersionHandler().SyncVersions();
    }
}
