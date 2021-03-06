package com.lcypj.animalregister;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class register extends AppCompatActivity {

    private Button btnRegister,btnCat,btnDog,btnReset,btnRegisterReset,registerBtnBack;
    private ListView registerListView;
    private RegisterAdapter registerAdapter;
    private OkHttpClient okHttpClient;
    private ArrayList<AdaopData_register> registerList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String kind;
    private RelativeLayout register_relate;
    private TextView register_loadingproblem,register_loadingfail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        kind = "all";

        findViews();

        okHttpClient = new OkHttpClient();

        Thread th = new Thread(okhttpRun);
        th.start();

        initOnScrollList();
    }

//    protected void onResume(){
//
//        Thread th = new Thread(okhttpRun);
//        th.start();
//        //registerAdapter.notifyDataSetChanged();
//        super.onResume();
//
//    }

    private void findViews(){
        btnRegister = findViewById(R.id.btnRegister);
        btnCat = findViewById(R.id.btnCatR);
        btnDog = findViewById(R.id.btnDogR);
        btnReset = findViewById(R.id.btnResetR);
        btnCat.setOnClickListener(btnLis);
        btnDog.setOnClickListener(btnLis);
        btnReset.setOnClickListener(btnLis);
        registerBtnBack = findViewById(R.id.registerBtnBack);
        registerBtnBack.setOnClickListener(registerBtnBackLis);
        registerListView = findViewById(R.id.registerList);
        register_relate = findViewById(R.id.register_relate);
        register_loadingproblem = findViewById(R.id.register_loadingproblem);
        register_loadingfail = findViewById(R.id.register_loadingfail);
        swipeRefreshLayout = findViewById(R.id.swipe2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //registerListView.setVisibility(View.GONE);
                register_relate.setVisibility(View.VISIBLE);
                kind = "all";
                swipeRefreshLayout.setRefreshing(true);
                Thread th = new Thread(okhttpRun);
                th.start();
            }
        });


        btnRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(register.this,registerData.class);
                startActivity(registerIntent);
            }
        });

    }

    Button.OnClickListener registerBtnBackLis = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            //registerListView.smoothScrollToPosition(0);
        }
    };


    Button.OnClickListener btnLis = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            //registerListView.setVisibility(View.GONE);
            register_relate.setVisibility(View.VISIBLE);
            int btnWhat = v.getId();
            switch (btnWhat){
                case R.id.btnCatR:
                    kind = "貓";

                    break;
                case R.id.btnDogR:
                    kind = "狗";
                    break;
                case R.id.btnResetR:

                    kind = "all";
                    break;

            }
            Thread th = new Thread(okhttpRun);
            th.start();
        }
    };

    Runnable okhttpRun = new Runnable() {
        @Override
        public void run() {
            getOkHttpConnect("http://lcyweb.000webhostapp.com/android_db_kind.php");
            //http://lcyweb.000webhostapp.com/android_db_kind.php
            //http://lcyweb.000webhostapp.com/search_db.php
        }
    };

    private void getOkHttpConnect(String s) {
        RequestBody requestBody = new FormEncodingBuilder()
                .add("kind",kind)
                .build();
        //建立連線
        Request request = new Request.Builder()
                .url(s)
                .post(requestBody)
                .build();
        //客戶端呼叫告知需求
        Call call = okHttpClient.newCall(request);
        //接收回應
        call.enqueue(new Callback() {

            //連線錯誤時的事件
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("OKHTTP", "連線失敗" + e.getMessage());
                register_loadingfail.setVisibility(View.VISIBLE);
            }
            //有回應時的事件
            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                //System.out.println(json);

                registerList = new ArrayList<>();
                parseJSON(json);
                registerAdapter = new RegisterAdapter(register.this, registerList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        registerListView.setAdapter(registerAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        registerListView.setVisibility(View.VISIBLE);
                        register_relate.setVisibility(View.GONE);
                        register_loadingfail.setVisibility(View.GONE);

                    }
                });
            }
        });

    }
    private void initOnScrollList(){
        registerListView.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem==0){
                    registerBtnBack.setVisibility(View.GONE);
                }else{
                    registerBtnBack.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void btnregisterBack(View view){
        register.this.finish();
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
