package adcar.com.adcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adcar.com.database.dao.CoordinateDAO;
import adcar.com.model.CoordinatesEntity;


public class TableActivity extends AppCompatActivity {

    CoordinateDAO coordinateDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ListView liView = (ListView)findViewById(R.id.list);
        coordinateDAO = new CoordinateDAO(this);
        ArrayList<CoordinatesEntity> cos = coordinateDAO.getCoordinates();
        List li = new ArrayList<>();
        for(CoordinatesEntity co: cos){
            li.add(co.getTimestamp() + " - " + co.getCoordinate().getLatitude() + " - " + co.getCoordinate().getLongitude());
        }


        // CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_table, R.id.history, li);

        // SET THIS ADAPTER AS YOUR LISTACTIVITY'S ADAPTER
        liView.setAdapter(adapter);
    }
}
