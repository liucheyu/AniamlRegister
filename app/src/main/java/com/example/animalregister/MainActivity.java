package com.example.animalregister;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//okhttp2.7.5時import下方
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {


   // private ListView adoplv;
    private RecyclerView recyclerView;
    private Button btnCat,btnDog,btnReset;
    private Button btnRegion,btnUpdate,btnAge,btnSex;
    //private RelativeLayout relativeImage;

    //客戶端okHttp連線的物件
    private OkHttpClient okHttpClient;
    //在parseJSON()中加入AdopData物件
    private ArrayList<AdopData> adopList;
    //動物領養的JSON resourse
    private String url = "http://data.coa.gov.tw/Service/OpenData/TransService.aspx?UnitId=QcbUEzN6E6DL";
    private String plusUrl = new String(url);
    private String kindFilter = "";
    private String timeFilter = "";
    private String regionFilter = "";
    private String ageFilter = "";
    private String sexFilter = "";
    //private AdopAdapter adopAdapter;
    private MyAdapter adopAdapter;
    //側選單
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String json;
    FavoriteDBController DBController;
    private Calendar cal;
    //UrlFilter urlFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //urlFilter = new UrlFilter(url);
        DBController = new FavoriteDBController(MainActivity.this);
        findViews();

        //電話的權限許可通知
        chechPermision();

        okHttpClient = new OkHttpClient();
        //使用okhttp回傳JSON，當中還有解析JSON加進ArrayList的方法，以及設定Adapter



        new Thread(firstR).start();


    }

    Runnable firstR = new Runnable() {
        @Override
        public void run() {
            getOkHttpConnect(url);
            //使用okHttp連線，取得JSON回應執行parseJSIN方法,使用runOnUiThread及setAdapter(ListVew)

        }

    };

    protected void onDestroy(){
        DBController.close();
        super.onDestroy();

    }

    private void findViews(){
        //adoplv = findViewById(R.id.adopList);
        recyclerView = findViewById(R.id.recycleview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        btnCat = findViewById(R.id.btnCat);
        btnDog = findViewById(R.id.btnDog);
        btnReset = findViewById(R.id.btnReset);
        btnCat.setOnClickListener(kindListener);
        btnDog.setOnClickListener(kindListener);
        btnReset.setOnClickListener(kindListener);
        //顯示圖片用的畫面，預設是不顯示，在ListView那邊點及圖片會顯示
        //在這邊再點一下會關閉
//        relativeImage = findViewById(R.id.relateiveImage);
//        relativeImage.setOnTouchListener(relativeLis);


        //以下為PopupMenu
        btnRegion = findViewById(R.id.btnRegion);
        btnRegion.setOnClickListener(regionLis);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(updateLis);
        btnAge = findViewById(R.id.btnAge);
        btnAge.setOnClickListener(ageLis);
        btnSex = findViewById(R.id.btnSex);
        btnSex.setOnClickListener(sexLis);

        //側邊選單
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //結合DrawerLayout和ToolBar的物件
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //Navigation的點擊事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //點擊時收起
                drawerLayout.closeDrawer(GravityCompat.START);
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.adop_page:
                        break;
                    case R.id.register_page:
                        Intent registerIt = new Intent();
                        registerIt.setClass(MainActivity.this,register.class);
                        startActivity(registerIt);
                        break;
                    case R.id.favorite_page:
                        Intent favoriteIt = new Intent();
                        favoriteIt.setClass(MainActivity.this,FavoriteActivity.class);
                        startActivity(favoriteIt);
                        break;
                    case R.id.about_page:
                        AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
                        d.setTitle("關於");
                        d.setMessage("歡迎使用此App，\n希望能透過此App\n讓更多流浪動物受到應有照顧。" +
                                "\n關於此App有任何問題，\n請與開發者聯繫。\n開發者：LCY\nMail：liucheyu1987@gmail.com");
                        d.show();
                        break;
                }
                return false;
            }
        });

    }



    //貓和狗的按鈕點擊事件
    Button.OnClickListener kindListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            //String newJSON = new String(json);
            //String animalkind = null;

            switch (v.getId()){
                case R.id.btnCat:
                    btnDog.setEnabled(false);
//                    btnAge.setVisibility(View.VISIBLE);
//                    btnRegion.setVisibility(View.VISIBLE);
//                    btnSex.setVisibility(View.VISIBLE);
//                    btnUpdate.setVisibility(View.VISIBLE);


                    //以下為另外一個方法，就是依照官方opendata公布的網址篩選規則
                    //"http://data.coa.gov.tw/Service/OpenData/TransService.aspx?UnitId=QcbUEzN6E6DL" + "&key=value"
                    //放近URL內的中文字需要編碼成16進位
                    try {
                        String text = URLEncoder.encode("貓","utf-8");
                        kindFilter = "&animal_kind=" + text;
                        //urlFilter會自動代入fileter
                        plusUrl = plusUrl + kindFilter;
                        //kind=0,update=1,region=2,age=3,sex=4;
                        final String cattemp = plusUrl.trim();

                         new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getOkHttpConnect(cattemp);
                            }
                        }).start();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    btnCat.setEnabled(false);

                    break;
                case R.id.btnDog:
                    btnCat.setEnabled(false);
