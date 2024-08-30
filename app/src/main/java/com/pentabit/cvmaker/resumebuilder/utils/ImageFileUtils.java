package com.pentabit.cvmaker.resumebuilder.utils;

import static com.pentabit.pentabitessentials.utils.EConstantsKt.FILE_NAME_PATTERN;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.pentabit.pentabitessentials.firebase.AppsKitSDK;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Keep
public class ImageFileUtils {
    private static ImageFileUtils instance = null;
    private String appDirectorySubName;
    private String appDirectoryName;
    String imagePath;
    String headerDir;

    private ImageFileUtils() {
    }

    public static ImageFileUtils getInstance() {
        if (instance == null)
            instance = new ImageFileUtils();
        return instance;
    }

    public Bitmap resizeBitmap(Bitmap originalBitmap) {
        if (originalBitmap == null || AppsKitSDK.getInstance().getContext() == null)
            return null;
        // Get the screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AppsKitSDK.getInstance().getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Calculate the aspect ratio
        float originalAspectRatio = (float) originalBitmap.getWidth() / originalBitmap.getHeight();

        // Calculate the new dimensions while maintaining the aspect ratio
        int newWidth, newHeight;
        if (originalAspectRatio > screenWidth / (float) screenHeight) {
            // If the original image is wider than the screen, scale based on width
            newWidth = screenWidth;
            newHeight = (int) (newWidth / originalAspectRatio);
        } else {
            // If the original image is taller than the screen, scale based on height
            newHeight = screenHeight;
            newWidth = (int) (newHeight * originalAspectRatio);
        }

        // Resize the bitmap
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

    public void setDirectoryName(String appDirectoryName) {
        this.appDirectoryName = appDirectoryName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean saveImageToHidden(byte[] image, boolean temp) {
        return saveImageToHiddenStorage(BitmapFactory.decodeByteArray(image, 0, image.length), temp, null, (5 * 1024 * 1024), null);
    }

    public boolean saveImageToHiddenStorage(Bitmap bitmap, boolean temp, String name, int maxSizeInBytes, String headerDir) {
        String timeStamp = name;
        this.headerDir = headerDir;

        // Set the subdirectory name based on the provided name
        if (name == null) {
            timeStamp = new SimpleDateFormat(FILE_NAME_PATTERN, Locale.US).format(Calendar.getInstance().getTime());
        } else {
            appDirectorySubName = name;
        }

        boolean exception = false;
        ContextWrapper cw = new ContextWrapper(AppsKitSDK.getInstance().getContext());
        File directory;

        // Get the base directory
        if (!temp) {
            directory = cw.getDir(headerDir, Context.MODE_PRIVATE);
        } else {
            directory = cw.getDir(headerDir, Context.MODE_PRIVATE);
        }

        // Navigate to the subdirectory (headerDir/appDirectorySubName/IMAGES)
        File targetDirectory = new File(directory, appDirectorySubName + "/IMAGES");

        // Ensure the target directory exists
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        // Compress the bitmap and save it to the target directory
        int quality = 80;
        byte[] compressedBitmapData;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        compressedBitmapData = baos.toByteArray();

        // Define the file path
        File myPath = new File(targetDirectory, timeStamp + ".png");
        try (FileOutputStream fos = new FileOutputStream(myPath)) {
            fos.write(compressedBitmapData);
        } catch (Exception e) {
            exception = true;
            e.printStackTrace();
        }

        imagePath = myPath.getAbsolutePath();
        return !exception;
    }


    public static File copyToHiddenStorage(Context context, Uri uri, boolean temp, String name) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory;
        if (!temp)
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        else
            directory = cw.getDir("temp", Context.MODE_PRIVATE);

        File destinationFile = new File(directory, name + ".png");
        ContentResolver contentResolver = context.getContentResolver();

        try (InputStream inputStream = contentResolver.openInputStream(uri); OutputStream outputStream = new FileOutputStream(destinationFile)) {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return destinationFile;
            } else return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File copyFileUsingStreams(Context context, String name, boolean temp, File sourceFile) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory;
        if (!temp)
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        else
            directory = cw.getDir("temp", Context.MODE_PRIVATE);
        File destFile = new File(directory, name + ".png");
        try (FileInputStream inputStream = new FileInputStream(sourceFile);
             FileOutputStream outputStream = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return destFile;
        } catch (Exception e) {
            return null;
        }
    }

    //    public  File copyFileUsingStreams(Context context, String name, boolean temp, File sourceFile) {
//        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
//        File directory;
//        if (!temp)
//            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        else
//            directory = cw.getDir("temp", Context.MODE_PRIVATE);
//        File destFile = new File(directory, name + ".png");
//        try (FileInputStream inputStream = new FileInputStream(sourceFile);
//             FileOutputStream outputStream = new FileOutputStream(destFile)) {
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            return destFile;
//        } catch (Exception e) {
//            return null;
//        }
//    }
    public List<String> loadImageFromHiddenStorage(String headerDir, String subheading) {

        List<String> list = new ArrayList<>();
        ContextWrapper cw = new ContextWrapper(AppsKitSDK.getInstance().getContext());

        // Get the main directory
        File mainDirectory = cw.getDir(headerDir, Context.MODE_PRIVATE);

        // Navigate to the subdirectory (appDirectorySubName/IMAGES)
        File targetDirectory = new File(mainDirectory, subheading + "/IMAGES");

        // Get the list of files in the target directory
        File[] files = targetDirectory.listFiles();
        if (files != null) {
            for (File inFile : files) {
                list.add(inFile.getAbsolutePath());
            }
        }
        return list;
    }

    public boolean saveImage(Bitmap bitmap, boolean isTemp) {
        if (isTemp) appDirectoryName = "Temp";

        String name = new SimpleDateFormat(FILE_NAME_PATTERN, Locale.US).format(new Date());
        File image;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            image = saveBitmapBelowQ(bitmap, name);
        } else {
            image = saveBitmapAboveQ(bitmap, AppsKitSDK.getInstance().getContext(), headerDir + "/" + appDirectorySubName + "/IMAGES", name);
        }
        return image != null;
    }

