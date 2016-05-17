package adcar.com.model.servertalkers;

import java.util.List;

/**
 * Created by aditya on 02/02/16.
 */
public class Area {

    Integer areaId;
    List<Coordinate> coordinates;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString(){
        return "areaid = " + areaId + " cooredinates = " + coordinates;
    }
}
