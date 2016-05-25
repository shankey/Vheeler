package adcar.com.model.servertalkers;

import java.util.List;

import adcar.com.model.CampaignInfo;

/**
 * Created by adinema on 06/05/16.
 */
public class Campaigns {

    private Integer campaignId;
    private List<CampaignInfo> campaignInfos;

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public List<CampaignInfo> getCampaignInfos() {
        return campaignInfos;
    }

    public void setCampaignInfos(List<CampaignInfo> campaignInfos) {
        this.campaignInfos = campaignInfos;
    }

    public String toString(){
        return campaignId + " " + campaignInfos.toString();
    }
}
