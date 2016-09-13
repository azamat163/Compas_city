package com.example.azamat.testaltarix;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by azz on 06.09.16.
 */
public class GeoDataModel {


    private String title;
    private Double dist;
    private Integer image;

    GeoDataModel(){

    }

    public void setTitle(String title) {
     this.title = title;
   }

    public void setDist(double dist) {
       this.dist = dist;
   }

    public void setImage(Integer image) {
       this.image = image;
   }


    public String getTitle(){
        return title;
    }

    public Double getDist(){
        return dist;
    }

    public Integer getImage(){
        return image;
    }


}
