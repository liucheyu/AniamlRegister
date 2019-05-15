package com.lcypj.animalregister;

public class UrlFilter {



        String kind = "";
        String update = "";
        String region = "";
        String age = "";
        String sex = "";
        String url = "";
        int count = 0;
        String plusUrl = new String(url);

        UrlFilter(String url){
            this.url = new String(url);
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setUrl(String url){
            this.url = url;
        }

        public String filter(int i){

            //kind=0,update=1,region=2,age=3,sex=4;


            switch(i){
                case 0:

                    url = url + kind;
                    url  = plusUrl.trim();

                    break;
                case 1:
                    url = url + update;
                    url = url.trim();
                    break;
                case 2:
                    url = url + region;
                    url = url.trim();
                    break;
                case 3:
                    url = url + age;
                    url = url.trim();
                    break;
                case 4:
                    url = url + sex;
                    url = url.trim();
                    break;


            }




            return url;

        }






}
