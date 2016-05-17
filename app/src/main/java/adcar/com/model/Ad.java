package adcar.com.model;




/**
 * Created by aditya on 09/02/16.
 */
public class Ad implements Comparable<Ad> {

    private Integer id;
    private Integer adId;
    private String url;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString(){
        return id + " " + adId + " " + url;

    }

    @Override
    public int compareTo(Ad ad) {
        return ad.getAdId();
    }
}
