package com.lcypj.animalregister;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;

import java.util.Calendar;
import java.util.TimeZone;

public class registerData extends AppCompatActivity {

    EditText edtRegister,edtKind,edtColor,edtAge,edtSex,
            edtFind,edtShelter,edtContact,edtTel,edtRemark,edtUrl;
    Button btnDatePick,btnSend;
    ScrollView register_scrollview;
    int mYear,mMonth,mDay;
    TextView txtDateShow;
    OkHttpClient client;
    JSONArray jsonArr;
    String txtRegister,txtUpdate,txtKind,txtColor,txtAge,txtSex,txtFind,txtShelter,txtContact,txtTel,txtRemark,txtUrl;
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ImageButton register_dataBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_data);
        initTimeset();
        findViews();


    }

    private void initTimeset(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GTM+8"));
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void findViews(){
        register_scrollview = findViewById(R.id.register_scrollview);
        edtRegister = findViewById(R.id.edtRegister);

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
        txtDateShow = findViewById(R.id.txtDateShow);
        register_dataBack = findViewById(R.id.register_dataBack);
        register_dataBack.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerData.this.finish();
            }
        });
        btnDatePick = findViewById(R.id.btnDatePick);
        btnDatePick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateListerner = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDateShow.setText(""+year+"-" + (month+1) + "-" + dayOfMonth);
                    }
                };
                new DatePickerDialog(registerData.this,dateListerner,mYear,mMonth,mDay).show();
            }
        });

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( edtRegister.getText().toString().equals("")){
                    Toast.makeText(registerData.this,"登錄人不可空白",Toast.LENGTH_SHORT).show();
                    register_scrollview.smoothScrollTo(0,0);

                }else if(edtKind.getText().toString().equals("")){
                    Toast.makeText(registerData.this,"種類不可為空白",Toast.LENGTH_SHORT).show();
                    register_scrollview.smoothScrollTo(0,0);
                }else if(edtFind.getText().toString().equals("")){
                    Toast.makeText(registerData.this,"發現區域不可空白",Toast.LENGTH_SHORT).show();
                    register_scrollview.smoothScrollTo(0,0);
                }else {
                    txtRegister = "".equals(edtRegister.getText().toString().trim()) ? "空白" : edtRegister.getText().toString();
                    txtUpdate = "".equals(txtDateShow.getText().toString().trim()) ? (mYear + "-" + mMonth + "-" + mDay) : txtDateShow.getText().toString();
                    txtKind = "".equals(edtKind.getText().toString().trim()) ? "空白" : edtKind.getText().toString();
                    txtColor = "".equals(edtColor.getText().toString().trim()) ? "空白" : edtColor.getText().toString();
                    txtAge = "".equals(edtAge.getText().toString().trim()) ? "空白" : edtAge.getText().toString();
                    txtSex = "".equals(edtSex.getText().toString().trim()) ? "空白" : edtSex.getText().toString();
                    txtFind = "".equals(edtFind.getText().toString().trim()) ? "空白" : edtFind.getText().toString();
                    txtShelter = "".equals(edtShelter.getText().toString().trim()) ? "空白" : edtShelter.getText().toString();
                    txtContact = "".equals(edtContact.getText().toString().trim()) ? "空白" : edtContact.getText().toString();
                    txtTel = "".equals(edtTel.getText().toString().trim()) ? "空白" : edtTel.getText().toString();
                    txtRemark = "".equals(edtRemark.getText().toString().trim()) ? "空白" : edtRemark.getText().toString();
                    txtUrl = "".equals(edtUrl.getText().toString().trim()) ? "空白" : edtUrl.getText().toString();

                    new AlertDialog.Builder(registerData.this)
                            .setTitle("送出確認")
                            .setMessage("請問是否確定要送出？")
                            .setPositiveButton("送出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            postOkHttp();
                                        }
                                    }).start();
                                    edtRegister.setText("");
                                    //edtUpdate.setText("");
                                    txtDateShow.setText("");
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

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    register_scrollview.smoothScrollTo(0,0);
                                }
                            })
                            .show();

                }
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
//    public void btndataBack(){
//        Intent it = new Intent(registerData.this,register.class);
//        startActivity(it);
//        this.finish();
//
//    }

}
