package omy.boston.boundservice.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BoundService extends Service {
    private BoundServiceBinder bServiceBinder = new BoundServiceBinder();
    private int counter = 0;
    private Thread thread = null;

    public class BoundServiceBinder extends Binder {
        public BoundService getService(){
            return BoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startCounter();
    }

    private void startCounter(){
        thread = new Thread(new Runnable() { // <-- Ovo čini posebnu dretvu!
            @Override
            public void run() {
                while (true){
                    counter++;
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        thread.start();
    }

    public String getCounterValue(){
        return counter+"";
    }

    @Override
    public void onDestroy() {
        if (thread != null) {
            thread.interrupt();
        }
        super.onDestroy();
    }
}
