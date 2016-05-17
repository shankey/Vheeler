package adcar.com.model.servertalkers;

import java.util.List;

import adcar.com.model.CoordinatesEntity;

/**
 * Created by aditya on 10/03/16.
 */
public class CoordinateBatchEntity {

    private List<CoordinatesEntity> li;
    private String deviceId;

    public List<CoordinatesEntity> getLi() {
        return li;
    }

    public void setLi(List<CoordinatesEntity> li) {
        this.li = li;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
