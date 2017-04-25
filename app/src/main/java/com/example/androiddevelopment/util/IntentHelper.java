package com.example.androiddevelopment.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.duzun.player.activity.PersonEditActivity;

import java.io.File;

/**
 * need to be tested
 * <p/>
 * <p>IntentHelper is designed for opening native application.
 * Such as call phone, open gallery, open settings .....
 *
 * @author Jason
 */
public class IntentHelper {

    /**
     * Request code for sdk version lower than 19
     */
    public static final int PICK_PHOTO = 0;

    /**
     * Request code for sdk version higher than 19(including 19)
     */
    public static final int PICK_PHOTO_KITKAT = 1;

    /**
     * Call phone<p/>
     * Specify call phone permission<p/>
     *
     * @param context  Context
     * @param phoneNum The number you wanna dial
     */
    public static void callPhoneNum(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    /**
     * Open web browser with specify url<p/>
     *
     * @param context Context
     * @param url     The url you wanna open
     */
    public static void openWebPage(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * Pick photo from gallery<p/>
     * The content uri will be saved in the intent in onActivityResult
     *
     * @param context Context
     */
    public static void pickPhotoFromGallery(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((PersonEditActivity) context).startActivityForResult(intent, PICK_PHOTO_KITKAT);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                ((PersonEditActivity) context).startActivityForResult(intent, PICK_PHOTO);
            }
        } catch (ActivityNotFoundException e) {
            Log.i("TAG", e.toString());
            //e.printStackTrace();
        }
    }

    public static boolean takePhoto(PersonEditActivity activity, String dir, String filename, int cmd) {
        String filePath = dir + filename;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File cameraDir = new File(dir);
        if (!cameraDir.exists()) {
            return false;
        }

        File file = new File(filePath);
        Uri outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        try {
            activity.startActivityForResult(intent, cmd);
        } catch (ActivityNotFoundException e) {
            return false;
        }
        return true;
    }

}
