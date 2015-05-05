package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtils
{

	public final static int BMPHEADERLENGTH = 66;
	private final static int BI_BITFIELDS = 3;

	/**
	 * Bitmap → byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[] → Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b)
	{
		if (b.length != 0)
		{
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Bitmap缩放
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 获得圆角图片
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 获得带倒影的图片
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap)
	{
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * Drawable缩放
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h)
	{
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// drawable转换成bitmap
		Bitmap oldbmp = drawableToBitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		// 设置缩放比例
		matrix.postScale(sx, sy);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * bitmap->drawable
	 * 
	 * @param bm
	 * @return
	 */
	public static Drawable bitmap2drawable(Bitmap bm)
	{
		BitmapDrawable bd = new BitmapDrawable(bm);
		return bd;// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
	}

	// 保存为bmp文件
	public void saveBmp(Bitmap bitmap, String filepath)
	{
		if (bitmap == null)
			return;
		// 位图大小
		int nBmpWidth = bitmap.getWidth();
		int nBmpHeight = bitmap.getHeight();
		// 图像数据大小
		int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);
		try
		{
			File file = new File(filepath);
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileOutputStream fileos = new FileOutputStream(filepath);
			// bmp文件头
			int bfType = 0x4d42;
			long bfSize = 14 + 40 + bufferSize;
			int bfReserved1 = 0;
			int bfReserved2 = 0;
			long bfOffBits = 14 + 40;
			// 保存bmp文件头
			writeWord(fileos, bfType);
			writeDword(fileos, bfSize);
			writeWord(fileos, bfReserved1);
			writeWord(fileos, bfReserved2);
			writeDword(fileos, bfOffBits);
			// bmp信息头
			long biSize = 40L;
			long biWidth = nBmpWidth;
			long biHeight = nBmpHeight;
			int biPlanes = 1;
			int biBitCount = 24;
			long biCompression = 0L;
			long biSizeImage = 0L;
			long biXpelsPerMeter = 0L;
			long biYPelsPerMeter = 0L;
			long biClrUsed = 0L;
			long biClrImportant = 0L;
			// 保存bmp信息头
			writeDword(fileos, biSize);
			writeLong(fileos, biWidth);
			writeLong(fileos, biHeight);
			writeWord(fileos, biPlanes);
			writeWord(fileos, biBitCount);
			writeDword(fileos, biCompression);
			writeDword(fileos, biSizeImage);
			writeLong(fileos, biXpelsPerMeter);
			writeLong(fileos, biYPelsPerMeter);
			writeDword(fileos, biClrUsed);
			writeDword(fileos, biClrImportant);
			// 像素扫描
			byte bmpData[] = new byte[bufferSize];
			int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
			for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
				for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3)
				{
					int clr = bitmap.getPixel(wRow, nCol);
					bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
					bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color.green(clr);
					bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color.red(clr);
				}
			fileos.write(bmpData);
			fileos.flush();
			fileos.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void writeWord(FileOutputStream stream, int value) throws IOException
	{
		byte[] b = new byte[2];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		stream.write(b);
	}

	protected void writeDword(FileOutputStream stream, long value) throws IOException
	{
		byte[] b = new byte[4];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		b[2] = (byte) (value >> 16 & 0xff);
		b[3] = (byte) (value >> 24 & 0xff);
		stream.write(b);
	}

	protected void writeLong(FileOutputStream stream, long value) throws IOException
	{
		byte[] b = new byte[4];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) (value >> 8 & 0xff);
		b[2] = (byte) (value >> 16 & 0xff);
		b[3] = (byte) (value >> 24 & 0xff);
		stream.write(b);
	}

	/**
	 * 把bmp565转为jpg
	 * 
	 * @param bmp565
	 * @param width
	 * @param height
	 * @param filePath
	 */
	public void compress(byte[] bmp565, int width, int height, String filePath)
	{
		// get bmp header
		byte[] headbuffer = getBmpHeader565(width, height);
		System.arraycopy(headbuffer, 0, bmp565, 0, BMPHEADERLENGTH);

		try
		{
			saveBmpToJpeg(bmp565, width, height, filePath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void saveBmpToJpeg(byte[] bmp565, int width, int height, String path) throws IOException
	{
		Bitmap bitmap = BitmapFactory.decodeByteArray(bmp565, 0, width * height * 2 + BMPHEADERLENGTH);
		File file = new File(path);
		if (file.exists())
			file.delete();
		FileOutputStream outputStream = new FileOutputStream(file);
		if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream))
			outputStream.flush();
		outputStream.close();
	}

	private byte[] getBmpHeader565(int width, int height)
	{
		BMPHEADER head = new BMPHEADER();
		TagBITMAPINFO info = new TagBITMAPINFO();
		getRgb565Header(head, info, width, height);
		byte[] tmp = new byte[BMPHEADERLENGTH];

		System.arraycopy(head.bfType, 0, tmp, 0, 2);
		System.arraycopy(intToByte(head.bfSize), 0, tmp, 2, 4);
		System.arraycopy(intToByte(head.bfReserved1), 0, tmp, 6, 4);
		System.arraycopy(intToByte(head.bfOffBits), 0, tmp, 10, 4);
		System.arraycopy(intToByte(info.bmiHeader.biSize), 0, tmp, 14, 4);
		System.arraycopy(intToByte(info.bmiHeader.biWidth), 0, tmp, 18, 4);
		System.arraycopy(intToByte(info.bmiHeader.biHeight), 0, tmp, 22, 4);
		System.arraycopy(intToByte(info.bmiHeader.biPlanes), 0, tmp, 26, 1);
		System.arraycopy(intToByte(info.bmiHeader.biPlanes >> 8), 0, tmp, 27, 1);
		System.arraycopy(intToByte(info.bmiHeader.biBitCount), 0, tmp, 28, 1);
		System.arraycopy(intToByte(info.bmiHeader.biBitCount >> 8), 0, tmp, 29, 1);
		System.arraycopy(intToByte(info.bmiHeader.biCompression), 0, tmp, 30, 4);
		System.arraycopy(intToByte(info.bmiHeader.biSizeImage), 0, tmp, 34, 4);
		System.arraycopy(intToByte(info.bmiHeader.biXPelsPerMeter), 0, tmp, 38, 4);
		System.arraycopy(intToByte(info.bmiHeader.biYPelsPerMeter), 0, tmp, 42, 4);
		System.arraycopy(intToByte(info.bmiHeader.biClrUsed), 0, tmp, 46, 4);
		System.arraycopy(intToByte(info.bmiHeader.biClrImportant), 0, tmp, 50, 4);
		System.arraycopy(intToByte((info.rgb)[0]), 0, tmp, 54, 4);
		System.arraycopy(intToByte((info.rgb)[1]), 0, tmp, 58, 4);
		System.arraycopy(intToByte((info.rgb)[2]), 0, tmp, 62, 4);

		return tmp;
	}

	private int getRgb565Header(BMPHEADER head, TagBITMAPINFO info, int width, int height)
	{
		int w = width;
		int h = height;
		int size;

		size = w * h * 2;
		head.bfType[0] = 'B';
		head.bfType[1] = 'M';
		head.bfOffBits = 14 + 40;
		head.bfSize = head.bfOffBits + size;
		head.bfSize = (head.bfSize + 3) & ~3;
		size = head.bfSize - head.bfOffBits;

		info.bmiHeader = new BMPINFO();
		info.bmiHeader.biSize = 40;
		info.bmiHeader.biWidth = w;
		info.bmiHeader.biHeight = -h;
		info.bmiHeader.biPlanes = 1;
		info.bmiHeader.biBitCount = 16;
		info.bmiHeader.biCompression = BI_BITFIELDS;
		info.bmiHeader.biSizeImage = size;

		info.rgb[0] = 0xF800;
		info.rgb[1] = 0x07E0;
		info.rgb[2] = 0x001F;

		return size;
	}

	private byte[] intToByte(int src)
	{
		byte[] tmp = new byte[4];
		tmp[0] = (byte) src;
		tmp[1] = (byte) (src >> 8);
		tmp[2] = (byte) (src >> 16);
		tmp[3] = (byte) (src >> 24);
		return tmp;
	}

	class BMPHEADER
	{
		short twobyte;
		// 14B
		byte[] bfType = new byte[2];
		int bfSize;
		int bfReserved1;
		int bfOffBits;
	};

	class BMPINFO
	{
		// 40B
		int biSize;
		int biWidth;
		int biHeight;
		short biPlanes;
		short biBitCount;
		int biCompression;
		int biSizeImage;
		int biXPelsPerMeter;
		int biYPelsPerMeter;
		int biClrUsed;
		int biClrImportant;
	};

	class TagRGBQUAD
	{
		byte rgbBlue;
		byte rgbGreen;
		byte rgbRed;
		byte rgbReserved;
	};

	class TagBITMAPINFO
	{
		BMPINFO bmiHeader;
		// RGBQUAD bmiColors[1];
		int[] rgb = new int[3];
	};
}