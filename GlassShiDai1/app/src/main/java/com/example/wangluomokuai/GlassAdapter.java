package com.example.wangluomokuai;

import java.util.List;











import com.example.uilistviewtest.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class GlassAdapter extends ArrayAdapter<Glass> {

	private Context mContext;
	private Boolean Del;
	 private MyClickListener mListener;//自定义监听接口 
	public GlassAdapter(Context context, int fruitItem, List<Glass> objects, boolean b) {
		super(context, fruitItem,objects);
		mContext=context;
		Del=b;
	}
	 public void setOnClickListener(MyClickListener listener) {      
	     mListener = listener;   
	   } 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Glass glass = getItem(position);//获取glass对象
		View view;
		if (convertView == null) {	 
			view = LayoutInflater.from(getContext()).inflate(R.layout.fruit_item,null);		
		} else {
			view = convertView;		
		}
		TextView text1 = (TextView) view.findViewById(R.id.text1);//名字TEXT
		ImageView imageView=(ImageView) view.findViewById(R.id.image);//背景图片
		ImageView imageView1=(ImageView) view.findViewById(R.id.image1);	//眼镜图片
		 ImageButton imageButton=(ImageButton) view.findViewById(R.id.imageBT2);//详情按钮
		ImageButton shoucangBT=(ImageButton) view.findViewById(R.id.imageButton2);//收藏按钮
	   text1.setText(glass.getName());      
		imageView.setImageResource(R.drawable.beijin);
		imageView.getLayoutParams().height =  600;
		imageView.getLayoutParams().width =550;
		//删除收藏按钮
		ImageButton delBT=(ImageButton) view.findViewById(R.id.Deletebt);
		if(Del)
		delBT.setVisibility(View.VISIBLE);

		View.OnClickListener mOnClickListener=new View.OnClickListener() {
	     @Override      
	     public void onClick(View v) {    
	        if (mListener != null) {       	        
	         switch (v.getId()) {                
	                case R.id.imageBT2:                 
	                       mListener.onPlayVideo(GlassAdapter.this,v,position); //设置监听回调            
	                       break;
				 case R.id.imageButton2:
					       mListener.onCollection(GlassAdapter.this,v,position);
					       break;
				 case R.id.Deletebt:
					       mListener.onDelCollection(GlassAdapter.this,v,position);
					      break;
	   }       
	  }
	 }
	};
	imageButton.setOnClickListener(mOnClickListener); 	//详情按钮监听事件
		shoucangBT.setOnClickListener(mOnClickListener);//收藏按钮监听事件
		delBT.setOnClickListener(mOnClickListener);//删除收藏按钮监听事件
Picasso.with(mContext).load(glass.Geturl()).resize(480, 250).into(imageView1);//设置眼镜图片

	return view;
	}
	
}
