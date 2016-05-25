package adcar.com.handler;

import android.util.Log;

import java.util.List;

import adcar.com.database.dao.AdDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.network.ImageDownloader;

/**
 * Created by adinema on 16/05/16.
 */
public class AdHandler extends Handler {

    public static AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);
    public static ImageDownloader imageDownloader = new ImageDownloader();

    public void SyncAds(){
        Log.i("ADSYNC", "Trying to sync ads");
        List<Ad> adList = adDAO.getUnsyncedAds(0);

        for(Ad ad: adList){
            Log.i("ADSYNC", " trying to download ad "+ ad);
            imageDownloader.downloadImage(ad);
        }

    }
}
