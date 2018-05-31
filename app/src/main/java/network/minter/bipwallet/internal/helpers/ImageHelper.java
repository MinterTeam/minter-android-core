package network.minter.bipwallet.internal.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Px;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Base64;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.common.annotations.Dp;
import network.minter.bipwallet.internal.helpers.data.Vec2;
import timber.log.Timber;

/**
 * Dogsy. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public final class ImageHelper {
    private Context mContext;
    private DisplayHelper mDisplay;

    public ImageHelper(Context context, DisplayHelper displayHelper) {
        mContext = context;
        mDisplay = displayHelper;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getBase64FromBitmap(Bitmap imagePath) {
        return getBase64FromBitmap(imagePath, 100);
    }

    public static String getBase64FromBitmap(Bitmap imagePath, int maxSize) {
        final Bitmap resizedBitmap = resizeBitmap(imagePath, maxSize);

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteStream);
        byte[] data = byteStream.toByteArray();

        imagePath.recycle();
        resizedBitmap.recycle();

        return Base64.encodeToString(data, Base64.DEFAULT);
    }

	public Bitmap drawableToBitmap(@DrawableRes int drawableRes) {
    	return drawableToBitmap(mContext.getResources().getDrawable(drawableRes));
	}

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap makeBitmapCircle(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private static Bitmap resizeBitmap(Bitmap source, int maxSize) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        int resultWidth;
        int resultHeight;

        if (sourceWidth > sourceHeight) {
            resultWidth = maxSize;
            resultHeight = (sourceHeight * maxSize) / sourceWidth;
        } else {
            resultHeight = maxSize;
            resultWidth = (sourceWidth * maxSize) / sourceHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, resultWidth, resultHeight, false);
        return resizedBitmap;
    }

    public String getBase64FromUri(Context context, Uri imagePath) throws IOException {
        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imagePath);
        final Bitmap resizedBitmap = resizeBitmap(bitmap, 100);

        /*
        int size = resizedBitmap.getRowBytes() * resizedBitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        resizedBitmap.copyPixelsToBuffer(byteBuffer);
        byte[] payload = byteBuffer.array();
        */

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byte[] data = byteStream.toByteArray();

        bitmap.recycle();
        resizedBitmap.recycle();

        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public String encodeTobase64(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
            Timber.e("Out of memory error catched");
        }
        return temp;
    }

    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);

        Boolean isSDPresent = Environment
                .getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED);

        File sdCardDirectory;
        if (isSDPresent) {
            // yes SD-card is present
            sdCardDirectory = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "IMG");

            if (!sdCardDirectory.exists()) {
                if (!sdCardDirectory.mkdirs()) {
                    Log.d("MySnaps", "failed to create directory");

                }
            }
        } else {
            // Sorry
            sdCardDirectory = new File(mContext.getCacheDir(), "");
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Wallet.LC_EN)
                .format(new Date());

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((1000 - 0) + 1) + 0;

        String nw = "IMG_" + timeStamp + randomNum + ".txt";
        File image = new File(sdCardDirectory, nw);


        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {


            outStream = new FileOutputStream(image);
            outStream.write(input.getBytes());

            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i("Compress bitmap path", image.getPath());
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
//            @SuppressLint("ResourceType") InputStream is = mContext.getResources().openRawResource(R.drawable.img_default_avatar);
//            bitmap = BitmapFactory.decodeStream(is);
            bitmap = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;//BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        //return decodeFile(image);
    }

    public Drawable vectorDrawable(@DrawableRes int resId) {
        return VectorDrawableCompat.create(mContext.getResources(), resId, mContext.getTheme());
    }

    public RequestCreator load(Uri uri) {
        return Picasso.with(mContext).load(uri);
    }

    public RequestCreator load(String url) {
        return Picasso.with(mContext).load(url);
    }

    public RequestCreator loadFit(Uri uri) {
        return Picasso.with(mContext).load(uri)
                .fit()
                .centerInside();
    }

    public RequestCreator loadFit(String url) {
        return Picasso.with(mContext).load(url)
                .fit()
                .centerInside();
    }

    public RequestCreator loadFit(@DrawableRes int drawableRes) {
        return Picasso.with(mContext)
                .load(drawableRes)
                .fit()
                .centerInside();
    }

    public RequestCreator loadResize(Uri imageUrl, @Dp float resizeDp) {
        final Vec2 widthHeight = mDisplay.getWidthAndHeightWithRatio(100.f, resizeDp, resizeDp);

        return Picasso.with(mContext)
                .load(imageUrl)
                .resize(widthHeight.getWidth(), widthHeight.getHeight())
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(@DrawableRes int imageId, @Dp float resizeDp) {
        final Vec2 widthHeight = mDisplay.getWidthAndHeightWithRatio(100.f, resizeDp, resizeDp);

        return Picasso.with(mContext)
                .load(imageId)
                .resize(widthHeight.getWidth(), widthHeight.getHeight())
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(@DrawableRes int imageId, @Px int resizePx) {
        return Picasso.with(mContext)
                .load(imageId)
                .resize(resizePx, resizePx)
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResizeRes(Uri imageUrl, @DimenRes int resId) {
        float pxs = mDisplay.getDimen(resId);
        final Vec2 widthHeight = mDisplay.getWidthAndHeightWithRatio(100.f, pxs, pxs);
        return Picasso.with(mContext)
                .load(imageUrl)
                .resize(widthHeight.getWidth(), widthHeight.getHeight())
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(Uri imageUrl, @Px int resizePx) {
        return Picasso.with(mContext)
                .load(imageUrl)
                .resize(resizePx, resizePx)
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(Uri imageUrl, @Dp float widthDp, @Dp float heightDp) {
        final Vec2 widthHeight = mDisplay.getWidthAndHeightWithRatio(100.f, widthDp, heightDp);

        return Picasso.with(mContext)
                .load(imageUrl)
                .resize(widthHeight.getWidth(), widthHeight.getHeight())
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(@DrawableRes int imageId, @Dp float widthDp, @Dp float heightDp) {
        final Vec2 widthHeight = mDisplay.getWidthAndHeightWithRatio(100.f, widthDp, heightDp);

        return Picasso.with(mContext)
                .load(imageId)
                .resize(widthHeight.getWidth(), widthHeight.getHeight())
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(Uri imageUrl, @Px int widthPx, @Px int heightPx) {
        return Picasso.with(mContext)
                .load(imageUrl)
                .resize(widthPx, heightPx)
                .onlyScaleDown()
                .centerCrop();
    }

    public RequestCreator loadResize(@DrawableRes int imageId, @Px int widthPx, @Px int heightPx) {
        return Picasso.with(mContext)
                .load(imageId)
                .resize(widthPx, heightPx)
                .onlyScaleDown()
                .centerCrop();
    }

    public String compressImage(String imageUri) {

        String filePath = imageUri;//getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public Bitmap decodeFile(File f) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale >= REQUIRED_SIZE && o.outHeight / scale >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);


            Boolean isSDPresent = Environment
                    .getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED);
            File sdCardDirectory;
            if (isSDPresent) {
                // yes SD-card is present
                sdCardDirectory = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "IMG");

                if (!sdCardDirectory.exists()) {
                    if (!sdCardDirectory.mkdirs()) {
                        Log.d("MySnaps", "failed to create directory");

                    }
                }
            } else {
                // Sorry
                sdCardDirectory = new File(mContext.getCacheDir(), "");
            }


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Wallet.LC_EN)
                    .format(new Date());

            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt((1000 - 0) + 1) + 0;

            String nw = "IMG_" + timeStamp + randomNum + ".png";
            File image = new File(sdCardDirectory, nw);


            FileOutputStream out = null;
            try {
                out = new FileOutputStream(image);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String pathNew = compressImage(image.getAbsolutePath());
            Uri uri = Uri.parse(pathNew);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return bitmap;
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private String getFilename() {
        /*File file = new File(Environment.getExternalStorageDirectory().getPath(), "IMG/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        */


        Boolean isSDPresent = Environment
                .getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED);

        File sdCardDirectory;
        if (isSDPresent) {
            // yes SD-card is present
            sdCardDirectory = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(),
                    "IMG/Images");

            if (!sdCardDirectory.exists()) {
                if (!sdCardDirectory.mkdirs()) {
                    Log.d("MySnaps", "failed to create directory");

                }
            }
        } else {
            // Sorry
            sdCardDirectory = new File(mContext.getCacheDir(), "");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Wallet.LC_EN)
                .format(new Date());

        Random rand = new Random();

// nextInt is normally exclusive of the top value,
// so add 1 to make it inclusive
        int randomNum = rand.nextInt((1000 - 0) + 1) + 0;

        String nw = "img_" + timeStamp + randomNum + ".jpg";
        File image = new File(sdCardDirectory, nw);

        String uriSting1 = (sdCardDirectory.getAbsolutePath() + "/" + nw);//System.currentTimeMillis() + ".jpg");

        return uriSting1;

    }
}
