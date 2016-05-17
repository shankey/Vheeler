package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 06/05/16.
 */
public class Campaigns {

    private Integer campaignId;
    private List<AreaAd> areaAds;

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public List<AreaAd> getAreaAds() {
        return areaAds;
    }

    public void setAreaAds(List<AreaAd> areaAds) {
        this.areaAds = areaAds;
    }

    public String toString(){
        return campaignId + " " + areaAds.toString();
    }
}
