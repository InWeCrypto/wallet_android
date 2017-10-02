package capital.fbg.wallet.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.style.ImageSpan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaoji on 2017/4/16.
 */

public class PhotoUtils {

    public static File tempFile = new File(Environment.getExternalStorageDirectory(),"small.jpg");

    /**
     * 拍照获取图片
     */
    public static void takePhoto(Activity context, int PHOTO_REQUEST_TAKEPHOTO) {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            // 调用系统的拍照功能
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 指定调用相机拍照后照片的储存路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            context.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
        } else {
            ToastUtil.show("内存卡不存在");
        }
    }

    /***
     * 从相册中取图片
     */
    public static void pickPhoto(Activity activity,int PHOTO_REQUEST_GALLERY) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    // 使用系统当前日期加以调整作为照片的名称
    @SuppressLint("SimpleDateFormat")
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    /**
     *
     * @param activity
     * @param PHOTO_REQUEST_CUT
     */
    public static Uri startPhotoZoom(Activity activity,File file,int PHOTO_REQUEST_CUT) {
        String cropImageName = "capty.jpg";
        File cropFile = new File(activity.getExternalCacheDir(), cropImageName);
        //注意到此处使用的file:// uri类型.
        Uri cropUri = Uri.fromFile(cropFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri sourceUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sourceUri = getImageContentUri(activity, file);
        } else {
            sourceUri = Uri.fromFile(file);
        }
        intent.setDataAndType(sourceUri, "image/*"); //此处有问题
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        if (componentName != null) {
            activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        }
        return cropUri;
    }

    //获取文件的Content uri路径
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 保存文件

     */
    public static String getThumbnail(Activity mContext,Uri uri)throws IOException {

        InputStream input = null;
        try {
            input = mContext.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return compressBitmap(mContext,BitmapFactory.decodeStream(input),false);
    }

    /**
     * 保存文件

     */
    public static String getThumbnail(Activity mContext,String file)throws IOException {
        return compressBitmap(mContext,BitmapFactory.decodeFile(file),false);
    }

    public static ImageSpan bitmapSpan;

    public static ImageSpan getImageSpan(){
        return bitmapSpan;
    }

    /**
     * 保存文件

     */
    public static String getThumbnail(Activity mContext, Uri uri, ImageSpan imageSpan)throws IOException {

        InputStream input = null;
        try {
            input = mContext.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return compressBitmap(mContext,BitmapFactory.decodeStream(input),true);
    }


    public static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight = 1280;
        int imageWidth = 960;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    public static String compressBitmap(Activity activity,Bitmap image,boolean isSpan) {
        // 最大图片大小 100KB
        int maxSize = 100;
        return compressBitmap(activity, image, isSpan, maxSize);

    }

    @NonNull
    public static String compressBitmap(Activity activity, Bitmap image, boolean isSpan, int maxSize) {
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(), image.getHeight());
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio, image.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        String path = Environment.getExternalStorageDirectory() +"/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + "small.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(myCaptureFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isSpan){
            bitmapSpan=new ImageSpan(activity,result);
        }else {
            // 释放Bitmap
            if (result != null && !result.isRecycled()) {
                result.recycle();
                result = null;
            }
        }

        return myCaptureFile.getAbsolutePath();
    }

    // 解析获取图片库图片Uri物理路径
    @SuppressLint("NewApi")
    public static String parsePicturePath(Context context, Uri uri) {

        if (null == context || uri == null)
            return null;

        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentUri
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageDocumentsUri
            if (isExternalStorageDocumentsUri(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] splits = docId.split(":");
                String type = splits[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + File.separator + splits[1];
                }
            }
            // DownloadsDocumentsUri
            else if (isDownloadsDocumentsUri(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaDocumentsUri
            else if (isMediaDocumentsUri(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosContentUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static boolean isExternalStorageDocumentsUri(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocumentsUri(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocumentsUri(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosContentUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
