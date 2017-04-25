package com.example.androiddevelopment.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class ImageUtil {

	/**
	 * 将任意形状的bitmap转换成圆形bitmap
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmaps(Bitmap bitmap) {
		// 圆形图片宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
									: Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	public static Bitmap getImageThumbnail(Context context, int resId) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 调用此方法得到options得到图片大小
		// 我们的目标是在800pixel的画面上显示
		// 所以需要调用computeSampleSize得到图片缩放的比例
		options.inSampleSize = 4;
		// 我们得到了缩放的比例，现在开始正式读入Bitmap数据
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.ARGB_8888;

		// 根据options参数，减少所需要的内存
		bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0) {
			return 1;
		}
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target) {
				candidate -= 1;
			}
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target) {
				candidate -= 1;
			}
		}
		return candidate;
	}
}
