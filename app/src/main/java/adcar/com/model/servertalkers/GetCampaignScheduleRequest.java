package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 09/05/16.
 */
public class GetCampaignScheduleRequest {

    private List<CampaignInfoIdentifier> campaignInfoIds;

    public List<CampaignInfoIdentifier> getCampaignInfoIds() {
        return campaignInfoIds;
    }

    public void setCampaignInfoIds(List<CampaignInfoIdentifier> campaignInfoIds) {
        this.campaignInfoIds = campaignInfoIds;
    }
}
