package com.example.jy.scv;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class teenList extends AppCompatActivity {

    FirebaseDatabase fbfb = FirebaseDatabase.getInstance();
    DatabaseReference rfrf = fbfb.getReference("appNum");

    private int goPlayStore = 0;
    private int temp;
    private String positionT;
    private ListView list_View;
    private ListViewAdapter adapter;
    private int[] img = {R.drawable.angrybirds, R.drawable.instagram, R.drawable.o, R.drawable.jou, R.drawable.mail,R.drawable.trail};
    private String[] Title = {"Angry Birds", "instagram", "당연시", "Ajou Portal", "Mail.Ru","지하철종결자"};
    private String[] Context = {"Angry Birds의 독특한 파워를 사용하여 욕심쟁이 돼지의 방어를 무너트리세요!",
            "facebook 짝퉁" ,"당장 연애 시작해!", "아주인이라면 필수 앱", "mail", "지하철 노선도를 파악하자."};

    private String AppPack = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        adapter = new ListViewAdapter();
        list_View = (ListView) findViewById(R.id.listview);
        list_View.setAdapter(adapter);

        for(int i=0; i<img.length; i++){
            adapter.addData(ContextCompat.getDrawable(this, img[i]), Title[i], Context[i]);
        }


        list_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Title[(int)(id)]=="Angry Birds")
                {
                    rfrf.setValue("Angry Birds");
                }
                else if(Title[(int)(id)]=="instagram")
                {
                    rfrf.setValue("Instagram");
                }
                else if(Title[(int)(id)]=="소녀전선")
                {
                    rfrf.setValue("소녀전선");
                }
                else if(Title[(int)(id)]=="당연시")
                {
                    rfrf.setValue("당연시");
                }
                else if(Title[(int)(id)]=="Ajou Portal")
                {
                    rfrf.setValue("Ajou Portal");
                }
                else if(Title[(int)(id)]=="Mail.Ru")
                {
                    rfrf.setValue("Mail.Ru");
                }
                else if(Title[(int)(id)]=="지하철종결자")
                {
                    rfrf.setValue("지하철종결자");
                }
                int reqCode = 1000+position;
                Log.d("이름은  : ", Title[(int)(id)]+ "");
                Log.d("position : ", positionT + "");
                //new JSONTask().execute("http://52.79.149.204:8000/list");//AsyncTask 시작시킴
                Intent intent = new Intent(getApplicationContext(), CastActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("position",position);
                startActivityForResult(intent, reqCode);
            }
        });

    }

    private PopupWindow pw;
    private void showPopup() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) teenList.this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_1));
            pw = new PopupWindow(layout, 500, 300, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button Yes = (Button) layout.findViewById(R.id.yes_popup);
            Yes.setOnClickListener(yes_button);
            Button Close = (Button) layout.findViewById(R.id.no_popup);
            Close.setOnClickListener(cancel_button);
            TextView txt = (TextView) layout.findViewById(R.id.txtView);
            String str = "체험은 잘 하셨나요?"+"\n"+"방금 체험하신 앱을 다운로드 받으러 가볼까요?";
            txt.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            goPlayStore = 0;
            pw.dismiss();
        }
    };
    private View.OnClickListener yes_button = new View.OnClickListener() {
        public void onClick(View v) {
            goPlayStore = 1;
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AppPack)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + AppPack)));
            }
            pw.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        showPopup();
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            AppPack = "com.rovio.angrybirds";
        }else if(requestCode == 1001){
            AppPack = "com.instagram.android";
        }else if(requestCode == 1002){
            AppPack = "kr.txwy.and.snqx";
        }else if(requestCode == 1003){
            AppPack = "com.koikatsu.android.dokidoki2.kr";
        }else if(requestCode == 1004){
            AppPack = "univ.ajou";
        }else if(requestCode == 1005){
            AppPack = "ru.mail.mailapp";
        }else if(requestCode == 1006){
            AppPack = "teamDoppelGanger.SmarterSubway";
        }

    }



    /*public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObjectList = new JSONObject();
                jsonObjectList.accumulate("user_id", ""+positionT);
                Log.d("log : ", positionT + "");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObjectList.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }


                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.d("RESULT", "list 가는중");

            if(result.equals("0")) {


                Toast.makeText(ListActivity.this, "0000000", Toast.LENGTH_SHORT).show();



            }
            else if(result.equals("1"))
            {
                Toast.makeText(ListActivity.this, "12121212121111", Toast.LENGTH_SHORT).show();



            }
            else if(result.equals("2"))
            {
                Toast.makeText(ListActivity.this, "222222222", Toast.LENGTH_SHORT).show();



            }

        }
    }*/
}
