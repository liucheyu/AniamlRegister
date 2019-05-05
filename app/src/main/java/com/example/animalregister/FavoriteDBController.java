package com.example.animalregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FavoriteDBController {

    private SQLiteDatabase db;
    private ArrayList<AdopData> arrayList;


    public FavoriteDBController(Context context){
        db = DBhelper.getDatabase(context);

    }

    public void close(){
        db.close();
    }

    public void insert(AdopData data){
        ContentValues cv = new ContentValues();
        cv.put("_kind",String.valueOf(data.getKind()));
        cv.put("_update",String.valueOf(data.getUpdate()));
        cv.put("_animalId",String.valueOf(data.getAnimalId()));
        cv.put("_color",String.valueOf(data.getColor()));
        cv.put("_age",String.valueOf(data.getAge()));
        cv.put("_sex",String.valueOf(data.getSex()));
        cv.put("_shelter",String.valueOf(data.getShelter()));
        cv.put("_address",String.valueOf(data.getAddress()));
        cv.put("_tel",String.valueOf(data.getTel()));
        cv.put("_remark",String.valueOf(data.getRemark()));
        cv.put("_animailImage",String.valueOf(data.getImageURL()));
        db.insert("FavoriteTable",null,cv);
    }

    public void delete(String animalId){
//        String id = "select * from FavoriteTable where _animalId= "+ animalId + ";";
//        Cursor cursor = db.rawQuery(id,null);
//        while (cursor.moveToNext()){
//            String where = cursor.getString(3);
        String where = "_animalId=" + animalId;
            //System.out.println(where);
            db.delete("FavoriteTable",where,null);
//        }


        //db.delete("FavoriteTable",where,null);

        query();
    }

    public void query(){

        arrayList = new ArrayList<>();
        String allData = "select * from FavoriteTable";
        Cursor cursor = db.rawQuery(allData,null);
        while (cursor.moveToNext()){
            String kind = cursor.getString(1);
            String updata = cursor.getString(2);
            String animalId = cursor.getString(3);
            String color = cursor.getString(4);
            String age = cursor.getString(5);
            String sex = cursor.getString(6);
            String shelter = cursor.getString(7);
            String address = cursor.getString(8);
            String tel = cursor.getString(9);
            String remark = cursor.getString(10);
            String animailImage = cursor.getString(11);
            AdopData adopData = new AdopData(kind,updata,animalId,color,age,sex,shelter,address,tel,remark,animailImage);
            arrayList.add(adopData);
        }
    }

    public ArrayList getList(){
        return arrayList;
    }

    public boolean checkInsertable(AdopData adopData){

        String animalId = adopData.getAnimalId();
        String count = "select * from FavoriteTable where _animalId=" + animalId + ";";
        Cursor cursor = db.rawQuery(count,null);

        return cursor.getCount()>=1 ? false : true;
    }


}
