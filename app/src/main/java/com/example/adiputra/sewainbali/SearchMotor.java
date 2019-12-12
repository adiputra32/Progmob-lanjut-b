package com.example.adiputra.sewainbali;

public class SearchMotor {
//    private int gambar;
    private String idMotor, gambar, namaMotor, jenis, harga, pemilik;

    public SearchMotor(String idMotor, String gambar, String namaMotor, String jenis, String harga, String pemilik){
        this.idMotor = idMotor;
        this.gambar = gambar;
        this.namaMotor = namaMotor;
        this.jenis = jenis;
        this.harga = harga;
        this.pemilik = pemilik;
    }

    public String getIdMotor() {
        return idMotor;
    }

    public void setIdMotor(String idMotor) {
        this.idMotor = idMotor;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
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
