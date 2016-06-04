package adcar.com.model.servertalkers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adinema on 25/05/16.
 */
public class GetExpiredCampaignResponse {

    private List<ExpiredCampaignResponse> expiredCampaigns;

    public List<ExpiredCampaignResponse> getExpiredCampaigns() {
        return expiredCampaigns;
    }

    public void setExpiredCampaigns(List<ExpiredCampaignResponse> expiredCampaigns) {
        this.expiredCampaigns = expiredCampaigns;
    }

    public String toString(){
        return expiredCampaigns.toString();
    }
}
