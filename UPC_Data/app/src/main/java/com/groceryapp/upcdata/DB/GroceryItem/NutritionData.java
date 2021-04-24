package com.groceryapp.upcdata.DB.GroceryItem;

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

    public NutritionData(){

    }
    public NutritionData(String calories, String Totalfat, String satfat, String transfat, String cholesterol, String carbs, String sodium, String fiber, String sugars, String protein){
        this.calories = calories;
        this.totalfat = Totalfat;
        this.satfat = satfat;
        this.transfat = transfat;
        this.cholesterol = cholesterol;
        this.carbs = carbs;
        this.sodium = sodium;
        this.fiber = fiber;
        this.sugars = sugars;
        this.protein = protein;

    }

    public String getCalories() {
        return calories;
    }

    public String getCarbs() {
        return carbs;
    }
}
