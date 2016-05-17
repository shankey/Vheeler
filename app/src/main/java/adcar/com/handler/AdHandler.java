package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import adcar.com.database.dao.AdDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.CampaignInfo;
import adcar.com.model.servertalkers.Ads;
import adcar.com.model.servertalkers.AreaAd;
import adcar.com.model.servertalkers.Campaigns;
import adcar.com.model.servertalkers.GetCampaigns;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.ImageDownloader;
import adcar.com.network.UrlPaths;

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
