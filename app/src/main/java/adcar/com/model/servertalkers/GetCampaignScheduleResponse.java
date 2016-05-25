package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by adinema on 19/05/16.
 */
public class GetCampaignScheduleResponse {

    private List<CampaignSchedule> campaignSchedules;

    public List<CampaignSchedule> getCampaignSchedules() {
        return campaignSchedules;
    }

    public void setCampaignSchedules(List<CampaignSchedule> campaignSchedules) {
        this.campaignSchedules = campaignSchedules;
    }
}
