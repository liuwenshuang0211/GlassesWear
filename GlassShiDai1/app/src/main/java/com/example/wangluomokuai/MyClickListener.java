package com.example.wangluomokuai;

import android.view.View;
import android.widget.BaseAdapter;

public interface MyClickListener {
	 public void  onPlayVideo(BaseAdapter adapter, View view, int position);

     public void onCollection(GlassAdapter glassAdapter, View v, int position);

     public void onDelCollection(GlassAdapter glassAdapter, View v, int position);
}
