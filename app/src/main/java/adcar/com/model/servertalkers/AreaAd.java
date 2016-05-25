package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 06/05/16.
 */
public class AreaAd {

    private Integer campaignInfoId;
    private Integer areaId;
    private Integer adId;
    private Integer version;
    private Integer active;


    public Integer getCampaignInfoId() {
        return campaignInfoId;
    }

    public void setCampaignInfoId(Integer campaignInfoId) {
        this.campaignInfoId = campaignInfoId;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String toString(){
        return areaId + " " + adId + " " + version;
    }
}
