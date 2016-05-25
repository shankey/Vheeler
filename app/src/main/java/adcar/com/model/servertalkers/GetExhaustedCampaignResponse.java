package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 23/05/16.
 */
public class GetExhaustedCampaignResponse {

    private List<ExhaustedCampaignResponse> exhaustedCampaignList;

    public List<ExhaustedCampaignResponse> getExhaustedCampaignList() {
        return exhaustedCampaignList;
    }

    public void setExhaustedCampaignList(List<ExhaustedCampaignResponse> exhaustedCampaignList) {
        this.exhaustedCampaignList = exhaustedCampaignList;
    }

    public String toString(){
        return exhaustedCampaignList.toString();
    }
}
