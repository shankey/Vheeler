package adcar.com.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import adcar.com.factory.Factory;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.handler.AdHandler;
import adcar.com.handler.CampaignHandler;
import adcar.com.handler.CampaignRunHandler;
import adcar.com.handler.CoordinatesHandler;
import adcar.com.handler.ExhaustedCampaignRunHandler;
import adcar.com.handler.VersionHandler;
import adcar.com.model.servertalkers.CoordinateBatchEntity;
import adcar.com.model.CoordinatesEntity;
import adcar.com.utility.Utility;

public class ScheduleReceiver extends BroadcastReceiver {

    public static int timeKeeper = 0;
    public static Gson gson = new GsonBuilder().create();
    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);

    public ScheduleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Chalu" ,Toast.LENGTH_LONG).show();
        Log.i("POLLER", "called now with timer = " + timeKeeper);

        //run every 15 minutes
        if(timeKeeper%15==0){
            if(Utility.isNetworkAvailable(context)){

                final List<CoordinatesEntity> li = coordinateDAO.getCoordinates();
                CoordinateBatchEntity batchEntity = new CoordinateBatchEntity();

                if(li.size() > 0){
                    batchEntity.setDeviceId(Utility.getDeviceId());
                    batchEntity.setLi(li);
                    new CoordinatesHandler().sendCoordinatesToServer(batchEntity);
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
            new ExhaustedCampaignRunHandler().SyncExhaustedCampaignRuns();
            new AdHandler().SyncAds();
            new VersionHandler().SyncVersions();
            CampaignRunHandler.StartSyncCampaignRuns();
        }



        if(timeKeeper == (24*60)){
            timeKeeper=0;
        }
        timeKeeper = timeKeeper + 15;
    }


}
