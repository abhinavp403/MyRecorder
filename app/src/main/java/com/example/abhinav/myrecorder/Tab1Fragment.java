package com.example.abhinav.myrecorder;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Date;

public class Tab1Fragment extends Fragment {
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private TextView timer;
    Button start, pause, reset;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        super.onCreate(savedInstanceState);

        play = view.findViewById(R.id.play);
        stop = view.findViewById(R.id.stop);
        record = view.findViewById(R.id.record);
        timer = view.findViewById(R.id.tvTimer);
        handler = new Handler() ;
        myAudioRecorder = new MediaRecorder();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, BuildDev.RECORD_AUDIO);
            Log.d("Fail", "fail");
        }
        else {
            Log.d("Pass", "Pass");
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.stop();
                } catch(RuntimeException e) {
                    e.printStackTrace();
                }
                myAudioRecorder.release();
                myAudioRecorder = null;

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                timer.setText("00:00:00");

                Date createdTime = new Date();
                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + createdTime + "_rec.3gp";
                Tab2Fragment obj = new Tab2Fragment ();
                Bundle args = new Bundle();
                args.putString("name", outputFile);
                obj.setArguments(args);
                Toast.makeText(getActivity(), "Recording saved", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getActivity(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);
            }
        });

        return view;
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timer.setText("" + Minutes + ":" + String.format("%02d", Seconds) + ":" +  String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }
    };
}