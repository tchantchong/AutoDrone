package testing.path_algorithm;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kenzo on 18/11/2016.
 */

public class Comando {
    public static void main(String[] args){
        double LonAtual;
        double LatAtual;
        double AngAtual;
        double[] LonProx = new double[10];
        double[] LatProx = new double[10];
        double AngProx;
        double AngComm;
        double FrenteComm;
        double x;
        double y;
        double LatToM;
        double PI = 3.14159265358979;
        LatToM = 110800; //1 Grau de Latitude em metros
        AngAtual = 90;


        LatAtual = -23.55559091;
        LonAtual = -46.73126332;

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

        //double x = Math.atan(5);
        for (int i=0;i<=4;i++)
        {
            x = LatToM * ( Math.cos(LatAtual*PI/180) * ( LonProx[i] - LonAtual ) );
            y = (LatProx[i]-LatAtual)*LatToM;


            if (LatProx[i]>LatAtual && LonProx[i] > LonAtual)
            {
                AngProx = Math.atan(x/y);
                AngProx = (AngProx*180)/PI;

            }
            else if (LatProx[i]<LatAtual && LonProx[i] > LonAtual)
            {
                AngProx = Math.atan(-x/y);
                AngProx = 180 - (AngProx*180)/PI;
            }
            else if (LatProx[i]<LatAtual && LonProx[i] < LonAtual)
            {
                AngProx = Math.atan(x/y);
                AngProx = 180 + (AngProx*180)/PI;
            }
            else if (LatProx[i]>LatAtual && LonProx[i] < LonAtual)
            {
                AngProx = Math.atan(-x/y);
                AngProx = 360 - (AngProx*180)/PI;
            }
            else
            {
                AngProx = 0;
            }

            if (AngAtual > AngProx)
            {
                while (AngAtual-AngProx > 5)
                {
                    if (AngAtual-AngProx > 180)
                    {
                        //Girar Horario
                        System.out.println("Girar HORA " + (360 - AngAtual + AngProx));
                    }
                    else
                    {
                        //Girar ANTI
                        System.out.println("Girar ANTI " + (AngAtual-AngProx));
                    }


                    //Atualiza Bussola
                    AngAtual = AngProx;
                    //System.out.println("Angulo " + AngAtual);
                }
            }

            else if (AngAtual < AngProx)
            {
                while (AngProx-AngAtual > 5)
                {
                    if (AngProx - AngAtual < 180)
                    {
                        //Girar HORA
                        System.out.println("Girar HORA " + (AngProx-AngAtual));
                    }
                    else
                    {
                        //Girar ANTI
                        System.out.println("Girar ANTI " + (360 - AngProx + AngAtual));

                    }
                    //Atualiza Bussola
                    AngAtual = AngProx;
                    //System.out.println("Angulo "+AngAtual);
                }

            }
            FrenteComm = Math.sqrt(   Math.pow(x, 2) + Math.pow(y, 2) ); //Comando de frente
            System.out.println("Andou " + FrenteComm + " metros");
            LatAtual = LatProx[i];
            LonAtual = LonProx[i];
        }



    }

}
