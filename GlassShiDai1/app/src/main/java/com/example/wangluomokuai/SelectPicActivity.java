package com.example.wangluomokuai;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.uilistviewtest.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

//获取头像方式界面
@SuppressLint("NewApi") public class SelectPicActivity extends Activity implements OnClickListener{

/***
* 使用照相机拍照获取图片
*/
public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
/***
* 使用相册中的图片
*/
public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
/***
* 从Intent获取图片路径的KEY
*/
public static final String KEY_PHOTO_PATH = "photo_path";
Uri bitmapUri = null;
private Button takePhotoBtn,pickPhotoBtn;

/**获取到的图片路径*/
private File tempfile;
private String path;
private String positionString;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.tupian);
Intent intent1 = getIntent();
positionString=intent1.getStringExtra("position");
initView();
}
/**
* 初始化加载View
*/
private void initView() {
takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
takePhotoBtn.setOnClickListener(this);
pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
pickPhotoBtn.setOnClickListener(this);
}

@Override
public void onClick(View v) {
switch (v.getId()) {

case R.id.btn_take_photo:
takePhoto();
break;
case R.id.btn_pick_photo:
	selectImage();
break;
default:
finish();
break;
}
}

/**
* 拍照获取图片
*/
private void takePhoto() {
//执行拍照前，应该先判断SD卡是否存在
String SDState = Environment.getExternalStorageState();
if(SDState.equals(Environment.MEDIA_MOUNTED))
{

//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
/***
* 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
* 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
* 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
*/
//ContentValues values = new ContentValues(); 
//photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
//intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
///**-----------------*/
//startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
ImageCapture();
}else{
Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
}
}

private void ImageCapture() {
	// TODO Auto-generated method stub
	File DatalDir = Environment.getExternalStorageDirectory();
	File myDir = new File(DatalDir, "/DCIM/Camera");
	myDir.mkdirs();
	String mDirectoryname = DatalDir.toString() + "/DCIM/Camera";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hhmmss",
	Locale.SIMPLIFIED_CHINESE);
	tempfile = new File(mDirectoryname, sdf.format(new Date())
	+ ".jpg");
	if (tempfile.isFile())
	tempfile.delete();
	Uri Imagefile = Uri.fromFile(tempfile);
	        
	Intent cameraIntent = new Intent(
	android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	   cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Imagefile);
	   startActivityForResult(cameraIntent, SELECT_PIC_BY_TACK_PHOTO);
}
/***
* 从相册中取图片
*/

private void selectImage() {
	Intent intent = new Intent();
	// Set an explicit MIME data type.
	intent.setType("image/*");
	// Set the general action to be performed.
	intent.setAction(Intent.ACTION_GET_CONTENT);
	// callBack
	startActivityForResult(intent,SELECT_PIC_BY_PICK_PHOTO );
	
	 
	 }
	 
@Override
protected void onActivityResult(int requestCode,
                                int resultCode, 
                                Intent data) {
	 final Data app = (Data)getApplication(); 
	 app.setpath("");
        if (resultCode == RESULT_OK) {
        	if(requestCode==SELECT_PIC_BY_TACK_PHOTO)
        	{ 
       
          Intent intent = new Intent(SelectPicActivity.this,ShiDaiActivity.class);
		  intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);          		 
		  intent.putExtra("one", tempfile.getPath() );
		  intent.putExtra("position",positionString );
	         startActivity(intent);
      finish();
             
        	}
        	else if(requestCode==SELECT_PIC_BY_PICK_PHOTO)
        	{
            Uri uri = data.getData();
            int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
			Log.d("sdkVersion:", String.valueOf(sdkVersion));
			Log.d("KITKAT:", String.valueOf(Build.VERSION_CODES.KITKAT));
			if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
				  path = uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
                  System.out.println("path:" + path);
                  // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
                  path = getPath_above19(SelectPicActivity.this, uri);
                  System.out.println("path_above19:" + path);
                  if(path != null && ( path.endsWith(".png") || path.endsWith(".PNG") ||path.endsWith(".jpg") ||path.endsWith(".JPG") ))
                  {
                	  Intent intent = new Intent(SelectPicActivity.this,ShiDaiActivity.class);
            		  intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);          		 
            		  intent.putExtra("one", path);
            		  intent.putExtra("position",positionString );
            	         startActivity(intent);
                  finish();
                  }else{
                  Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
                  }
			} else {
			    path = getFilePath_below19(uri);
			      if(path != null && ( path.endsWith(".png") || path.endsWith(".PNG") ||path.endsWith(".jpg") ||path.endsWith(".JPG") ))
                  {
			    	  Intent intent = new Intent(SelectPicActivity.this,ShiDaiActivity.class);
            		  intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			    	  intent.putExtra("one", path);
			    	  intent.putExtra("position",positionString );
         	         startActivity(intent);
                  finish();
                  }else{
                  Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
                  }
			}
			
        	}
			
        }
        super.onActivityResult(requestCode, resultCode, data);
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
   
    @TargetApi(Build.VERSION_CODES.KITKAT) @SuppressLint("NewApi") public  static String getPath_above19(final Context context, final Uri uri) {
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