package com.example.jy.scv;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.jy.scv.MainActivity;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText ID;
    private EditText PW;
    private EditText PWConfirm;
    private EditText Age;
    private Button btnDone;
    private Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ID = (EditText) findViewById(R.id.ID);
        PW = (EditText) findViewById(R.id.PW);
        PWConfirm = (EditText) findViewById(R.id.PWConfirm);
        Age = (EditText) findViewById(R.id.Age);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        PWConfirm.addTextChangedListener(new TextWatcher() {    // 두 비밀번호 일치 검사
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = PW.getText().toString();
                String confirm = PWConfirm.getText().toString();
                if(password.equals(confirm)){
                    PW.setBackgroundColor(Color.GREEN);
                    PWConfirm.setBackgroundColor(Color.GREEN);
                }else{
                    PW.setBackgroundColor(Color.RED);
                    PWConfirm.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(ID.getText().toString().length() == 0){  // ID 입력 확인
                    Toast.makeText(RegisterActivity.this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
                    ID.requestFocus();
                    return;
                }
                if(PW.getText().toString().length() == 0){  // PW 입력 확인
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    PW.requestFocus();
                    return;
                }
                if(PWConfirm.getText().toString().length() == 0){  // PW 확인 입력 확인
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요", Toast.LENGTH_SHORT).show();
                    PWConfirm.requestFocus();
                    return;
                }
                if(!PW.getText().toString().equals(PWConfirm.getText().toString())){    // PW 일치 확인
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    PW.setText("");
                    PWConfirm.setText("");
                    PW.requestFocus();
                    return;
                }
                new JSONTask().execute("http://13.125.246.47:8000/register");//AsyncTask 시작시킴


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", ""+ID.getText().toString());
                jsonObject.accumulate("name", ""+PW.getText().toString());
                jsonObject.accumulate("age", Integer.valueOf(Age.getText().toString()));

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
                    writer.write(jsonObject.toString());
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


            Log.d("RESULT", "OK안들어옴");
            Log.d("RESULT", result+"");
            if(result.equals("success")) {

                Intent res = new Intent();
                res.putExtra("ID", ID.getText().toString());
                setResult(RESULT_OK, res);
                finish();

            }
            else if(result.equals("failed"))
            {
                Log.d("RESULT", "아이디가 이미 있습니다");
                Toast.makeText(RegisterActivity.this, "아이디가 이미 있습니다", Toast.LENGTH_SHORT).show();
                PW.setText("");
                PWConfirm.setText("");
                ID.setText("");

            }

        }
    }
}