//                    btnAge.setVisibility(View.VISIBLE);
//                    btnRegion.setVisibility(View.VISIBLE);
//                    btnSex.setVisibility(View.VISIBLE);
//                    btnUpdate.setVisibility(View.VISIBLE);
                    try {
                        String text = URLEncoder.encode("狗","utf-8");
                        kindFilter = "&animal_kind=" + text;
                        plusUrl = plusUrl + kindFilter;
                        //kind=0,update=1,region=2,age=3,sex=4;
                        final String dogtemp = plusUrl.trim();
                        //okHttpClient = new OkHttpClient();
                        //此方法內有除了有okHttp所做的回應外，還有解析JSON，以及setAdaper

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getOkHttpConnect(dogtemp);
                            }
                        }).start();


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    btnDog.setEnabled(false);
                    break;
                case R.id.btnReset:
//                    plusUrl="";
                    plusUrl = new String(url);
                    btnCat.setEnabled(true);
                    btnDog.setEnabled(true);
                    btnAge.setEnabled(true);
                    btnAge.setText(R.string.all_age);
                    btnRegion.setEnabled(true);
                    btnRegion.setText(R.string.allRegion);
                    btnSex.setEnabled(true);
                    btnSex.setText(R.string.all_sex);
                    btnUpdate.setEnabled(true);
                    btnUpdate.setText(R.string.all_days);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getOkHttpConnect(url);
                        }
                    }).start();
                    break;
            }

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
                json = response.body().string();
                adopList = new ArrayList<>();
                parseJSON(json,adopList);
                adopAdapter = new MyAdapter(MainActivity.this, adopList, DBController);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adopAdapter);
                    }
                });
            }
        });

    }

    private void parseJSON(String s,ArrayList<AdopData> arrayList){

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
                String kind = obj.getString("animal_kind");
                String update = obj.getString("animal_update");
                String animailId = obj.getString("animal_id");
                String color = obj.getString("animal_colour");
                String age = obj.getString("animal_age");
                String sex = obj.getString("animal_sex");
                String shelter = obj.getString("shelter_name");
                String address = obj.getString("shelter_address");
                String tel = obj.getString("shelter_tel");
                String remark = obj.getString("animal_remark");
                String imageURL = obj.getString("album_file");

                AdopData adopData = new AdopData(kind,update,animailId,color,age,sex,shelter,address,tel,remark,imageURL);

                arrayList.add(adopData);

          }

        }catch (JSONException e){
            e.printStackTrace();
        }


    }

    private void chechPermision(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },1);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


    }

    Button.OnClickListener regionLis = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
            popupMenu.getMenuInflater().inflate(R.menu.popupmenu_region,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.allRegion:
                            btnRegion.setText(getResources().getString(R.string.allRegion));
                            //plusUrl是opendata複製的網址 + 條件
                            plusUrl = plusUrl + "";
                            final String allTimeTemp = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(allTimeTemp);
                                }
                            }).start();
                            break;
                        case R.id.taipei:
                            btnRegion.setText(getResources().getString(R.string.taipei));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 2;
                            plusUrl = plusUrl + regionFilter;
                            final String region02 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region02);
                                }
                            }).start();
                            break;
                        case R.id.newtaipei:
                            btnRegion.setText(getResources().getString(R.string.newtaipei));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 3;
                            plusUrl = plusUrl + regionFilter;
                            final String region03 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region03);
                                }
                            }).start();
                            break;
                        case R.id.keelung:
                            btnRegion.setText(getResources().getString(R.string.keelung));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 4;
                            plusUrl = plusUrl + regionFilter;
                            final String region04 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region04);
                                }
                            }).start();
                            break;
                        case R.id.yilan:
                            btnRegion.setText(getResources().getString(R.string.yilan));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 5;
                            plusUrl = plusUrl + regionFilter;
                            final String region05 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region05);
                                }
                            }).start();
                            break;
                        case R.id.taoyuan:
                            btnRegion.setText(getResources().getString(R.string.taoyuan));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 6;
                            plusUrl = plusUrl + regionFilter;
                            final String region06 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region06);
                                }
                            }).start();
                            break;
                        case R.id.hsinchu:
                            btnRegion.setText(getResources().getString(R.string.hsinchu));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 7;
                            plusUrl = plusUrl + regionFilter;
                            final String region07 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region07);
                                }
                            }).start();
                            break;
                        case R.id.hsinchu_city:
                            btnRegion.setText(getResources().getString(R.string.hsinchu_city));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 8;
                            plusUrl = plusUrl + regionFilter;
                            final String region08 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region08);
                                }
                            }).start();
                            break;
                        case R.id.miaoli:
                            btnRegion.setText(getResources().getString(R.string.miaoli));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 9;
                            plusUrl = plusUrl + regionFilter;
                            final String region09 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region09);
                                }
                            }).start();
                            break;
                        case R.id.taichung:
                            btnRegion.setText(getResources().getString(R.string.taichung));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 10;
                            plusUrl = plusUrl + regionFilter;
                            final String region10 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region10);
                                }
                            }).start();
                            break;
                        case R.id.changhua:
                            btnRegion.setText(getResources().getString(R.string.changhua));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 11;
                            plusUrl = plusUrl + regionFilter;
                            final String region11 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region11);
                                }
                            }).start();
                            break;
                        case R.id.nantou:
                            btnRegion.setText(getResources().getString(R.string.nantou));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 12;
                            plusUrl = plusUrl + regionFilter;
                            final String region12 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region12);
                                }
                            }).start();
                            break;

                        case R.id.yunlin:
                            btnRegion.setText(getResources().getString(R.string.yunlin));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 13;
                            plusUrl = plusUrl + regionFilter;
                            final String region13 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region13);
                                }
                            }).start();
                            break;
                        case R.id.chiayi:
                            btnRegion.setText(getResources().getString(R.string.chiayi));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 14;
                            plusUrl = plusUrl + regionFilter;
                            final String region14 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region14);
                                }
                            }).start();
                            break;
                        case R.id.chiayi_city:
                            btnRegion.setText(getResources().getString(R.string.chiayi_city));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 15;
                            plusUrl = plusUrl + regionFilter;
                            final String region15 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region15);
                                }
                            }).start();
                            break;
                        case R.id.tainan:
                            btnRegion.setText(getResources().getString(R.string.tainan));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 16;
                            plusUrl = plusUrl + regionFilter;
                            final String region16 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region16);
                                }
                            }).start();
                            break;
                        case R.id.kaohsiung:
                            btnRegion.setText(getResources().getString(R.string.kaohsiung));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 17;
                            plusUrl = plusUrl + regionFilter;
                            final String region17 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region17);
                                }
                            }).start();
                            break;
                        case R.id.pingtung:
                            btnRegion.setText(getResources().getString(R.string.pingtung));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 18;
                            plusUrl = plusUrl + regionFilter;
                            final String region18 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region18);
                                }
                            }).start();
                            break;
                        case R.id.hualien:
                            btnRegion.setText(getResources().getString(R.string.hualien));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 19;
                            plusUrl = plusUrl + regionFilter;
                            final String region19 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region19);
                                }
                            }).start();
                            break;
                        case R.id.taitung:
                            btnRegion.setText(getResources().getString(R.string.taitung));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 20;
                            plusUrl = plusUrl + regionFilter;
                            final String region20 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region20);
                                }
                            }).start();
                            break;
                        case R.id.penghu:
                            btnRegion.setText(getResources().getString(R.string.penghu));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 21;
                            plusUrl = plusUrl + regionFilter;
                            final String region21 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region21);
                                }
                            }).start();
                            break;
                        case R.id.kinmen:
                            btnRegion.setText(getResources().getString(R.string.kinmen));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 22;
                            plusUrl = plusUrl + regionFilter;
                            final String region22 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region22);
                                }
                            }).start();
                            break;
                        case R.id.lienchiang:
                            btnRegion.setText(getResources().getString(R.string.lienchiang));
                            //plusUrl是opendata複製的網址 + 條件
                            regionFilter = "&animal_area_pkid=" + 23;
                            plusUrl = plusUrl + regionFilter;
                            final String region23 = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(region23);
                                }
                            }).start();
                            break;


                    }
                    return true;
                }
            });

            popupMenu.show();
            btnRegion.setEnabled(false);

        }
    };



    Button.OnClickListener updateLis = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            final int year = cal.get(Calendar.YEAR);
            final int month = cal.get(Calendar.MONTH) + 1;
            final int day = cal.get(Calendar.DAY_OF_MONTH);
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
            popupMenu.getMenuInflater().inflate(R.menu.popupmenu_update,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.all_days:
                            btnUpdate.setText(getResources().getString(R.string.all_days));
                            plusUrl = plusUrl + "";
                            //kind=0,update=1,region=2,age=3,sex=4;
                            final String allTimeTemp = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(allTimeTemp);
                                }
                            }).start();
                            break;

                        case R.id.one_month:
                            btnUpdate.setText(getResources().getString(R.string.one_month));
                            try {
                                //String text = URLEncoder.encode("貓","utf-8");

                                if(month<10){
                                    timeFilter  = "&animal_update=" + year+ "/"+ "0" + month;
                                }else{
                                    timeFilter  = "&animal_update=" + year+ "/" + month;
                                }

                                plusUrl = plusUrl + timeFilter;
                                //kind=0,update=1,region=2,age=3,sex=4;
                                final String monthTemp = plusUrl.trim();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOkHttpConnect(monthTemp);
                                    }
                                }).start();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.todays:
                            btnUpdate.setText(getResources().getString(R.string.today));
                            try {

                                if(month<10){
                                    if(day<10){
                                        timeFilter  = "&animal_update=" + year+ "/"+ "0" + month + "/" + "0" + day;
                                    }else{
                                        timeFilter  = "&animal_update=" + year+ "/"+ "0" + month + "/" + day;
                                    }

                                }else{
                                    if(day<10){
                                        timeFilter  = "&animal_update=" + year+ "/" + month + "/" + "0" + day;
                                    }else{
                                        timeFilter  = "&animal_update=" + year+ "/"+ month + "/" + day;
                                    }

                                }
                                plusUrl = plusUrl + timeFilter;
                                //kind=0,update=1,region=2,age=3,sex=4;
                                final String dayTemp = plusUrl.trim();
                                //okHttpClient = new OkHttpClient();
                                //此方法內有除了有okHttp所做的回應外，還有解析JSON，以及setAdaper

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOkHttpConnect(dayTemp);
                                    }
                                }).start();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                    }
                    return true;
                }
            });
            popupMenu.show();
            btnUpdate.setEnabled(false);
        }
    };
    Button.OnClickListener ageLis = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
            popupMenu.getMenuInflater().inflate(R.menu.popupmenu_age,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.all_age:
                            btnAge.setText(getResources().getString(R.string.all_age));
                            plusUrl = plusUrl + "";
                            //kind=0,update=1,region=2,age=3,sex=4;
                            final String allAgeTemp = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(allAgeTemp);
                                }
                            }).start();
                            break;
                        case R.id.child:
                            btnAge.setText(getResources().getString(R.string.child));
                            try {
                                String text = URLEncoder.encode("CHILD","utf-8");
                                ageFilter = "&animal_age=" + text;
                                plusUrl = plusUrl + ageFilter;
                                //kind=0,update=1,region=2,age=3,sex=4;
                                final String childTemp = plusUrl.trim();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOkHttpConnect(childTemp);
                                    }
                                }).start();

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.adult:
                            btnAge.setText(getResources().getString(R.string.adult));
                            try {
                                String text = URLEncoder.encode("ADULT","utf-8");
                                ageFilter = "&animal_age=" + text;
                                plusUrl = plusUrl + ageFilter;
                                //kind=0,update=1,region=2,age=3,sex=4;
                                final String adultTemp = plusUrl.trim();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOkHttpConnect(adultTemp);
                                    }
                                }).start();

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            break;

                    }
                    return true;
                }
            });
            popupMenu.show();
            btnAge.setEnabled(false);
        }
    };
    Button.OnClickListener sexLis = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
            popupMenu.getMenuInflater().inflate(R.menu.popupmenu_sex,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.all_sex:
                            btnSex.setText(getResources().getString(R.string.all_sex));
                            plusUrl = plusUrl + "";
                            //kind=0,update=1,region=2,age=3,sex=4;
                            final String allsexTemp = plusUrl.trim();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getOkHttpConnect(allsexTemp);
                                }
                            }).start();
                            break;
                        case R.id.male:
                            btnSex.setText(getResources().getString(R.string.male));
                            try {
                                String text = URLEncoder.encode("M","utf-8");
                                sexFilter = "&animal_sex=" + text;
                                plusUrl = plusUrl + sexFilter;
                                //kind=0,update=1,region=2,age=3,sex=4;
                                final String maelTemp = plusUrl.trim();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOkHttpConnect(maelTemp);
                                    }
                                }).start();

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.female:
                            btnSex.setText(getResources().getString(R.string.female));
                            try {
                                String text = URLEncoder.encode("F","utf-8");
                                sexFilter = "&animal_sex=" + text;
                                plusUrl = plusUrl + sexFilter;
                                //kind=0,update=1,region=2,age=3,sex=4;
                                final String femaelTemp = plusUrl.trim();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOkHttpConnect(femaelTemp);
                                    }
                                }).start();

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            break;

                    }
                    return true;
                }
            });
            popupMenu.show();
            btnSex.setEnabled(false);
        }
    };