    public boolean saveImage(byte[] image) {
        return saveImage(BitmapFactory.decodeByteArray(image, 0, image.length), false);
    }

    public boolean saveImage(Uri uri) throws IOException {
        String name = new SimpleDateFormat(FILE_NAME_PATTERN, Locale.US).format(new Date());
        File image;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(AppsKitSDK.getInstance().getContext().getContentResolver(), uri);
            image = saveBitmapBelowQ(bitmap, name);
        } else {
            ImageDecoder.Source source = ImageDecoder.createSource(AppsKitSDK.getInstance().getContext().getContentResolver(), uri);
            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
            image = saveBitmapAboveQ(bitmap, AppsKitSDK.getInstance().getContext(), appDirectoryName, name);
        }
        return image != null;
    }

    private File saveBitmapBelowQ(Bitmap bitmap, String name) {
        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), appDirectoryName);
        if (!imageRoot.exists()) {
            imageRoot.mkdirs();
        }
        File image = new File(imageRoot, name + ".png");
        try {
            if (image.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(image)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,
                            100, bos);
                    fos.write(bos.toByteArray());
                    fos.flush();
                    imagePath = image.getPath();
                    new SingleMediaScanner(AppsKitSDK.getInstance().getContext(), image, "image/*");
                }
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public File saveBitmapAboveQ(Bitmap bitmap, Context context, String directoryName, String name) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + directoryName);
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        if (imageUri != null)
            try (OutputStream fos = resolver.openOutputStream(imageUri)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                imagePath = new File(getPath(imageUri)).getAbsolutePath();
                new SingleMediaScanner(AppsKitSDK.getInstance().getContext(), new File(getPath(imageUri)), "image/*");
                return new File(getPath(imageUri));
            } catch (Exception e) {
                return null;
            }
        else return null;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = AppsKitSDK.getInstance().getContext().getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndex);
        cursor.close();
        return s;
    }

    public void deleteDirectory(String child) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), child);
        deleteDirectory(file);
    }

    public void deleteDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory() && fileOrDirectory.listFiles() != null)
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteDirectory(child);
        fileOrDirectory.delete();
    }

    public String[] convertListToArray(List<String> lst) {
        return ((String[]) lst.toArray());
    }
}
