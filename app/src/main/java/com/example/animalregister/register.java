package com.example.animalregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class register extends AppCompatActivity {

    Button btnRegister,btnCat,btnDog,btnReset;
    ListView registerListView;
    RegisterAdapter registerAdapter;
    private OkHttpClient okHttpClient;
    private ArrayList<AdaopData_register> registerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerList = new ArrayList<>();
        findViews();

        okHttpClient = new OkHttpClient();

        Thread th = new Thread(okhttpRun);
        th.start();


    }

    private void findViews(){
        btnRegister = findViewById(R.id.btnRegister);
        btnCat = findViewById(R.id.btnCat);
        btnDog = findViewById(R.id.btnDog);
        registerListView = findViewById(R.id.registerList);
        btnReset = findViewById(R.id.btnReset);
        btnRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(register.this,registerData.class);
                startActivity(registerIntent);
            }
        });
    }

    Runnable okhttpRun = new Runnable() {
        @Override
        public void run() {
            getOkHttpConnect("http://lcyweb.000webhostapp.com/search_db.php");
        }
    };

    private void getOkHttpConnect(String s) {
        //建立連線
        Request request = new Request.Builder()
                .url(s)
                .build();
        //客戶端呼叫告知需求
        Call call = okHttpClient.newCall(request);
        //接收回應
        call.enqueue(new Callback() {

            //連線錯誤時的事件
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("OKHTTP", "連線失敗" + e.getMessage());
            }
            //有回應時的事件
            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                System.out.println(json);

                parseJSON(json);
                registerAdapter = new RegisterAdapter(register.this, registerList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        registerListView.setAdapter(registerAdapter);
                    }
                });
            }
        });

    }

    private void parseJSON(String s){

        try {
            //動物領養JSON的為外層是JSON陣列
            JSONArray arr = new JSONArray(s);
            //使用for迴圈，將解出陣列中的每一個JSON物件，
            //並以key取得物件中每一個value，
            //將每個value給該有的參考名稱
            //生成AdopData物件，放入value的參考名稱
            //迴圈會在每次生成AdopData物件放入集合adopList
            for (int i = 0; i<arr.length(); i++) {


                JSONObject obj = arr.getJSONObject(i);
                String name = obj.getString("register_name");
                String kind = obj.getString("kind");
                String update = obj.getString("register_time");
                //String animailId = obj.getString("animal_id");
                String color = obj.getString("color");
                String age = obj.getString("age");
                String sex = obj.getString("sex");
                String find_place = obj.getString("find_place");
                String shelter = obj.getString("shelter_place");
                String contact_name = obj.getString("contact_name");
                String tel = obj.getString("contact_tel");
                String remark = obj.getString("remark");
                String image_url = obj.getString("image_url");

                AdaopData_register data = new AdaopData_register();

                data.setName(name);
                data.setKind(kind);
                data.setUpdate(update);
                data.setColor(color);
                data.setAge(age);
                data.setSex(sex);
                data.setFind(find_place);
                data.setShelter(shelter);
                data.setContact(contact_name);
                data.setTel(tel);
                data.setRemark(remark);
                data.setUrl(image_url);

                registerList.add(data);

            }

        }catch (JSONException e){
            e.printStackTrace();
        }


    }

}
