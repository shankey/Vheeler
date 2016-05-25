package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 22/05/16.
 */
public class GetExhaustedCampaignRequest {

    List<ExhaustedCampaign> exhaustedCampaignList;

    public List<ExhaustedCampaign> getExhaustedCampaignList() {
        return exhaustedCampaignList;
    }

    public void setExhaustedCampaignList(List<ExhaustedCampaign> exhaustedCampaignList) {
        this.exhaustedCampaignList = exhaustedCampaignList;
    }

    public String toString(){
        return exhaustedCampaignList.toString();
    }
}
