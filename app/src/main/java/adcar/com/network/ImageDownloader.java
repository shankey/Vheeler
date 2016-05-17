package adcar.com.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.IOException;

import adcar.com.adcar.R;
import adcar.com.database.dao.AdDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 09/02/16.
 */
public class ImageDownloader {

    final AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);
    NetworkManager nw = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);

    public void downloadImage(final Ad ad){
        ImageRequest request = new ImageRequest(UrlPaths.BASE_URL + ad.getUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        try {
                            Utility.saveToInternalStorage(bitmap, ad.getAdId());
                            adDAO.updateAdStatus(ad);
                        } catch (IOException e) {
                            Log.e("ADSYNC", "error downloading", e);
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
// Access the RequestQueue through your singleton class.
        nw.addToRequestQueue(request);
    }
}
