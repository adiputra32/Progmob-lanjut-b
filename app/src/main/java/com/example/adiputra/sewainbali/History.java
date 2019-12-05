package com.example.adiputra.sewainbali;

public class History {
    private int gambar;
    private String nama;
    private String pemilik;
    private String jenis;
    private String status;

    public History(int gambar, String nama, String pemilik, String jenis, String status){
        this.gambar = gambar;
        this.nama = nama;
        this.pemilik = pemilik;
        this.jenis = jenis;
        this.status = status;
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

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
