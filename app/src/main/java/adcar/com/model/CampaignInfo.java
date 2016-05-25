package adcar.com.model;

/**
 * Created by adinema on 06/05/16.
 */
public class CampaignInfo implements Comparable<CampaignInfo> {

    private Integer id;
    private Integer campaignId;
    private Integer campaignInfoId;
    private Integer adId;
    private Integer areaId;
    private Integer version;
    private Integer status;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getVersion() {
        return version;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String toString(){
        return campaignId + " " + campaignInfoId + " " + status;
    }

    @Override
    public int compareTo(CampaignInfo campaignInfo) {
        return campaignInfo.getAdId();
    }
}