//    View.OnTouchListener relativeLis = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            v.setVisibility(View.GONE);
//            return false;
//        }
//
//    };


//    private void separateKind(String s,String animalkind){
//        ArrayList<AdopData> arrayList = new ArrayList<>();
//        AdopData adopData;
//        try {
//
//            JSONArray jsonArray = new JSONArray(s);
//            for (int i = 0; i<jsonArray.length(); i++) {
//                JSONObject obj = jsonArray.getJSONObject(i);
//                String kind = obj.getString("animal_kind");
//                String update = obj.getString("animal_update");
//                String animailId = obj.getString("animal_id");
//                String color = obj.getString("animal_colour");
//                String age = obj.getString("animal_age");
//                String sex = obj.getString("animal_sex");
//                String shelter = obj.getString("shelter_name");
//                String address = obj.getString("shelter_address");
//                String tel = obj.getString("shelter_tel");
//                String remark = obj.getString("animal_remark");
//                String imageURL = obj.getString("album_file");
//
//                adopData = new AdopData(kind,update,animailId,color,age,sex,shelter,address,tel,remark,imageURL);
//
//                if(adopData.getKind().equals(animalkind)){
//                    arrayList.add(adopData);
//                    adopAdapter = new AdopAdapter(MainActivity.this, arrayList);
//
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                           adoplv.setAdapter(adopAdapter);
//                     }
//                  });
//                }
//
//            }
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//    }


}
