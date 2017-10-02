package capital.fbg.wallet.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by Administrator on 2017/8/12.
 * 功能描述：
 * 版本：@version
 */

public class QuanCodeUtils {
    /**
     * 生成用户的二维码
     *
     * @param context 上下文
     * @param content 二维码内容
     * @return 生成的二维码图片
     * @throws WriterException 生成二维码异常
     */
    public static Bitmap createQuanCode(Context context, String content) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);   //设置白边
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,
                (int) (ScreenUtils.getScreenWidth(context)/4.0*3),
                (int) (ScreenUtils.getScreenWidth(context)/4.0*3),hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK;
                } else {
                    pixels[y * width + x] = Color.TRANSPARENT;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
