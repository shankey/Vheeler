package adcar.com.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import adcar.com.adcar.MainActivity;
import adcar.com.adcar.ProcessorService;
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
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

public class ScheduleReceiver extends BroadcastReceiver {

    public static int timeKeeper = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.TOAST, "ALARM CHALU");
        try {
            Thread.sleep(2000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("POLLER", "called now with timer = " + timeKeeper);


        timeKeeper = timeKeeper + 15;

        ProcessorService.startProcessorService(context, timeKeeper);

        if(timeKeeper == (24*60)){
            timeKeeper=0;
        }
        Log.i("POLLER", "returning from broadcast receiver = " + timeKeeper);
    }
}
