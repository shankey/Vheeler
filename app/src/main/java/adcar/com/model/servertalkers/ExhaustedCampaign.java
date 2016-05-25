package adcar.com.model.servertalkers;

/**
 * Created by adinema on 22/05/16.
 */
public class ExhaustedCampaign {

    Integer campaignInfoId;
    String date;


    public Integer getCampaignInfoId() {
        return campaignInfoId;
    }

    public void setCampaignInfoId(Integer campaignInfoId) {
        this.campaignInfoId = campaignInfoId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString(){
        return campaignInfoId + " - " + date;
    }

    @Override  // ** don't forget this annotation
    public int hashCode() { // *** note capitalization of the "C"
        return (campaignInfoId + "-" + date).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExhaustedCampaign other = (ExhaustedCampaign) obj;
        if ((campaignInfoId != other.getCampaignInfoId()) && !date.equals(other.getDate()))
            return false;
        return true;
    }
}
