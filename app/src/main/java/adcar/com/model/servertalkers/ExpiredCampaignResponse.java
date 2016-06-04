package adcar.com.model.servertalkers;

/**
 * Created by adinema on 25/05/16.
 */
public class ExpiredCampaignResponse {

    private Integer campaignId;
    private Integer active;

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String toString(){
        return campaignId + " - " + active;
    }
}
