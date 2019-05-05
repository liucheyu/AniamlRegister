package com.example.animalregister;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;

public class AdopAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<AdopData> adopList;
    //private Bitmap bitmap;
    private ImageButton btnMap,btnTel,btnFavotite;
    private Context context;


    AdopAdapter(Context context, ArrayList<AdopData> adopList){
        this.adopList = adopList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return adopList.size();
    }

    @Override
    public Object getItem(int position) {
        return adopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.adop_listview,parent,false);
        }

        final AdopData adopData = adopList.get(position);
        TextView kind = (TextView)convertView.findViewById(R.id.txtKind);
        kind.setText(String.valueOf(adopData.getKind()));
        TextView update = (TextView)convertView.findViewById(R.id.txtUpdate);
        update.setText(String.valueOf(adopData.getUpdate()));
        TextView animalId = (TextView)convertView.findViewById(R.id.txtId);
        animalId.setText(String.valueOf(adopData.getAnimalId()));
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

        final TextView shelter = (TextView)convertView.findViewById(R.id.txtShelter);
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
        //判斷是否是空字串，不是空字串就是網址
        if(!image.equals("")){
            //animailImage.setImageResource(R.mipmap.loading);
            //getPic方法使用okHttp取得網址回應，再將回應的串流轉成Bitmap
            //getPic(image,animailImage);

        }


        //圖片點擊放大的事件
        animailImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });


        btnMap = (ImageButton) convertView.findViewById(R.id.btnmap);
        btnTel = (ImageButton) convertView.findViewById(R.id.btntel);
        btnFavotite = (ImageButton) convertView.findViewById(R.id.btnFavorite);
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

        btnFavotite.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject obj = new JSONObject();
                JSONArray arr = new JSONArray();
                String kindF = String.valueOf(adopData.getKind());
                String updateF = String.valueOf(adopData.getUpdate());
                String animalIdF = String.valueOf(adopData.getAnimalId());
                String colorF = String.valueOf(adopData.getColor());
                String ageF = String.valueOf(adopData.getAge());
                String sexF = String.valueOf(adopData.getSex());
                String shelterF = String.valueOf(adopData.getShelter());
                String shelterAddressF = String.valueOf(adopData.getAddress());
                String telF = String.valueOf(adopData.getTel());
                String remarkF = String.valueOf(adopData.getRemark());
                String imageurlF =  String.valueOf(adopData.getImageURL());

                try {
                    obj.put("animal_kind",kindF);
                    obj.put("animal_update",updateF);
                    obj.put("animal_id",animalIdF);
                    obj.put("animal_colour",colorF);
                    obj.put("animal_age",ageF);
                    obj.put("animal_sex",sexF);
                    obj.put("shelter_name",shelterF);
                    obj.put("shelter_address",shelterAddressF);
                    obj.put("shelter_tel",telF);
                    obj.put("animal_remark",remarkF);
                    obj.put("album_file",imageurlF);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = obj.toString();
                String dot = ",";
//                File file = getExtPubPicDir("favorite_list.txt");
                File dir = context.getFilesDir();
                //生成檔案物件，設定路徑
                File file = new File(dir,"favorite.txt");
                //檔案輸出流
                FileOutputStream fos = null;
                try {
                    //生成檔案輸出流物件，給路徑及是否繼續編寫
                    fos = new FileOutputStream(file,true);
                    //寫檔
                    fos.write(json.getBytes());
                    fos.write(dot.getBytes());
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try{
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                Toast.makeText(context,"增加到我的最愛",Toast.LENGTH_SHORT).show();

            }
        });


        return convertView;
    }
    public File getExtPubPicDir(String dir){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),dir);
        if(!file.mkdirs()){
            Log.e("LOG_TAG","無法建立目錄");

        }
        return file;
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


    //使用Geocoder將地址反查成經緯度
    private double[] getLatLngFromAddress(String address){
        double[] latlng = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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



        //由於AsyncTask只能被調用一次，而getView會連續調用，所以這邊不適合使用。
//    private class AsyncParseImage extends AsyncTask<String, Void, Bitmap> {
//
//        ImageView imageView;
//        Bitmap[] bitmap = new Bitmap[1];
//        public AsyncParseImage(ImageView imageView){
//            AsyncParseImage.this.imageView = imageView;
//        }
//
//

//        @Override
//        protected Bitmap doInBackground(String... strings) {
//
//           try{
//                URL url = new URL(strings[0]);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                int v ;
//                if( connection.getContentLength() > 0){
//                    v = connection.getContentLength();
//                }else {
//                    v = 0;
//                }
//                //int v = connection.getContentLength() >0 ? connection.getContentLength() : 0;
//                if(v>0){
//                    InputStream in = new BufferedInputStream(connection.getInputStream());
//                    bitmap[0] = BitmapFactory.decodeStream(in);
//
//                }
//
//            }catch (MalformedURLException e){
//                e.printStackTrace();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//
//            return bitmap[0];
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            imageView.setImageBitmap(bitmap);
//            //imageView.setImageResource(R.mipmap.top_view_cat);
//
//
//        }
//    }



}
