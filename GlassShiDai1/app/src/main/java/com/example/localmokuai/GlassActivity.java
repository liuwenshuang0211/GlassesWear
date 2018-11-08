package com.example.localmokuai;


import java.io.IOException;

import com.example.uilistviewtest.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class GlassActivity extends Activity {//本地眼镜试戴页面activity
	private Camera camera; // 相机对象
private	SurfaceHolder sh ;
private SurfaceView sv;//SurfaceView组件，用于显示相机预览
	private String path;//保存图片路径
	private Button quedingBT,takePhoto,xiangceBT;//三个按钮，分别是确定按钮，拍照获取照片按钮，相册获取照片按钮
  private ImageView touxiangImageView,glass1,glass2,glassImageView;
  //四张图片分别是获取到的可识别图片、供选择眼镜图片1、供选择眼镜图片2、戴在人脸上的眼镜图片PNG
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置全屏显示
		setContentView(R.layout.localglass);
		/****************** 判断是否安装SD卡 *********************************/
		if (!android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "请安装SD卡！", Toast.LENGTH_SHORT).show(); // 弹出消息提示框显示提示信息
		}
		/******************************************************************/
		 sv = (SurfaceView) findViewById(R.id.surfaceView1); // 获取SurfaceView组件，用于显示相机预览
              sh = sv.getHolder();//获得句柄
		sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 设置该SurfaceHolder自己不维护缓冲
		 quedingBT = (Button) findViewById(R.id.yulanBT); // 获取“确定”按钮		
			touxiangImageView=(ImageView) findViewById(R.id.tupian4567);
			takePhoto = (Button) findViewById(R.id.takephoto); // 获取“拍照”按钮
			xiangceBT=(Button) findViewById(R.id.xiangceBT);// 获取“相册”按钮
			glass1=(ImageView) findViewById(R.id.glassimage1);
			glass2=(ImageView) findViewById(R.id.glassimage2);
			glassImageView=(ImageView) findViewById(R.id.glassimage4);
		 

        
        
        glass1.setOnClickListener(new OnClickListener() {	//点击戴上眼镜1		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				glassImageView.setImageResource(R.drawable.bb6);
				glassImageView.getLayoutParams().height = 300;
				glassImageView.getLayoutParams().width =640;		
			}
		});
        glass2.setOnClickListener(new OnClickListener() {	//点击戴上眼镜1		
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				glassImageView.setImageResource(R.drawable.jj1);
				glassImageView.getLayoutParams().height =  300;
				glassImageView.getLayoutParams().width =640;		
			}
		});
        
		quedingBT.setOnClickListener(new View.OnClickListener() {//在预览模式下，点击按钮获得照片
			@Override
			public void onClick(View v) {
				// 如果相机为非预览模式，则打开相机
				quedingBT.setVisibility(View.GONE); 
				takePhoto.setVisibility(View.VISIBLE);  	
				xiangceBT.setVisibility(View.VISIBLE);  
				if(camera!=null){
				camera.takePicture(null, null, jpeg); // 进行拍照
			}			
			}
		});
	
		xiangceBT.setOnClickListener(new OnClickListener() {//使用相册获取照片
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				touxiangImageView.setVisibility(View.VISIBLE);  	
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
			    intent.addCategory(Intent.CATEGORY_OPENABLE);
			    intent.setType("image/*");
			    intent.putExtra("return-data", true);
			    startActivityForResult(intent, 0);
			}
		});

		takePhoto.setOnClickListener(new View.OnClickListener() {//使用拍照模式获取图片
			@Override
			public void onClick(View v) {
				touxiangImageView.setVisibility(View.GONE);  	
				quedingBT.setVisibility(View.VISIBLE); 
				takePhoto.setVisibility(View.GONE);  	
				xiangceBT.setVisibility(View.GONE);  
				 int cameraCount = 0;
	                CameraInfo cameraInfo = new CameraInfo();
	                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

	                for(int i = 0; i < cameraCount; i++) {
	                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息	                 
	                           // camera.stopPreview();//停掉原来摄像头的预览
	                         //   camera.release();//释放资源
	                            camera = null;//取消原来摄像头
	                            camera = Camera.open(1);//打开当前选中的摄像头
	                            camera.setDisplayOrientation(90);
	                            try {
	                                camera.setPreviewDisplay(sh);//通过surfaceview显示取景画面
	                            } catch (IOException e) {
	                                // TODO Auto-generated catch block
	                                e.printStackTrace();
	                            }
	                            camera.startPreview();//开始预览
	                            break;
	                        }               
			}
		});
	}
	
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {//相册事件回调
	  System.out.println(resultCode);
	  super.onActivityResult(requestCode, resultCode, data);
      Uri uri = data.getData();
      int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		Log.d("sdkVersion:", String.valueOf(sdkVersion));
		Log.d("KITKAT:", String.valueOf(Build.VERSION_CODES.KITKAT));
		if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
			  path = uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
            System.out.println("path:" + path);
            // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
            path = getPath_above19(GlassActivity.this, uri);
            System.out.println("path_above19:" + path);
            if(path != null && ( path.endsWith(".png") || path.endsWith(".PNG") ||path.endsWith(".jpg") ||path.endsWith(".JPG") ))
            {
            	Bitmap bm = BitmapFactory.decodeFile(path);
            	touxiangImageView.setImageBitmap(bm);
            }else{
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
            }
		} else {
		    path = getFilePath_below19(uri);
		      if(path != null && ( path.endsWith(".png") || path.endsWith(".PNG") ||path.endsWith(".jpg") ||path.endsWith(".JPG") ))
            {
		    	   	Bitmap bm = BitmapFactory.decodeFile(path);
	            	touxiangImageView.setImageBitmap(bm);
            }else{
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
            }
		}
	 }
	
	
	//实现拍照的回调接口
	final PictureCallback jpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// 根据拍照所得的数据创建位图
			Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);
		     Matrix m = new Matrix();
		     m.setRotate(90,(float) bm0.getWidth() / 2, (float) bm0.getHeight() / 2);
		     final Bitmap bm = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), m, true);
			// 加载layout/save.xml文件对应的布局资源	
			// 获取对话框上的ImageView组件
			camera.stopPreview();		//停止预览
	}		
	};
			

	@Override
	protected void onPause() {
		if(camera!=null){
			camera.stopPreview();	//停止预览
			camera.release();	//释放资源
		}
		super.onPause();
	}
	    /**
	     * API19以下获取图片路径的方法
	     * @param uri
	     */
	    private String getFilePath_below19(Uri uri) {
	        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
	        String[] proj = {MediaStore.Images.Media.DATA};
	        //好像是android多媒体数据库的封装接口，具体的看Android文档
	        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
	        //获得用户选择的图片的索引值
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        System.out.println("***************" + column_index);
	        //将光标移至开头 ，这个很重要，不小心很容易引起越界
	        cursor.moveToFirst();
	        //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
	        String path = cursor.getString(column_index);
	        System.out.println("path:" + path);
	        return path;
	    }


	    /**
	     * APIlevel 19以上才有
	     * 创建项目时，我们设置了最低版本API Level，比如我的是10，
	     * 因此，AS检查我调用的API后，发现版本号不能向低版本兼容，
	     * 比如我用的“DocumentsContract.isDocumentUri(context, uri)”是Level 19 以上才有的，
	     * 自然超过了10，所以提示错误。
	     * 添加    @TargetApi(Build.VERSION_CODES.KITKAT)即可。
	     *
	     * @param context
	     * @param uri
	     * @return
	     */
	   
	    @TargetApi(Build.VERSION_CODES.KITKAT) @SuppressLint("NewApi") 
	    public  static String getPath_above19(final Context context, final Uri uri) {
	        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	        // DocumentProvider
	        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	            // ExternalStorageProvider
	            if (isExternalStorageDocument(uri)) {
	                final String docId = DocumentsContract.getDocumentId(uri);
	                final String[] split = docId.split(":");
	                final String type = split[0];
	                if ("primary".equalsIgnoreCase(type)) {
	                    return Environment.getExternalStorageDirectory() + "/" + split[1];
	                }
	                // TODO handle non-primary volumes
	            }
	            // DownloadsProvider
	            else if (isDownloadsDocument(uri)) {
	                final String id = DocumentsContract.getDocumentId(uri);
	                final Uri contentUri = ContentUris.withAppendedId(
	                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
	                return getDataColumn(context, contentUri, null, null);
	            }
	            // MediaProvider
	            else if (isMediaDocument(uri)) {
	                final String docId = DocumentsContract.getDocumentId(uri);
	                final String[] split = docId.split(":");
	                final String type = split[0];
	                Uri contentUri = null;
	                if ("image".equals(type)) {
	                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	                } else if ("video".equals(type)) {
	                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	                } else if ("audio".equals(type)) {
	                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	                }
	                final String selection = "_id=?";
	                final String[] selectionArgs = new String[]{
	                        split[1]
	                };
	                return getDataColumn(context, contentUri, selection, selectionArgs);
	            }
	        }
	        // MediaStore (and general)
	        else if ("content".equalsIgnoreCase(uri.getScheme())) {
	            // Return the remote address
	            if (isGooglePhotosUri(uri))
	                return uri.getLastPathSegment();
	            return getDataColumn(context, uri, null, null);
	        }
	        // File
	        else if ("file".equalsIgnoreCase(uri.getScheme())) {
	            return uri.getPath();
	        }
	        return null;
	    }

	    /**
	     * Get the value of the data column for this Uri. This is useful for
	     * MediaStore Uris, and other file-based ContentProviders.
	     *
	     * @param context       The context.
	     * @param uri           The Uri to query.
	     * @param selection     (Optional) Filter used in the query.
	     * @param selectionArgs (Optional) Selection arguments used in the query.
	     * @return The value of the _data column, which is typically a file path.
	     */
	    public static String getDataColumn(Context context, Uri uri, String selection,
	                                       String[] selectionArgs) {
	        Cursor cursor = null;
	        final String column = "_data";
	        final String[] projection = {column};
	        try {
	            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                    null);
	            if (cursor != null && cursor.moveToFirst()) {
	                final int index = cursor.getColumnIndexOrThrow(column);
	                return cursor.getString(index);
	            }
	        } finally {
	            if (cursor != null)
	                cursor.close();
	        }
	        return null;
	    }

	    /**
	     * @param uri The Uri to check.
	     * @return Whether the Uri authority is ExternalStorageProvider.
	     */
	    public static boolean isExternalStorageDocument(Uri uri) {
	        return "com.android.externalstorage.documents".equals(uri.getAuthority());
	    }

	    /**
	     * @param uri The Uri to check.
	     * @return Whether the Uri authority is DownloadsProvider.
	     */
	    public static boolean isDownloadsDocument(Uri uri) {
	        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	    }

	    /**
	     * @param uri The Uri to check.
	     * @return Whether the Uri authority is MediaProvider.
	     */
	    public static boolean isMediaDocument(Uri uri) {
	        return "com.android.providers.media.documents".equals(uri.getAuthority());
	    }

	    /**
	     * @param uri The Uri to check.
	     * @return Whether the Uri authority is Google Photos.
	     */
	    public static boolean isGooglePhotosUri(Uri uri) {
	        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	    }


}