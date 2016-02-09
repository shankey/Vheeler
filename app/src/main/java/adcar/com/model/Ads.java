package adcar.com.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 09/02/16.
 */
public class Ads {

    String version;
    List<Ad> ads = new ArrayList<>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    @Override
    public String toString(){
        return "version = " + version + " ads = " + ads;
    }
}
