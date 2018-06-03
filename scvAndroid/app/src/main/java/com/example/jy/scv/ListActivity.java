package com.example.jy.scv;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

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


public class ListActivity extends AppCompatActivity {


    private String positionT;
    private ListView list_View;
    private ListViewAdapter adapter;
    private int[] img = {R.drawable.angrybirds, R.drawable.app2, R.drawable.app3};
    private String[] Title = {"앵그리버드", "용진쨩", "하..."};
    private String[] Context = {"Angry Birds의 독특한 파워를 사용하여 욕심쟁이 돼지의 방어를 무너트리세요!",
            "용진아..", "Context3"};

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
                if(position==0)
                {
                    positionT="0";
                }
                else if(position==1)
                {
                    positionT="1";
                }
                else if(position==2)
                {
                    positionT="2";
                }

                Log.d("position : ", positionT + "");
                new JSONTask().execute("http://52.79.149.204:8000/list");//AsyncTask 시작시킴
                //Intent intent = new Intent(getApplicationContext(), CastActivity.class);
                //startActivity(intent);
            }
        });

    }



    public class JSONTask extends AsyncTask<String, String, String> {

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
    }
}
