package com.example.wangluomokuai;

import android.app.Application;


public class Data extends Application{  //全局变量
  private String path="";  //图片路径


  public String getpath(){  
      return this.path;  
  }  
  public void setpath(String c){this.path= c;}

}