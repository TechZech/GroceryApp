package com.groceryapp.upcdata.DB.GroceryItem;

import com.facebook.stetho.common.StringUtil;
import com.google.zxing.common.StringUtils;

public class NutritionData {
    private String calories;
    private String totalfat;
    private String satfat;
    private String transfat;
    private String cholesterol;
    private String carbs;
    private String sodium;
    private String fiber;
    private String sugars;
    private String protein;
    private String calFromFat;

    public NutritionData(){

    }
    public NutritionData(String calories, String Totalfat, String satfat, String transfat, String cholesterol, String carbs, String sodium, String fiber, String sugars, String protein){
        this.calories = RoundString(calories);
        this.totalfat = RoundString(Totalfat) + "g";
        this.satfat = RoundString(satfat) + "g";
        this.transfat = RoundString(transfat) + "g";
        this.cholesterol = RoundString(cholesterol) + "mg";
        this.carbs = RoundString(carbs) + "g";
        this.sodium = RoundString(sodium) + "mg";
        this.fiber = RoundString(fiber) + "g";
        this.sugars = RoundString(sugars) + "g";
        this.protein = RoundString(protein) + "g";

        this.calFromFat = String.valueOf(Integer.parseInt(RoundString(Totalfat))*9);
    }

    public String getCalories() {
        return calories;
    }
    public String getCarbs() {
        return carbs;
    }
    public String getTotalfat(){return totalfat; }
    public String getSatfat(){return satfat;}
    public String getTransfat(){return transfat;}
    public String getCholesterol(){return cholesterol;}
    public String getSodium(){return sodium;}
    public String getFiber(){return fiber;}
    public String getSugars(){return sugars; }
    public String getProtein(){return protein;}
    public String getCalFromFat(){return calFromFat;}

    public String RoundString(String string){
        int iend = string.indexOf(".");
        return string.substring(0, iend);
    }
}
