package com.lcypj.animalregister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoriteActivity extends AppCompatActivity {
    private ArrayList<AdopData> favotiteList;
    private FavoriteAdapter favoriteAdapter;
    private ListView falv;
    private ImageButton btnMap,btnTel,btnFavotite,btnClose;
    private FavoriteDBController DBController;
    private RelativeLayout favoriteRelate;
    private Button favotiteBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        falv = findViewById(R.id.favoriteList);
        favoriteRelate = findViewById(R.id.favoriteRelate);
        favotiteBtnBack = findViewById(R.id.favotiteBtnBack);
        favotiteBtnBack.setOnClickListener(favotiteBtnBackLis);
        DBController = new FavoriteDBController(FavoriteActivity.this);
        Thread th = new Thread(r);
        th.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(favotiteList.size() == 0){
                            favoriteRelate.setVisibility(View.VISIBLE);
                            falv.setVisibility(View.GONE);
                        }else{
                            favoriteRelate.setVisibility(View.GONE);
                            falv.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();

//        initList();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                falv.setAdapter(favoriteAdapter);
//            }
//        });

        //以下為使用內存將使用JSON格式存成txt檔案，再取出來解析
        //File dir = getApplicationContext().getFilesDir();
        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"favorite_list.txt");
        //File dir = getApplicationContext().getFilesDir();
        //生成檔案物件，設定路徑
//        File file = new File(dir,"favorite.txt");
//        StringBuilder sb = new StringBuilder();
//        BufferedReader reader = null;
//        FileInputStream fs;
//        try{
//            //檔案輸入流
//            fs = new FileInputStream(file);
//            //讀取檔案輸入流物件
//            InputStreamReader sr = new InputStreamReader(fs,"utf-8");
//            //緩衝
//            reader = new BufferedReader(sr);
//            String line;
//            while((line = reader.readLine())!=null){
//                sb.append(line);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally {
//            try{
//                reader.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        String json = sb.toString();
        //自行將文檔中的JSON陣列前後加[]變成陣列，
        //因為後方還有一個逗點，所以加一個{}避免錯誤，在抓JSON時，length-1就行了
//        String jsonarr = "["+json+"{}"+"]";
//        parseJSON(jsonarr);

    }

    Button.OnClickListener favotiteBtnBackLis = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            falv.smoothScrollToPosition(0);
        }
    };

    Runnable r = new Runnable() {
        @Override
        public void run() {
            DBController.query();
            favotiteList = DBController.getList();
            favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,favotiteList);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    falv.setAdapter(favoriteAdapter);
                }
            });
        }
    };

    private void initList(){
        DBController.query();
        favotiteList = DBController.getList();
        favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,favotiteList);
    }




//    private void parseJSON(String s){
//        favotiteList = new ArrayList<>();
//        try {
//            //動物領養JSON的為外層是JSON陣列
//            JSONArray arr = new JSONArray(s);
//            //使用for迴圈，將解出陣列中的每一個JSON物件，
//            //並以key取得物件中每一個value，
//            //將每個value給該有的參考名稱
//            //生成AdopData物件，放入value的參考名稱
//            //迴圈會在每次生成AdopData物件放入集合adopList
//            for (int i = 0; i<arr.length()-1; i++) {
//                JSONObject obj = arr.getJSONObject(i);
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
//                AdopData adopData = new AdopData(kind,update,animailId,color,age,sex,shelter,address,tel,remark,imageURL);
//
//                favotiteList.add(adopData);
//                favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, favotiteList);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        falv.setAdapter(favoriteAdapter);
//                    }
//                });
//
//            }
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//
//    }


    private class FavoriteAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater;
        private ArrayList<AdopData> favotiteList;
        private Context context;

        public FavoriteAdapter(Context context, ArrayList<AdopData> favotiteList) {
            this.context = context;
            this.favotiteList = favotiteList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return favotiteList.size();
        }

        @Override
        public Object getItem(int position) {
            return favotiteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = layoutInflater.inflate(R.layout.adop_listview,parent,false);
            }
            final AdopData adopData = favotiteList.get(position);

            TextView kind = (TextView)convertView.findViewById(R.id.txtKind);
            kind.setText(String.valueOf(adopData.getKind()));

            TextView update = (TextView)convertView.findViewById(R.id.txtUpdate);
            update.setText(String.valueOf(adopData.getUpdate()));

            final TextView animalId = (TextView)convertView.findViewById(R.id.txtId);
            final String animalid = String.valueOf(adopData.getAnimalId());
            animalId.setText(animalid);

            TextView color = (TextView)convertView.findViewById(R.id.txtColor);
            color.setText(String.valueOf(adopData.getColor()));

            TextView age = (TextView)convertView.findViewById(R.id.txtAge);
            String ageget = String.valueOf(adopData.getAge());
            if(ageget.equals("CHILD")){
                age.setText("幼齡");
            }else if(ageget.equals("ADULT")){
                age.setText("成年");
            }else{
                age.setText("未知");
            }

            TextView sex = (TextView)convertView.findViewById(R.id.txtSex);
            String boy = "男生";
            String girl = "女生";
            String sexdata = String.valueOf(adopData.getSex());
            if(sexdata.equals("M")){
                sex.setText(boy);
            }else if(sexdata.equals("F")){
                sex.setText(girl);
            }else {
                sex.setText("未知");
            }

            TextView shelter = (TextView)convertView.findViewById(R.id.txtShelter);
            final String shelterName = String.valueOf(adopData.getShelter());
            shelter.setText(shelterName);
            TextView address = (TextView)convertView.findViewById(R.id.txtAddress);
            final String shelterAddress = String.valueOf(adopData.getAddress());
            address.setText(shelterAddress);
            TextView tel = (TextView)convertView.findViewById(R.id.txtTel);
            tel.setText(String.valueOf(adopData.getTel()));
            TextView remark = (TextView)convertView.findViewById(R.id.txtRemark);
            remark.setText(String.valueOf(adopData.getRemark()));

            ImageView animailImage = (ImageView)convertView.findViewById(R.id.animalImage);
            animailImage.setImageResource(R.mipmap.noimage);
            String image = adopData.getImageURL();
            Glide.with(FavoriteActivity.this)
                    .load(image)
                    .placeholder(R.mipmap.noimage)
                    .error(R.mipmap.noimage)
                    .into(animailImage);
            //判斷是否是空字串，不是空字串就是網址
