package adcar.com.model;

import java.util.List;

/**
 * Created by aditya on 06/02/16.
 */
public class Areas {

    List<Area> areas;

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    public String toString(){
        return "areas = " + areas.toString();
    }
}
