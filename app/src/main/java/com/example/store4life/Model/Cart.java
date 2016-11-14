package com.example.store4life.Model;


public class Cart {
    public int ID_GH;
    public int IDSP;
    public String TENSP;
    public String HINHSP;
    public int GIASP;
    public int SOLUONG;
    public int TONGTIEN;
    public int IDTVMUA;


    public Cart (int IDSP,String TENSP,String HINHSP,int GIASP,int SOLUONG,int TONGTIEN,int IDTVMUA){

        this.IDSP = IDSP;
        this.TENSP = TENSP;
        this.HINHSP = HINHSP;
        this.GIASP = GIASP;
        this.SOLUONG = SOLUONG;
        this.TONGTIEN = TONGTIEN;
        this.IDTVMUA = IDTVMUA;
    }
    public Cart (int IDSP,int SOLUONG){
        this.IDSP = IDSP;
        this.SOLUONG = SOLUONG;
    }
}
