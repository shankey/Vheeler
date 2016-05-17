package adcar.com.adcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.model.Ad;
import adcar.com.model.servertalkers.Area;
import adcar.com.model.servertalkers.Coordinate;
import adcar.com.model.CoordinatesEntity;


public class TableActivity extends AppCompatActivity {

    CoordinateDAO coordinateDAO;
    AreaDAO areaDAO;
    AdDAO adDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ListView liView = (ListView)findViewById(R.id.list);
        List li = new ArrayList<>();

        Intent intent = getIntent();
        if(intent.getIntExtra("ACTION", -1) == 1){
            coordinateDAO = new CoordinateDAO(this);
            ArrayList<CoordinatesEntity> cos = coordinateDAO.getCoordinates();

            for(CoordinatesEntity co: cos){
                li.add(co.getAdId() + " - " + co.getAreaId() + " - " + co.getTimestamp() + " - " + co.getCoordinate().getLatitude() + " - " + co.getCoordinate().getLongitude());
            }
        }

        if(intent.getIntExtra("ACTION", -1) == 3){
            areaDAO = new AreaDAO(this);
            List<Area> areas = areaDAO.getAreas();

            for(Area area: areas){
                for(Coordinate co : area.getCoordinates()){
                    li.add(area.getAreaId() + " - " + co.getLatitude() + " - " + co.getLongitude());
                }
            }
        }

        if(intent.getIntExtra("ACTION", -1) == 2){
            adDAO = new AdDAO(this);
            List<Ad> ads = adDAO.getAds(null, null);

            for(Ad ad: ads){
                li.add(ad.getAdId() + " - " + ad.getUrl());
            }
        }


        // CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_table, R.id.history, li);

        // SET THIS ADAPTER AS YOUR LISTACTIVITY'S ADAPTER
        liView.setAdapter(adapter);
    }
}
