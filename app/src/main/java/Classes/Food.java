package Classes;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Food {
    private Bitmap im_food;
    private String url;
    private String dishName;
    private ArrayList<String> l_ingridients;
    private ArrayAdapter<String> l_ingridientsAdapter;

    public Bitmap getIm_food() {
        return im_food;
    }

    public void setIm_food(Bitmap im_food) {
        this.im_food = im_food;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }

    public String getdishName() {
        return dishName;
    }

    public void setdishName(String dishName) {
        this.dishName = dishName;
    }

    public ArrayList<String> getl_lngridients() {
        return l_ingridients;
    }

    public void setl_ingridients(ArrayList<String> l_ingridients) {
        this.l_ingridients = l_ingridients;
    }

    public ArrayAdapter<String> getl_ingridientsAdapter() {
        return l_ingridientsAdapter;
    }

    public void setl_ingridientsAdapter(ArrayAdapter<String> l_ingridientsAdapter) {
        this.l_ingridientsAdapter = l_ingridientsAdapter;
    }

    public Food(String dishName, String url, Bitmap im_food, ArrayList<String> l_ingridients, ArrayAdapter<String> l_ingridientsAdapter){
        this.dishName = dishName;
        this.url = url;
        this. im_food = im_food;
        this.l_ingridients = l_ingridients;
        this.l_ingridientsAdapter = l_ingridientsAdapter;
    }

    public Food(String dishName, String url,ArrayList<String> l_ingridients, ArrayAdapter<String> l_ingridientsAdapter){
        this.dishName = dishName;
        this.url = url;
        this.l_ingridients = l_ingridients;
        this.l_ingridientsAdapter = l_ingridientsAdapter;
    }
}
