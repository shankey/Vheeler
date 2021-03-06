package adcar.com.algorithms;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import adcar.com.adcar.MainActivity;
import adcar.com.cache.Cache;
import adcar.com.model.servertalkers.Area;
import adcar.com.model.servertalkers.Coordinate;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 08/02/16.
 */
public class CoordinateAlgorithms {



    public static Boolean checkIfInside(LatLng point, List<LatLng> area){
        return PolyUtil.containsLocation(point, area, false);
    }

    public static Integer getInsideAreaId(Coordinate currentCoordinate){
        List<Area> areas = Cache.getCache().getAreas().getAreas();
        if(areas == null || areas.isEmpty()){
            Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.TOAST, "Areas yet to be synced");
        }

        Log.i("AREA", areas.toString());
        LatLng currentLatLng = new LatLng(currentCoordinate.getLatitude(),currentCoordinate.getLongitude());

        for(Area area: areas){
            List<LatLng> polygon = new ArrayList<>();

            for(Coordinate co : area.getCoordinates()){
                LatLng ltlng = new LatLng(co.getLatitude(), co.getLongitude());
                polygon.add(ltlng);
            }

            if(checkIfInside(currentLatLng, polygon)){
                return area.getAreaId();
            }
        }

        return Strings.DEFAULT_AREA_ID;
    }

    public static Integer getInsideAreaId(Location location){
        Coordinate co = new Coordinate();
        co.setLatitude(location.getLatitude());
        co.setLongitude(location.getLongitude());

        return getInsideAreaId(co);
    }
}