//            if(!image.equals("")){
                //animailImage.setImageResource(R.mipmap.loading);
                //getPic方法使用okHttp取得網址回應，再將回應的串流轉成Bitmap
//                getPic(image,animailImage);



//            }
            btnMap = (ImageButton) convertView.findViewById(R.id.btnmap);
            btnTel = (ImageButton) convertView.findViewById(R.id.btntel);
            btnFavotite = (ImageButton) convertView.findViewById(R.id.btnFavorite);
            btnFavotite.setVisibility(View.GONE);
            btnClose = convertView.findViewById(R.id.btnClose);
            btnClose.setVisibility(View.VISIBLE);
            final View view = convertView;
            btnClose.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //view.setVisibility(View.GONE);
                    //先使ArrayList刪除項目，
                    //並重新setAdapter後，
                    //再來刪除資料庫所屬項目
                    favotiteList.remove(position);

                    favoriteAdapter.notifyDataSetChanged();
                    if(favotiteList.size() == 0){
                        favoriteRelate.setVisibility(View.VISIBLE);
                        falv.setVisibility(View.GONE);
                    }else{
                        favoriteRelate.setVisibility(View.GONE);
                    }

                    DBController.delete(animalid);

                }
            });


            btnMap.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //利用自訂方法將地址解成經緯度後，以double型態傳送
                    //由於Bundle不能包經緯度，直接以double傳送

                    String shelter = new String(shelterName);//因插針需要，將位置名稱複製過來(應可以直接使用不必複製)

                    //自訂方法會return回double陣列
                    double[] latlng = getLatLngFromAddress(shelterAddress);

                    //分別將陣列拆開
                    Double latitude = latlng[0];
                    Double longitude = latlng[1];

                    //印出檢查(Logcat會顯示)
                    System.out.println(shelterName);
                    System.out.println("latlng:"+String.valueOf(latitude)+","+String.valueOf(longitude));

                    //測試用
//                Uri uri = Uri.parse("geo:"+String.valueOf(latitude)+","+String.valueOf(longitude));
//                Intent it = new Intent(Intent.ACTION_VIEW,uri);
//                context.startActivity(it);

                    //包裹&轉換畫面
                    Intent it = new Intent(context,MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitude",latitude);
                    bundle.putDouble("longitude",longitude);
                    bundle.putString("shelterName",shelter);
                    bundle.putString("shelterAddress",shelterAddress);
                    it.putExtras(bundle);
                    context.startActivity(it);

                }
            });
            btnTel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = (String)adopData.getTel();
                    Uri uri =Uri.parse("tel:"+phoneNumber);
                    Intent dial = new Intent(Intent.ACTION_VIEW,uri);
//                dial.setAction("android.intent.action.call");
//                dial.setData(Uri.parse("tel:"+phoneNumber));
                    //getPackageManager可接收是否有安裝或是授權的訊息
                    if(dial.resolveActivity(context.getPackageManager()) != null){
                        context.startActivity(dial);
                    }
                }
            });


            return convertView;
        }
    }
    //使用Geocoder將地址反查成經緯度
    private double[] getLatLngFromAddress(String address){
        double[] latlng = null;
        Geocoder geocoder = new Geocoder(FavoriteActivity.this, Locale.getDefault());
        try {
            List<Address> addressLocation = geocoder.getFromLocationName(address, 1);
            double latitude = addressLocation.get(0).getLatitude();
            double longitude = addressLocation.get(0).getLongitude();
            latlng = new double[]{latitude,longitude};

        }catch (IOException e){
            e.printStackTrace();
        }

        return latlng;
    }
    private void getPic(String s, final ImageView imageView){

        //final Bitmap[] bitmap = new Bitmap[1];
        OkHttpClient client = new OkHttpClient();
        //建立連線
        final Request request = new Request.Builder()
                .url(s)
                .build();
        //客戶端呼叫告知需求
        Call call = client.newCall(request);
        //接收回應
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                imageView.setImageResource(R.mipmap.noimage);
                Log.d("okhttp","圖片錯誤");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                try {
                    //載進串流
                    InputStream in = response.body().byteStream();
                    //解成Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    imageView.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();

                }

            }
        });

        //return bitmap[0];
    }

}
