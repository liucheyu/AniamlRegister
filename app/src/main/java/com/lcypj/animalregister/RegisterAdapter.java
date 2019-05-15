package com.lcypj.animalregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RegisterAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<AdaopData_register> registerList;
    private Context context;
    RegisterAdapter(Context context,ArrayList<AdaopData_register> registerList){
        this.registerList = registerList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return registerList.size();
    }

    @Override
    public Object getItem(int position) {
        return registerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = inflater.inflate(R.layout.registerlist,parent,false);
        }

        AdaopData_register data = registerList.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.txtRegisterName);
        String txtRegisterName = String.valueOf(data.getName());
        name.setText(txtRegisterName);

        TextView kind = (TextView) convertView.findViewById(R.id.txtKind);
        String txtKind = String.valueOf(data.getKind());
        kind.setText(txtKind);

        TextView update = (TextView) convertView.findViewById(R.id.txtUpdate);
        String txtUpdate = String.valueOf(data.getUpdate());
        update.setText(txtUpdate);

        TextView color = (TextView) convertView.findViewById(R.id.txtColor);
        String txtColor = String.valueOf(data.getColor());
        color.setText(txtColor);

        TextView age = (TextView) convertView.findViewById(R.id.txtAge);
        String txtAge = String.valueOf(data.getAge());
        age.setText(txtAge);

        TextView sex = (TextView) convertView.findViewById(R.id.txtSex);
        String txtSex = String.valueOf(data.getSex());
        sex.setText(txtSex);

        TextView find = (TextView) convertView.findViewById(R.id.txtFind);
        String txtFind = String.valueOf(data.getFind());
        find.setText(txtFind);

        TextView shelter = (TextView) convertView.findViewById(R.id.txtShelter);
        String txtShelter = String.valueOf(data.getShelter());
        shelter.setText(txtShelter);

        TextView contact = (TextView) convertView.findViewById(R.id.txtContact);
        String txtContact = String.valueOf(data.getContact());
        contact.setText(txtContact);

        TextView tel = (TextView) convertView.findViewById(R.id.txtTel);
        String txtTel = String.valueOf(data.getTel());
        tel.setText(txtTel);

        TextView remark = (TextView) convertView.findViewById(R.id.txtRemark);
        String txtRemark = String.valueOf(data.getRemark());
        remark.setText(txtRemark);

        ImageView animalImage = (ImageView) convertView.findViewById(R.id.register_image);
        String txtUrl = String.valueOf(data.getUrl());
        Glide.with(context)
                .load(txtUrl)
                .placeholder(R.mipmap.noimage)
                .error(R.mipmap.noimage)
                .into(animalImage);

        return convertView;
    }
}
