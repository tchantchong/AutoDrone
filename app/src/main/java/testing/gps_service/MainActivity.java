package testing.gps_service;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import compass.CompassAssistant;

public class MainActivity extends AppCompatActivity implements CompassAssistant.CompassAssistantListener {

    private Button btn_start, btn_stop;
    private TextView textView;
    private BroadcastReceiver broadcastReceiver;
    private double latitude;
    private double longitude;
    private CompassAssistant compassAssistant;
    private double currentDegree;
    private double referenceLatitude = 90.00f;
    private double referenceLongitude = 0.00f;

    double AngProx;
    double FrenteComm;
    double x;
    double y;
    double LatToM;
    double PI = 3.14159265358979;
    double[] LatProx = new double[10];
    double[] LonProx = new double[10];
    int flag = 0;





    @Override
    protected void onResume() {
        super.onResume();
        compassAssistant = new CompassAssistant(MainActivity.this);
        compassAssistant.addListener(MainActivity.this);
        compassAssistant.start();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = Double.parseDouble(intent.getExtras().get("latitude").toString());
                    longitude = Double.parseDouble(intent.getExtras().get("longitude").toString());
                    currentDegree = compassAssistant.getBearingBetweenLocations(latitude, longitude, referenceLatitude, referenceLongitude);
                    textView.setText("Latitude: " + String.valueOf(latitude) + "\nLongitude: " + String.valueOf(longitude) + "\nDegrees: " + String.valueOf(currentDegree));

                    LatToM = 110800;
                    LatProx[0] = -23.55531922;
                    LonProx[0] = -46.73111714;

                    LatProx[1] = -23.55538315;
                    LonProx[1] = -46.73096962;

                    LatProx[2] = -23.55565115;
                    LonProx[2] = -46.7311319;

                    LatProx[3] = -23.55545076;
                    LonProx[3] = -46.73083551;

                    LatProx[4] = -23.55579006;
                    LonProx[4] = -46.73084356;

                    for (int i=0;i<=4;i++) {


                        x = LatToM * (Math.cos(latitude * PI / 180) * (LonProx[i] - longitude));
                        y = (LatProx[i] - latitude) * LatToM;
                        //System.out.println(x);

                        if (LatProx[i]>latitude && LonProx[i] > longitude)
                        {
                            AngProx = Math.atan(x/y);
                            AngProx = (AngProx*180)/PI;

                        }
                        else if (LatProx[i]<latitude && LonProx[i] > longitude)
                        {
                            AngProx = Math.atan(-x/y);
                            AngProx = 180 - (AngProx*180)/PI;
                        }
                        else if (LatProx[i]<latitude && LonProx[i] < longitude)
                        {
                            AngProx = Math.atan(x/y);
                            AngProx = 180 + (AngProx*180)/PI;
                        }
                        else if (LatProx[i]>latitude && LonProx[i] < longitude)
                        {
                            AngProx = Math.atan(-x/y);
                            AngProx = 360 - (AngProx*180)/PI;
                        }
                        else
                        {
                            AngProx = 0;
                        }

                        if (currentDegree > AngProx)
                        {
                            while (Math.abs(currentDegree-AngProx) > 5)
                            {
                                if (currentDegree-AngProx > 180)
                                {
                                    //Girar Horario
                                    System.out.println("Girar HORA " + (360 - currentDegree + AngProx));
                                }
                                else
                                {
                                    //Girar ANTI
                                    System.out.println("Girar ANTI " + (currentDegree-AngProx));
                                }
                                currentDegree = AngProx;
                            }
                        }

                        else if (currentDegree < AngProx)
                        {
                            while (Math.abs(currentDegree-AngProx) > 5)
                            {
                                if (AngProx - currentDegree < 180)
                                {
                                    //Girar HORA
                                    System.out.println("Girar HORA " + (AngProx-currentDegree));
                                }
                                else
                                {
                                    //Girar ANTI
                                    System.out.println("Girar ANTI " + (360 - AngProx + currentDegree));

                                }
                                //Atualiza Bussola
                                currentDegree = AngProx;
                                //System.out.println("Angulo "+currentDegree);
                            }

                        }
                        FrenteComm = Math.sqrt(   Math.pow(x, 2) + Math.pow(y, 2) ); //Comando de frente
                            System.out.println("Andou " + FrenteComm + " metros");
                            //Thread.sleep(5000);
                            //flag = 1;


                        //Checagem
                       // x = LatToM * (Math.cos(latitude * PI / 180) * (LonProx[i] - longitude));
                       // y = (LatProx[i] - latitude) * LatToM;
                      //  FrenteComm = Math.sqrt(   Math.pow(x, 2) + Math.pow(y, 2) );
                        //if (FrenteComm > 10)
                        //{
                           // i=i-1;
                            //System.out.println("FUCK\n");

                        //}
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.compassAssistant.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.button);
        btn_stop = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);

        if(!runtime_permissions())
            enable_buttons();

    }

    private void enable_buttons() {

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),GPS_Service.class);
                stopService(i);

            }
        });

    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }

    @Override
    public void onNewDegreesToNorth(float degrees) {

    }

    @Override
    public void onNewSmoothedDegreesToNorth(float degrees) {

    }

    @Override
    public void onCompassStopped() {

    }

    @Override
    public void onCompassStarted() {

    }
}
