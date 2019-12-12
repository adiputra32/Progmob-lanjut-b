package com.example.adiputra.sewainbali;

public class ChatRoom {
    private String pesan;
    private String nama;
    private String email_asal;
    private String email_tujuan;

    public ChatRoom(String nama, String pesan, String email_asal, String email_tujuan){
        this.pesan = pesan;
        this.nama = nama;
        this.email_asal = email_asal;
        this.email_tujuan = email_tujuan;
    }

    public String getEmail_asal() {
        return email_asal;
    }

    public void setEmail_asal(String email_asal) {
        this.email_asal = email_asal;
    }

    public String getEmail_tujuan() {
        return email_tujuan;
    }

    public void setEmail_tujuan(String email_tujuan) {
        this.email_tujuan = email_tujuan;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
