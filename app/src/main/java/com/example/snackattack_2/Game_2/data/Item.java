package com.example.snackattack_2.Game_2.data;

import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.snackattack_2.Game_2.utilities.TypeItem;
import com.example.snackattack_2.Game_2.utilities.TypeVisibility;

public class Item {

    private AppCompatImageView image;
    private TypeVisibility typeVisibility;
    private TypeItem typeItem;
    private int imageCoin;


    public Item() {}


    public TypeItem getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(TypeItem typeItem) {
        this.typeItem = typeItem;
    }


    public ImageView getImage() {
        return image;
    }

    public TypeVisibility getTypeVisibility() {
        return typeVisibility;
    }

    public Item setImageBroccoli(AppCompatImageView image) {
        this.image = image;
        return this;
    }

    public Item setImageChocolate(int imageCoin){
        this.imageCoin = imageCoin;
        return this;
    }

    public Item setTypeVisibility(TypeVisibility typeVisibility) {
        this.typeVisibility = typeVisibility;
        return this;
    }
}
