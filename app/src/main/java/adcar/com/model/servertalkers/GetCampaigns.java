package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 06/05/16.
 */
public class GetCampaigns {

    private List<Campaigns> campaigns;

    public List<Campaigns> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaigns> campaigns) {
        this.campaigns = campaigns;
    }

    public String toString(){
        return campaigns.toString();
    }
}
