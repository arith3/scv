package com.example.namori.serverscv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase dbdb = FirebaseDatabase.getInstance();
    DatabaseReference drdr = dbdb.getReference("appNum");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = findViewById(R.id.button1);
        Button b2 = findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drdr.setValue("null");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drdr.setValue("end");
            }
        });

        drdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(MainActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                String line = dataSnapshot.getValue().toString();
                if(line.equals("end"))
                    appKiller();
                else if(line.equals("null")) {}
                else
                    appStarter(line);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void appStarter(String app) {
        drdr.setValue("null");
        String AppPack = "";
        if(app.equals("Angry Birds"))
            AppPack = "com.rovio.angrybirds";//앵그리버드
        else if(app.equals("Instagram"))
            AppPack = "com.instagram.android";//인스타
        else if(app.equals("소녀전선"))
            AppPack = "kr.txwy.and.snqx";//소녀전선
        else if(app.equals("당연시"))
            AppPack = "com.koikatsu.android.dokidoki2.kr";//당연시
        else if(app.equals("Ajou Portal"))
            AppPack = "univ.ajou";//아포털
        else if(app.equals("Mail.Ru"))
            AppPack = "ru.mail.mailapp";//메이루
        else if(app.equals("지하철종결자"))
            AppPack = "teamDoppelGanger.SmarterSubway";//지하철노선도
        else {
            Toast.makeText(this, "Input is out of range", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent isInstall = getPackageManager().getLaunchIntentForPackage(AppPack);
        if(isInstall != null) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(AppPack);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "No App!!", Toast.LENGTH_SHORT).show();
    }

    private void appKiller() {
        drdr.setValue("null");
        Intent arisu = new Intent(getApplicationContext(), MainActivity.class);
        arisu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(arisu);
    }
}
