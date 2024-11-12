package com.example.panier.entity;

import androidx.room.TypeConverter;

import com.example.panier.entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromProductList(List<Product> products) {
        if (products == null) {
            return null;
        }
        Type type = new TypeToken<List<Product>>() {}.getType();
        return gson.toJson(products, type);
    }

    @TypeConverter
    public static List<Product> toProductList(String productsJson) {
        if (productsJson == null) {
            return null;
        }
        Type type = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(productsJson, type);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date fromTimestamp(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}
