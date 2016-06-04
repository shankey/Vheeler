package adcar.com.adcar;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.handler.AdHandler;
import adcar.com.handler.CampaignHandler;
import adcar.com.handler.CampaignRunHandler;
import adcar.com.handler.CoordinatesHandler;
import adcar.com.handler.ExhaustedCampaignRunHandler;
import adcar.com.handler.VersionHandler;
import adcar.com.model.CoordinatesEntity;
import adcar.com.model.servertalkers.CoordinateBatchEntity;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ProcessorService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static Gson gson = new GsonBuilder().create();
    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);

    public static ExhaustedCampaignRunHandler exhaustedCampaignRunHandler = new ExhaustedCampaignRunHandler();
    public static AdHandler adHandler = new AdHandler();
    public static VersionHandler versionHandler = new VersionHandler();
    public static CampaignHandler campaignHandler = new  CampaignHandler();

    public ProcessorService() {
        super("ProcessorService");
        Log.i("PROCESSORSERVICE", "constructor called");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void  startProcessorService(Context context, Integer timer) {
        Intent intent = new Intent(context, ProcessorService.class);
        intent.putExtra(Strings.TIMER, timer);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final Integer timeKeeper = intent.getIntExtra(Strings.TIMER, -1);
            Log.i("PROCESSORSERVICE", "timerKeeper is " + timeKeeper);
            if(timeKeeper%15==0){
                if(Utility.isNetworkAvailable(this)){

                    final List<CoordinatesEntity> li = coordinateDAO.getCoordinates();
                    CoordinateBatchEntity batchEntity = new CoordinateBatchEntity();

                    if(li!=null && li.size() > 0){
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
                exhaustedCampaignRunHandler.SyncExhaustedCampaignRuns();
                adHandler.SyncAds();
                versionHandler.SyncVersions();
                CampaignRunHandler.StartSyncCampaignRuns();
                campaignHandler.SyncExpiredCampaigns();
            }
        }
    }

}
