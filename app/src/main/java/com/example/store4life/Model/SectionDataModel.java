package com.example.store4life.Model;

import java.util.ArrayList;

/**
 * Created by duchieu on 10/10/2016.
 */

public class SectionDataModel {

    public String headerTitle;
    public ArrayList<Product> allItemsInSection;

    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, ArrayList<Product> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }




    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Product> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<Product> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
