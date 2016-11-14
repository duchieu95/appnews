package com.example.store4life.Model;

/**
 * Created by duchieu on 10/10/2016.
 */

public class Product {

    public int ID_SP;
    public String TenSP;
    public int GiaSP;
    public String HinhSP;
    public String MASP;
    public int IDKHUYENMAI;
    public String MOTA;
    public int ID_LOAI;
    public String ID_YT;
    public int SOLUONG;
    public String NGAY;
    public Product(int ID_SP,String TenSP,String HinhSP,int GiaSP,int ID_LOAI){
        this.ID_SP= ID_SP;
        this.TenSP = TenSP;
        this.GiaSP = GiaSP;
        this.HinhSP = HinhSP;
        this.ID_LOAI = ID_LOAI;
    }

    public Product(int ID_SP,String MASP,String TenSP,String HinhSP,int GiaSP,String MOTA,int ID_LOAI){
        this.ID_SP= ID_SP;
        this.MASP = MASP;
        this.TenSP = TenSP;
        this.HinhSP = HinhSP;
        this.GiaSP = GiaSP;
        this.MOTA = MOTA;
        this.ID_LOAI = ID_LOAI;
    }

    public Product(String ID_YT,int ID_SP,String TenSP,String HinhSP,int GiaSP){
        this.ID_YT = ID_YT;
        this.ID_SP= ID_SP;
        this.TenSP = TenSP;
        this.HinhSP = HinhSP;
        this.GiaSP = GiaSP;
    }

    public Product(int ID_SP,String TenSP,String HinhSP,int GiaSP,int SOLUONG,String NGAY){
        this.ID_SP= ID_SP;
        this.TenSP = TenSP;
        this.HinhSP = HinhSP;
        this.GiaSP = GiaSP;
        this.SOLUONG = SOLUONG;
        this.NGAY = NGAY;
    }
}
