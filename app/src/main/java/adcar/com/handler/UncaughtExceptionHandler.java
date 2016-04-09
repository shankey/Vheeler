package adcar.com.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.crashlytics.android.Crashlytics;


public class UncaughtExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final Class<?> myActivityClass;

    public UncaughtExceptionHandler(Context context, Class<?> c) {
        myContext = context;
        myActivityClass = c;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Crashlytics.logException(exception);
        try {
            Thread.sleep(5000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(myContext, myActivityClass);
        myContext.startActivity(intent);
        //for restarting the Activity
        Process.killProcess(Process.myPid());

        System.exit(0);
    }
}