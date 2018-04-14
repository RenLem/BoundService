package omy.boston.boundservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import omy.boston.boundservice.Services.BoundService;

public class MainActivity extends AppCompatActivity {

    private BoundService boundService;
    private boolean serviceBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BoundService.BoundServiceBinder boundServiceBinder = (BoundService.BoundServiceBinder)service;
            boundService = boundServiceBinder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //IZRAVNO DODANI!
        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnInfo = (Button) findViewById(R.id.btnAbout);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!serviceBound){
                    Intent i = new Intent(getApplicationContext(), BoundService.class);
                    startService(i);
                    bindService(i, serviceConnection, BIND_AUTO_CREATE);
                    serviceBound = true;
                }else {Toast.makeText(getApplicationContext(), "Servis je već pokrenut!", Toast.LENGTH_SHORT).show();}
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceBound){
                    unbindService(serviceConnection);
                    serviceBound = false;
                    Intent i = new Intent(getApplicationContext(), BoundService.class);
                    stopService(i);
                }else {Toast.makeText(getApplicationContext(), "Servis je već zausravljen!", Toast.LENGTH_SHORT).show();}
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceBound){
                    String counterVlue = boundService.getCounterValue();
                    Toast.makeText(getApplicationContext(), "Counter je na: " + counterVlue, Toast.LENGTH_SHORT
                    ).show();
                }else {Toast.makeText(getApplicationContext(), "Info za servis nije dostupan!", Toast.LENGTH_SHORT).show();}
            }
        });
    }
}
