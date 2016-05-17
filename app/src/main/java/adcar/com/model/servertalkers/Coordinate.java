package adcar.com.model.servertalkers;

/**
 * Created by aditya on 02/02/16.
 */
public class Coordinate {

    Double latitude;
    Double longitude;

    public Coordinate(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(){

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return "latitude = " + latitude + " longitude = " + longitude;
    }
}
