package com.example.adiputra.sewainbali;

public class MostSearched {
//    private int gambar;
    private String idMotor, gambarMotor, namaMotor, jenis, harga, pemilik;

    public MostSearched(String idMotor, String gambarMotor, String namaMotor, String jenis, String harga, String pemilik){
        this.idMotor = idMotor;
        this.gambarMotor = gambarMotor;
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

    public String getGambarMotor() {
        return gambarMotor;
    }

    public void setGambarMotor(String gambarMotor) {
        this.gambarMotor = gambarMotor;
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
