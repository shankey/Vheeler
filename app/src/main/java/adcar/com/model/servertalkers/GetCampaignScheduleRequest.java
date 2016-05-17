package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 09/05/16.
 */
public class GetCampaignScheduleRequest {

    private List<CampaignAreaAd> campaigns;

    public List<CampaignAreaAd> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<CampaignAreaAd> campaigns) {
        this.campaigns = campaigns;
    }
}
