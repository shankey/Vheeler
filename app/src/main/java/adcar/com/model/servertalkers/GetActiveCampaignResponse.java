package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 19/05/16.
 */
public class GetActiveCampaignResponse {

    private List<Campaigns> campaigns;

    public List<Campaigns> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaigns> campaigns) {
        this.campaigns = campaigns;
    }
}
