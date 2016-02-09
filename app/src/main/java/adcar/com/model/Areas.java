package adcar.com.model;

import java.util.List;

/**
 * Created by aditya on 06/02/16.
 */
public class Areas {

    String version;
    List<Area> areas;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    public String toString(){
        return "version = " + version + "areas = " + areas.toString();
    }
}
