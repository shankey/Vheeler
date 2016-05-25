package adcar.com.model.servertalkers;

import java.util.List;

import adcar.com.model.CampaignRun;

/**
 * Created by adinema on 06/05/16.
 */
public class CampaignSchedule {

    private Integer campaignInfoId;
    private List<CampaignRun> schedule;

    public Integer getCampaignInfoId() {
        return campaignInfoId;
    }

    public void setCampaignInfoId(Integer campaignInfoId) {
        this.campaignInfoId = campaignInfoId;
    }

    public List<CampaignRun> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<CampaignRun> schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString(){
        return schedule.toString();
    }
}
