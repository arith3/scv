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
                drdr.setValue("1");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drdr.setValue("2");
            }
        });

        drdr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
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
        if(app.equals("0"))
            AppPack = "com.rovio.angrybirds";
        else if(app.equals("1"))
            AppPack = "com.instagram.android";
        else if(app.equals("2"))
            AppPack = "com.kakao.talk";
        Intent intent = getPackageManager().getLaunchIntentForPackage(AppPack);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void appKiller() {
        drdr.setValue("null");
        Intent arisu = new Intent(getApplicationContext(), MainActivity.class);
        arisu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(arisu);
    }
}
