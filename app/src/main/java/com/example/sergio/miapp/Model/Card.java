package com.example.sergio.miapp.Model;


public class Card {
    private String title;
    private String body;
    private String img;
    private String date;

    public Card() {
    }

    public Card(String title, String body, String img, String date) {

        this.title = title;
        this.body = body;
        this.img = img;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setImageUrl(String imageUrl) {
        this.img = imageUrl;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}
