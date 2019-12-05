package com.example.adiputra.sewainbali;

public class SearchMotor {
    private int gambar;
    private String namaMotor, jenis, harga, pemilik;

    public SearchMotor(int gambar, String namaMotor, String jenis, String harga, String pemilik){
        this.gambar = gambar;
        this.namaMotor = namaMotor;
        this.jenis = jenis;
        this.harga = harga;
        this.pemilik = pemilik;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }

    public String getNamaMotor() {
        return namaMotor;
    }

    public void setNamaMotor(String namaMotor) {
        this.namaMotor = namaMotor;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }
}
