package com.example.store4life.Model;



public class History {
    public  int ID_HD;
    public String TENSP;
    public int TONGTIEN;
    public String NGAYTAO_HD;
    public int SOLUONG;
    public int TRANGTHAI;
    public int ID_TV;

    public History( int ID_HD,String NGAYTAO_HD,int TONGTIEN,int ID_TV,int TRANGTHAI){
        this.ID_HD = ID_HD;
        this.NGAYTAO_HD = NGAYTAO_HD;
        this.TONGTIEN = TONGTIEN;
        this.NGAYTAO_HD = NGAYTAO_HD;
        this.TONGTIEN = TONGTIEN;
        this.ID_TV = ID_TV;
        this.TRANGTHAI = TRANGTHAI;
    }

}
