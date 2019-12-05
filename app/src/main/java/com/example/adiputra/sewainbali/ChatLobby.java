package com.example.adiputra.sewainbali;

public class ChatLobby {
    private int gambar;
    private String nama;
    private String pesan;

    public ChatLobby(int gambar, String nama, String pesan){
        this.gambar = gambar;
        this.nama = nama;
        this.pesan = pesan;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
