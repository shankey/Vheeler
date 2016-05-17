package adcar.com.model;

import java.sql.Timestamp;

import adcar.com.model.servertalkers.Coordinate;

/**
 * Created by aditya on 25/01/16.
 */
public class CoordinatesEntity {

    Integer id;
    Coordinate coordinate;
    Timestamp timestamp;
    Integer adId;
    Integer areaId;
    String deviceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString(){
        return adId + " "+areaId + " "+deviceId + " " + coordinate.toString();
    }
}
