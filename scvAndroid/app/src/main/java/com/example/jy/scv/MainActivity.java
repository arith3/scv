package com.example.jy.scv;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity {

    private EditText ID;
    private EditText PW;
    private Button btnResgister;
    private Button btnLogin;
    private String user_id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ID = (EditText) findViewById(R.id.ID);
        PW = (EditText) findViewById(R.id.PW);
        btnResgister = (Button) findViewById(R.id.btnRegist);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnResgister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivityForResult(intent, 1000);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user_id = null;
                name = null;
                user_id = ID.getText().toString();
                name = "Yoon";


                new JSONTask().execute("http://13.125.246.47:8000/post");//AsyncTask 시작시킴
                Log.d("RESULT", user_id);
                Log.d("RESULT", "클릭됨");


            }
        });
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "" + ID.getText().toString());
                jsonObject.accumulate("name", "" + PW.getText().toString());

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
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
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }


                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
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


            Log.d("RESULT", "OK안들어옴");
            Log.d("RESULT", result + "");
            if (result.equals("adult")) {
                Log.d("RESULT", "OK들어옴");
                Intent intent2 = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent2);
            } else if (result.equals("teen")) {
                    Log.d("RESULT", "OK들어옴");
                    Intent intent3 = new Intent(getApplicationContext(), teenList.class);
                    startActivity(intent3);
            } else if (result.equals("pwnot")) {
                Log.d("RESULT", "패스워드를 확인하세요");
                Toast.makeText(MainActivity.this, "패스워드를 확인하세요", Toast.LENGTH_SHORT).show();

            } else if (result.equals("not")) {
                Log.d("RESULT", "아이디를 확인하세요");
                Toast.makeText(MainActivity.this, "아이디를 확인하세요", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("RESULT", requestCode + "");
        Log.d("RESULT", resultCode + "");
        Log.d("RESULT", data + "");

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, "회원가입을 완료했습니다", Toast.LENGTH_SHORT).show();
            ID.setText(data.getStringExtra("ID"));
        }
    }


}