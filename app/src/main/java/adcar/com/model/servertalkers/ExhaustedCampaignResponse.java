package adcar.com.model.servertalkers;

/**
 * Created by adinema on 23/05/16.
 */
public class ExhaustedCampaignResponse {
    Integer campaignInfoId;
    String date;
    Integer active;

    public Integer getCampaignInfoId() {
        return campaignInfoId;
    }

    public void setCampaignInfoId(Integer campaignInfoId) {
        this.campaignInfoId = campaignInfoId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String toString(){
        return campaignInfoId + " - " + active + " - " + date;
    }
}
