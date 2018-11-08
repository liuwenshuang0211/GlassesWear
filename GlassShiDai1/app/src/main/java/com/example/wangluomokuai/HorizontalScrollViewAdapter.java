package com.example.wangluomokuai;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.uilistviewtest.R;
import com.squareup.picasso.Picasso;
public class HorizontalScrollViewAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> mDatas;

    private static int number=4;
    private MyClickListener1 mListener; 

	public HorizontalScrollViewAdapter(Context context, List<String> mDatas2)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas2;

	}
	
	public void setOnClickListener(MyClickListener1 listener) {      
	     mListener = listener;   
	   }
	public int getCount()
	{
		if(mDatas.size()<4)//当眼镜数据大于4个，眼睛栏框架增加一个view
		return number;
		else {
			return mDatas.size()+1;
		}
	
	}

	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item, parent, false);
			viewHolder.mImg = (ImageView) convertView
					.findViewById(R.id.image11);
			viewHolder.mImageButton = (ImageButton) convertView
					.findViewById(R.id.imageButton11);
			viewHolder.mImageButton .setOnClickListener(mOnClickListener);
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImageButton .setTag(position);
		
		
		
		if(position<mDatas.size()){
		 Picasso.with(mContext)
	   	.load(mDatas.get(position))
		   	.resize(200,130).into(viewHolder.mImg); 
		  viewHolder.mImageButton.setVisibility(View.VISIBLE);  		  
		}	  
		if(position==mDatas.size())
			viewHolder.mImg.setImageResource(R.drawable.jiahao);
		
		return convertView;
	}

	private class ViewHolder
	{
		ImageView mImg;
		ImageButton mImageButton;
	}
	private  View.OnClickListener mOnClickListener=new View.OnClickListener() {  //适配器封装回调 
	     @Override      
	     public void onClick(View v) {    
	        if (mListener != null) {       
	         int position = (Integer) v.getTag();       
	         switch (v.getId()) {                
	                case R.id.imageButton11:                 
	                       mListener.Delete(HorizontalScrollViewAdapter.this,v,position);             
	                       break;             

	   }       }
		 }
		};  
}
