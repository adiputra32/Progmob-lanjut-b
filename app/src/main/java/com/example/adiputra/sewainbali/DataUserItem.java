package com.example.adiputra.sewainbali;

import com.google.gson.annotations.SerializedName;

public class DataUserItem {
    @SerializedName("id_user")
    private String id_user;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("id_card")
    private String id_card;

    @SerializedName("photo_profil")
    private String photo_profil;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getPhoto_profil() {
        return photo_profil;
    }

    public void setPhoto_profil(String photo_profil) {
        this.photo_profil = photo_profil;
    }

    @Override
    public String toString(){
        return
                "DataUserItem{" +
                        "id_user = '" + id_user + '\'' +
                        ",name = '" + name + '\'' +
                        ",email = '" + email + '\'' +
                        ",birthdate = '" + birthdate + '\'' +
                        ",address = '" + address + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",id_card = '" + id_card + '\'' +
                        ",photo_profil = '" + photo_profil + '\'' +
                        "}";
    }
}
