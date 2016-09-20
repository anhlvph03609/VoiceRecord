package com.example.administrator.androidvoicerecord;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListRecordActivity extends AppCompatActivity {
    ListView lvRecord;
    final String FILEPATH = "/sdcard/AudioRecord/";
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);
        lvRecord = (ListView)findViewById(R.id.lvRecord);
        final ArrayList<String> FilesInFolder = GetFiles(FILEPATH);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                FilesInFolder);

        lvRecord.setAdapter(adapter);

        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaPlayer record = new MediaPlayer();
                try {
                    record.setDataSource(FILEPATH+FilesInFolder.get(i).toString().trim());
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    record.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                record.start();
            }
        });

        lvRecord.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListRecordActivity.this);


                alertDialogBuilder.setTitle("Do you want to delete this record?");
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getApplicationContext(),outputFile,Toast.LENGTH_LONG).show();

                                    File file = new File(FILEPATH+FilesInFolder.get(i).toString().trim());
                                    boolean deleted = file.delete();
                                if(deleted) {
                                    FilesInFolder.remove(i);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "Record deleted successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });


                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });
    }

    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
                MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }

    protected void showInputDialog() {

    }
}
