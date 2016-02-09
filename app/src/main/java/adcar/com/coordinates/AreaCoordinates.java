package adcar.com.coordinates;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aditya on 02/02/16.
 */
public class AreaCoordinates {

    private static AreaCoordinates areaCoordinates = new AreaCoordinates();

    Map<String ,List<LatLng>> areas = new HashMap<String, List<LatLng>>();

    public static AreaCoordinates getAreaCoordinates(){
        return areaCoordinates;
    }


}
