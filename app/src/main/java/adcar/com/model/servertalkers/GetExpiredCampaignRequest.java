package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 25/05/16.
 */
public class GetExpiredCampaignRequest {

    private List<Integer> campaignIds;

    public List<Integer> getCampaignIds() {
        return campaignIds;
    }

    public void setCampaignIds(List<Integer> campaignIds) {
        this.campaignIds = campaignIds;
    }
}
