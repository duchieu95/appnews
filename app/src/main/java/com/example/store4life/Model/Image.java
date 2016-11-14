package com.example.store4life.Model;



public class Image {
    public int ID;
    public int ID_SP;
    public String Hinh;
    public String TieuDe;
    public Image(int ID,String TieuDe,String Hinh){
        this.ID=ID;
        this.TieuDe = TieuDe;
        this.Hinh = Hinh;
    }
    public Image(int ID,int ID_SP,String Hinh){
        this.ID=ID;
        this.ID_SP = ID_SP;
        this.Hinh = Hinh;
    }
}
