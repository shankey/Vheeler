package adcar.com.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import adcar.com.factory.Factory;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.handler.AreaHandler;
import adcar.com.handler.CoordinatesHandler;
import adcar.com.model.CoordinatesEntity;
import adcar.com.utility.Utility;

public class ScheduleReciever extends BroadcastReceiver {

    public static int timeKeeper = 0;
    public static Gson gson = new GsonBuilder().create();
    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);

    public ScheduleReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Chalu" ,Toast.LENGTH_LONG);

        //run every 15 minutes
        if(timeKeeper%15==0){
            if(Utility.isNetworkAvailable(context)){

                final List<CoordinatesEntity> li = coordinateDAO.getCoordinates();

                if(li.size() > 0){
                    new CoordinatesHandler().sendCoordinatesToServer(li);
                }
            }
        }

        //run every 30 minutes
        if(timeKeeper%30==0){

        }

        //run every 45 minutes
        if(timeKeeper%45==0){

        }

        //run every 60 minutes
        if(timeKeeper%60==0){
            new AreaHandler().SyncAreas();
        }



        if(timeKeeper == (24*60)){
            timeKeeper=0;
        }
        timeKeeper = timeKeeper + 15;
    }

    public CoordinateDAO getCoordinateDAO(Context context){
        if(coordinateDAO == null){
            coordinateDAO = new CoordinateDAO(context);
        }

        return coordinateDAO;
    }
}
