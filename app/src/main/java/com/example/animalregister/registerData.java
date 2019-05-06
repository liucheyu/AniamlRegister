package com.example.animalregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;

public class registerData extends AppCompatActivity {

    EditText edtRegister,edtUpdate,edtKind,edtColor,edtAge,edtSex,
            edtFind,edtShelter,edtContact,edtTel,edtRemark,edtUrl;
    Button btnSend;
    OkHttpClient client;
    JSONArray jsonArr;
    String txtRegister,txtUpdate,txtKind,txtColor,txtAge,txtSex,txtFind,txtShelter,txtContact,txtTel,txtRemark,txtUrl;
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_data);

        findViews();


    }

    private void findViews(){
        edtRegister = findViewById(R.id.edtRegister);
        edtUpdate = findViewById(R.id.edtUpdate);
        edtKind = findViewById(R.id.edtKind);
        edtColor = findViewById(R.id.edtColor);
        edtAge = findViewById(R.id.edtAge);
        edtSex = findViewById(R.id.edtSex);
        edtFind = findViewById(R.id.edtFind);
        edtShelter = findViewById(R.id.edtShelter);
        edtContact = findViewById(R.id.edtContact);
        edtTel = findViewById(R.id.edtTel);
        edtRemark = findViewById(R.id.edtRemark);
        edtUrl = findViewById(R.id.edtUrl);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtRegister = edtRegister.getText().toString();
                txtUpdate = edtUpdate.getText().toString();
                txtKind = edtKind.getText().toString();
                txtColor = edtColor.getText().toString();
                txtAge = edtAge.getText().toString();
                txtSex = edtSex.getText().toString();
                txtFind = edtFind.getText().toString();
                txtShelter = edtShelter.getText().toString();
                txtContact = edtContact.getText().toString();
                txtTel = edtTel.getText().toString();
                txtRemark = edtRemark.getText().toString();
                txtUrl = edtRemark.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postOkHttp();
                    }
                }).start();
                edtRegister.setText("");
                edtUpdate.setText("");
                edtKind.setText("");
                edtColor.setText("");
                edtAge.setText("");
                edtSex.setText("");
                edtFind.setText("");
                edtShelter.setText("");
                edtContact.setText("");
                edtTel.setText("");
                edtRemark.setText("");
                edtUrl.setText("");
//                jsonArr = new JSONArray();
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("register_name", txtRegister);
//                    obj.put("register_update", txtUpdate);
//                    obj.put("register_kind", txtKind);
//                    obj.put("register_color", txtColor);
//                    obj.put("register_age", txtAge);
//                    obj.put("register_sex", txtSex);
//                    obj.put("register_find", txtFind);
//                    obj.put("register_shelter", txtShelter);
//                    obj.put("register_contact", txtContact);
//                    obj.put("register_tel", txtTel);
//                    obj.put("register_remark", txtRemark);
//                    obj.put("register_url", txtUrl);
//                    jsonArr.put(obj);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
            }

        });

    }
    private void postOkHttp() {

        client = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("register_name", txtRegister)
                .add("register_update", txtUpdate)
                .add("register_kind", txtKind)
                .add("register_color", txtColor)
                .add("register_age", txtAge)
                .add("register_sex", txtSex)
                .add("register_find", txtFind)
                .add("register_shelter", txtShelter)
                .add("register_contact", txtContact)
                .add("register_tel", txtTel)
                .add("register_remark", txtRemark)
                .add("register_url", txtUrl)
                .build();

        final Request request = new Request.Builder()
                //需要填入伺服器網址的php
                .url("http://lcyweb.000webhostapp.com/insert_data.php")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        //接收回應
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("okhttp","回傳失敗" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);
            }
        });

    }

}
