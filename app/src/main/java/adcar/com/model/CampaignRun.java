package adcar.com.model;

import java.util.Date;

/**
 * Created by adinema on 06/05/16.
 */
public class CampaignRun {

    private Integer id;
    private Integer campaignInfoId;
    private Date date;
    private Integer active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCampaignInfoId() {
        return campaignInfoId;
    }

    public void setCampaignInfoId(Integer campaignInfoId) {
        this.campaignInfoId = campaignInfoId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
