package adcar.com.model;

/**
 * Created by aditya on 09/02/16.
 */
public class Ad {

    private Integer id;
    private Integer areaId;
    private String url;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Id = " + id + "AreaId = "+ areaId + "URL = " + areaId + "status = " + status;
    }
}
