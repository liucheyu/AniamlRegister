package com.lcypj.animalregister;


public class AdopData  {

    private String kind,update,color,age,sex,shelter,address,tel,remark;

    private String imageURL;

    private String animalId;

//    private Bitmap image;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

//    public Bitmap getImage() {
//        return image;
//    }
//
//    public void setImage(Bitmap image) {
//        this.image = image;
//    }

    public AdopData(String kind, String update, String animalId, String color, String age, String sex, String shelter, String address, String tel, String remark, String imageURL) {
        this.kind = kind;
        this.update = update;
        this.animalId = animalId;
        this.color = color;
        this.age = age;
        this.sex = sex;
        this.shelter = shelter;
        this.address = address;
        this.tel = tel;
        this.remark = remark;
        this.imageURL = imageURL;
        //this.image = image;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
