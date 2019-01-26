package com.example.uzzal.mediaplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView myListSong;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListSong = findViewById(R.id.listView_id);

        runtimePermission();
    }

    public void runtimePermission(){

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                display();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {


            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    token.continuePermissionRequest();
            }
        }).check();

    }

   public ArrayList<File> findSong (File file){

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File stringFile: files){

            if(stringFile.isDirectory() && !stringFile.isHidden()){
                arrayList.addAll(findSong(stringFile));
            }else{

                if(stringFile.getName().endsWith(".mp3") ||
                        stringFile.getName().endsWith(".wav")){

                  arrayList.add(stringFile);
                }
            }

       }

       return arrayList;
   }

   void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];

        for (int i = 0; i<mySongs.size(); i++){

            items[i] = mySongs.get(i).getName().toString().replace(".mp3"," ").replace(".wav"," ");

        }

       ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        myListSong.setAdapter(myAdapter);


        myListSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName = myListSong.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                .putExtra("songs",mySongs).putExtra("songName",songName)
                .putExtra("pos",i));
            }
        });


   }
}
