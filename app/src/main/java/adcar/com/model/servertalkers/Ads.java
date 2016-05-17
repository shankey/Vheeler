package adcar.com.model.servertalkers;

import java.util.ArrayList;
import java.util.List;

import adcar.com.model.Ad;

/**
 * Created by aditya on 09/02/16.
 */
public class Ads {

    List<Ad> ads = new ArrayList<>();


    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    @Override
    public String toString(){
        return " ads = " + ads;
    }
}
