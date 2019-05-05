package com.example.animalregister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<AdopData> adoptList;
    private FavoriteDBController DBController;

    MyAdapter(Context context,ArrayList<AdopData> adoptList,FavoriteDBController DBController){
        this.context = context;
        this.adoptList = adoptList;
        this.DBController = DBController;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView kind;
        TextView update;
        TextView animalId;
        TextView color;
        TextView age;
        TextView sex;
        TextView shelter;
        TextView address;
        TextView tel;
        TextView remark;
        ImageView animailImage;
        ImageButton btnMap,btnTel,btnFavotite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemview = layoutInflater.inflate(R.layout.adop_listview,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(itemview);
        holder.kind = (TextView)itemview.findViewById(R.id.txtKind);
        holder.update = (TextView)itemview.findViewById(R.id.txtUpdate);
        holder.animalId = (TextView) itemview.findViewById(R.id.txtId);
        holder.color = (TextView)itemview.findViewById(R.id.txtColor);
        holder.age = (TextView)itemview.findViewById(R.id.txtAge);
        holder.sex = (TextView)itemview.findViewById(R.id.txtSex);
        holder.shelter = (TextView)itemview.findViewById(R.id.txtShelter);
        holder.address = (TextView)itemview.findViewById(R.id.txtAddress);
        holder.tel = (TextView)itemview.findViewById(R.id.txtTel);
        holder.remark = (TextView)itemview.findViewById(R.id.txtRemark);
        holder.animailImage = (ImageView)itemview.findViewById(R.id.animalImage);
        holder.btnMap = (ImageButton)itemview.findViewById(R.id.btnmap);
        holder.btnTel = (ImageButton)itemview.findViewById(R.id.btntel);
        holder.btnFavotite = (ImageButton)itemview.findViewById(R.id.btnFavorite);
        holder.btnMap = (ImageButton) itemview.findViewById(R.id.btnmap);
        holder.btnTel = (ImageButton) itemview.findViewById(R.id.btntel);
        holder.btnFavotite = (ImageButton) itemview.findViewById(R.id.btnFavorite);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //以ArrayList陣列位置，取得資料物件
        AdopData adopData = adoptList.get(position);
        String kind = String.valueOf(adopData.getKind());
        holder.kind.setText(kind);
        String update = String.valueOf(adopData.getUpdate());
        holder.update.setText(update);
        String animalId = String.valueOf(adopData.getAnimalId());
        holder.animalId.setText(animalId);
        String color = String.valueOf(adopData.getColor());
        holder.color.setText(color);
        //age年齡因為返回的是英文，用if判斷轉成中文
        String ageget = String.valueOf(adopData.getAge());
        if(ageget.equals("CHILD")){
            holder.age.setText("幼齡");
        }else if(ageget.equals("ADULT")){
            holder.age.setText("成年");
        }else{
            holder.age.setText("未知");
        }
        //sex性別因為返回的是英文，用if判斷轉成中文
        String sexdata = String.valueOf(adopData.getSex());
        if(sexdata.equals("M")){
            holder.sex.setText("男生");
        }else if(sexdata.equals("F")){
            holder.sex.setText("女生");
        }else {
            holder.sex.setText("未知");
        }
        //因為等等地圖還會用到，這裡另外給一個參照
        String shelterName = String.valueOf(adopData.getShelter());
        holder.shelter.setText(shelterName);
        //因為等等地圖還會用到，這裡另外給一個參照
        String shelterAddress = String.valueOf(adopData.getAddress());
        holder.address.setText(shelterAddress);
        //因為等等電話還要再用到，這裡給一個參照
        String telnumber = String.valueOf(adopData.getTel());
        holder.tel.setText(telnumber);

        String remark = String.valueOf(adopData.getRemark());
        holder.remark.setText(remark);
        //圖片先預設一張顯示尚未有圖片
        //holder.animailImage.setImageResource(R.mipmap.noimage);
        String image = adopData.getImageURL();
        //判斷是否是空字串，不是空字串就是網址
//        if(!image.equals("")){
            //getPic方法使用okHttp取得網址回應，再將回應的串流轉成Bitmap
            Glide.with(context)
                    .load(image)
                    .placeholder(R.mipmap.noimage)
                    .error(R.mipmap.noimage)
                    .into(holder.animailImage);
            //getPic(image,holder.animailImage);

            //執行緒
//            Thread th = new Thread(new GetPicThread(image,holder.animailImage));
//            th.start();


//        }
        //設定監聽，每個設定監聽內都有會返回實作事件的方法
        holder.btnMap.setOnClickListener(initMapButton(shelterName,shelterAddress));
        holder.btnTel.setOnClickListener(initTelButton(telnumber));
        //加到我的最愛使用SQLite時使用以下
        holder.btnFavotite.setOnClickListener(initFavoriteButton(adopData,position));
        //加到我的最愛使用內存存成txt時使用以下
        //holder.btnFavotite.setOnClickListener(initFavoriteButton(kind,update,animalId,color,ageget,sexdata,shelterName,shelterAddress,telnumber,remark,image));

        holder.kind.setVisibility(adopData.getKind()==null ? View.GONE : View.VISIBLE);
        holder.update.setVisibility(adopData.getUpdate()==null ? View.GONE : View.VISIBLE);
        holder.animalId.setVisibility(adopData.getAnimalId()==null ? View.GONE : View.VISIBLE);
        holder.color.setVisibility(adopData.getColor()==null ? View.GONE : View.VISIBLE);
        holder.age.setVisibility(adopData.getAge()==null ? View.GONE : View.VISIBLE);
        holder.sex.setVisibility(adopData.getSex()==null ? View.GONE : View.VISIBLE);
        holder.shelter.setVisibility(adopData.getShelter()==null ? View.GONE : View.VISIBLE);
        holder.address.setVisibility(adopData.getAddress()==null ? View.GONE : View.VISIBLE);
        holder.tel.setVisibility(adopData.getTel()==null ? View.GONE : View.VISIBLE);
        holder.remark.setVisibility(adopData.getRemark()==null ? View.GONE : View.VISIBLE);
        holder.animailImage.setVisibility(adopData.getImageURL()==null ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return adoptList.size();

    }
    //會返回地圖按鈕的事件之方法，此方法當中有呼叫以地址反查經緯度的方法，並會轉跳至google地圖畫面
    ImageButton.OnClickListener initMapButton(final String shelter,final String address){
        ImageButton.OnClickListener maplis = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                //利用自訂方法將地址解成經緯度後，以double型態傳送
                //由於Bundle不能包經緯度，直接以double傳送

                //自訂方法會return回double陣列
                double[] latlng = getLatLngFromAddress(address);

                //分別將陣列拆開
                Double latitude = latlng[0];
                Double longitude = latlng[1];

                //印出檢查(Logcat會顯示)
//                System.out.println(shelterName);
//                System.out.println("latlng:"+String.valueOf(latitude)+","+String.valueOf(longitude));

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
                bundle.putString("shelterAddress",address);
                it.putExtras(bundle);
                context.startActivity(it);

            }
        };
        return maplis;
    }
    //會返回電話按鈕的事件之方法，此方法轉跳撥電話
    ImageButton.OnClickListener initTelButton(final String tel){
        ImageButton.OnClickListener tellis = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri uri =Uri.parse("tel:"+tel);
                Intent dial = new Intent(Intent.ACTION_VIEW,uri);
//                dial.setAction("android.intent.action.call");
//                dial.setData(Uri.parse("tel:"+phoneNumber));
                //getPackageManager可接收是否有安裝或是授權的訊息
                if(dial.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(dial);
                }
            }
        };
        return tellis;
    }
    //會返回我的最愛按鈕的事件之方法，此方法有使用SQLite或是寫檔
    ImageButton.OnClickListener initFavoriteButton(final AdopData adopData, final int position){
        //使用內存時，此方法須代入以下參數:final String kind,final String update,final String animalId,final String color,final String age,final String sex,final String shelter,final String Address,final String tel,final String remark,final String image
        ImageButton.OnClickListener favoritelis = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                //以下是使用SQLite的方式，將資料加到資料庫，之後再取出SHOW在ListView上
                //checkInsertable方法為自訂，會檢查資料是否存在資料庫內
                if(DBController.checkInsertable(adopData)){
                    DBController.insert(adopData);
                    Toast.makeText(context,"增加到我的最愛",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"資料已存在，無法重複新增",Toast.LENGTH_SHORT).show();
                }

                //以下是使用內存將JSON存成txt
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("animal_kind",kind);
//                    obj.put("animal_update",update);
//                    obj.put("animal_id",animalId);
//                    obj.put("animal_colour",color);
//                    obj.put("animal_age",age);
//                    obj.put("animal_sex",sex);
//                    obj.put("shelter_name",shelter);
//                    obj.put("shelter_address",Address);
//                    obj.put("shelter_tel",tel);
//                    obj.put("animal_remark",remark);
//                    obj.put("album_file",image);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String json = obj.toString();
//                String dot = ",";
////                File file = getExtPubPicDir("favorite_list.txt");
//                File dir = context.getFilesDir();
//                //生成檔案物件，設定路徑
//                File file = new File(dir,"favorite.txt");
//                //檔案輸出流
//                FileOutputStream fos = null;
//                try {
//                    //生成檔案輸出流物件，給路徑及是否繼續編寫
//                    fos = new FileOutputStream(file,true);
//                    //寫檔
//                    fos.write(json.getBytes());
//                    fos.write(dot.getBytes());
//                    fos.flush();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    try{
//                        fos.close();
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
                //如果是用內存的方式新增，以下顯示新增成功
                //Toast.makeText(context,"增加到我的最愛",Toast.LENGTH_SHORT).show();
            }
        };
        return favoritelis;
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

    }

    class GetPicThread implements Runnable{

        String url;
        ImageView imageView;
        public GetPicThread(String url,ImageView imageView){
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        public void run() {

            //final Bitmap[] bitmap = new Bitmap[1];
            OkHttpClient client = new OkHttpClient();
            //建立連線
            final Request request = new Request.Builder()
                    .url(url)
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


        }
    }



}
