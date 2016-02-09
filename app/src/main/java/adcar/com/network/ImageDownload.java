package adcar.com.network;

import android.graphics.Bitmap;

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
public class ImageDownload {

    final AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);
    NetworkManager nw = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);

    public void downloadImage(final Ad ad){
        ImageRequest request = new ImageRequest(ad.getUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        try {
                            Utility.saveToInternalStorage(bitmap, ad.getAreaId());
                            Ad ad = new Ad();
                            ad.setStatus(1);
                            adDAO.addAd(ad);
                        } catch (IOException e) {
                            Ad ad = new Ad();
                            ad.setStatus(0);
                            adDAO.addAd(ad);
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
