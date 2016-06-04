package adcar.com.adcar;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import adcar.com.handler.CoordinatesHandler;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GPSProcessorService extends IntentService {

    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";

    public static CoordinatesHandler coordinatesHandler = new CoordinatesHandler();

    public GPSProcessorService() {
        super("GPSProcessorService");

    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startGPSLocationDigest(Context context, Double lat, Double lng) {

        Intent intent = new Intent(context, GPSProcessorService.class);
        intent.putExtra(LATITUDE, lat);
        intent.putExtra(LONGITUDE, lng);
        Log.i("GPSPROCESSOR", "inside start location digest with location = " + lat + " - " + lng);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Double lat = intent.getDoubleExtra(LATITUDE, -1);
            Double lng = intent.getDoubleExtra(LONGITUDE, -1);

            Location location = new Location("MANUAL");
            location.setLatitude(lat);
            location.setLongitude(lng);
            Log.i("GPSPROCESSOR", "start making use of new location in coordinateHandler");
            coordinatesHandler.makeUseOfNewLocation(location);
        }
    }
}
