package adcar.com.adcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by aditya on 07/03/16.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startVheeler = new Intent(context, MainActivity.class);
        startVheeler.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startVheeler);
        Toast.makeText(context, "BOOT TRIED", Toast.LENGTH_LONG).show();
        Log.i("HELLO", "on boot we tried this");
    }
}
