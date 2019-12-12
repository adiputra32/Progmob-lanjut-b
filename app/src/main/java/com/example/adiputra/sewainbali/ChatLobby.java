package com.example.adiputra.sewainbali;

public class ChatLobby {
    private String email;
    private String gambar;
    private String nama;

    public ChatLobby(String email, String gambar, String nama){
        this.email = email;
        this.gambar = gambar;
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
