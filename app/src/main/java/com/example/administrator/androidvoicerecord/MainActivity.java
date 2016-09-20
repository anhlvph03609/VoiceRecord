package com.example.administrator.androidvoicerecord;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button btnRecord,btnList;
    TextView tvTime;

    private MediaRecorder myAudioRecorder;
    String outputFile;
    String filePath;
     Chronometer myChronometer;
    private final String FILEPATH = "sdcard/AudioRecord";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File folder = new File(Environment.getExternalStorageDirectory() + "/AudioRecord");
        if (!folder.exists()) {
            folder.mkdir();
        }

        myChronometer = (Chronometer)findViewById(R.id.recordTime);
        btnRecord = (Button) findViewById(R.id.btnStart);
        tvTime = (TextView) findViewById(R.id.recordTime);
        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ListRecordActivity.class);
                startActivity(intent);
            }
        });


        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AudioRecord/";;


        btnRecord.setOnClickListener(new View.OnClickListener() {
            boolean isStartRecoding = true;
            @Override
            public void onClick(View view) {
                if(isStartRecoding){
                try {
                    DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ");
                    String date = df.format(Calendar.getInstance().getTime());
                    btnRecord.setText("Stop");
                    myChronometer.setBase(SystemClock.elapsedRealtime());
                    myChronometer.start();
                    myAudioRecorder=new MediaRecorder();
                    myChronometer.setBase(SystemClock.elapsedRealtime());
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile + date+ ".3gp");
                    filePath = outputFile + date+ ".3gp";
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                }

                catch (IllegalStateException e) {

                    e.printStackTrace();
                }

                catch (IOException e) {

                    e.printStackTrace();
                }
                    isStartRecoding = !isStartRecoding;

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

                }else{
                    showInputDialog();
                    btnRecord.setText("Record");
                    isStartRecoding = !isStartRecoding;

                }

            }

        });
    }
    protected void showInputDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);


        alertDialogBuilder.setTitle("Do you want to save this record?");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            //Toast.makeText(getApplicationContext(),outputFile,Toast.LENGTH_LONG).show();
                       try {
                           myChronometer.setBase(SystemClock.elapsedRealtime());
                           myChronometer.stop();
                           myAudioRecorder.stop();
                           myAudioRecorder.release();
                           myAudioRecorder = null;
                           Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
                       }catch(IllegalStateException e){

                           throw e;
                       }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myChronometer.setBase(SystemClock.elapsedRealtime());
                                myChronometer.stop();
                               myAudioRecorder.stop();
                                File file = new File(filePath);
                                boolean deleted = file.delete();
                                myAudioRecorder=null;
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
