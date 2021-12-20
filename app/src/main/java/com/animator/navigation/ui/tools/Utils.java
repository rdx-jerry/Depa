package com.animator.navigation.ui.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidatePhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{10}")) {
            return true;
        }
        return false;
    }

    public static boolean isValidateName(String name) {
        if (name.matches("^[A-Za-z]+$")) {
            return true;
        }
        return false;
    }

    public static boolean isLoggedIn(SharedPreferences share) {
        String family_id = share.getString("family_no", "");
        return family_id != null && !family_id.equalsIgnoreCase("");
    }

    public static boolean isNUllOrEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static String getFileToByteBase64(Context context, Uri picUri) {
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            return Base64.encodeToString(bos.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getPdfFileToByte(Context context, Uri pdfUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(pdfUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int read = inputStream.read(buffer);
                int len = read;
                if (read == -1) {
                    return byteBuffer.toByteArray();
                }
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] getVideoFileToByte(Context context, Uri videoUri) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(String.valueOf(videoUri)));
            byte[] buf = new byte[1024];
            while (true) {
                int read = fis.read(buf);
                int n = read;
                if (-1 == read) {
                    return baos.toByteArray();
                }
                baos.write(buf, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] getImageFileToByte(Context context, Uri picUri) {
        byte[] bt = null;
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            String encodeString = Base64.encodeToString(bt, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bt;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, 0).show();
    }

    public static String convertDate(String date) {
        Date oneWayTripDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd-mm-yyyy");
        try {
            oneWayTripDate = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(oneWayTripDate);
    }

    public static String convertDateUrdu(String date) {
        Date oneWayTripDate = null;
        SimpleDateFormat input = new SimpleDateFormat("d-m-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-mm-dd");
        try {
            oneWayTripDate = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(oneWayTripDate);
    }
}
