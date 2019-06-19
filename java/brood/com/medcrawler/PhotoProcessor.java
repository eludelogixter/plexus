package brood.com.medcrawler;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class PhotoProcessor {

    /* Saves photos locally on the app's folder */
    public static boolean savePicToLocal(String uniqueString, byte[] rawDataArray, Context context)
            throws Exception {

        String filename = uniqueString;
        FileOutputStream outputStream;

        outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(rawDataArray);
        outputStream.close();

        return true;
    }


    /* Reads photos from the local storage based on a file's name */
    public static byte[] readPicFromLocal(String filename, Context context) throws  Exception{

        String myFilePath = context.getFilesDir() + "/" + filename;
        File myFile = new File(myFilePath);
        int length = (int) myFile.length();
        byte[] tempBiteArray = new byte[length];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(myFile));
            buf.read(tempBiteArray, 0, tempBiteArray.length);
            buf.close();
        } catch (FileNotFoundException fnf) {
            //fnf.printStackTrace();
        } catch (IOException IOE) {
            //IOE.printStackTrace();
        }

        return tempBiteArray;
    }

    /* Generates composed name + unique gen-code */
    public static String photoFullName(String usrFName, String usrLname) {

        Random rnd = new Random();
        String composedName = usrFName + usrLname + String.valueOf(rnd.nextInt(2000 - 100 + 1) + 100) + ".tmp";

        return composedName;
    }

    /* Delete all ".tmp" & ".pdf" files from the app */
    public static void deleteCashedFiles(Context context) throws Exception {

        //1. Get a list with all stored files on the app's internal storage
        File tempDelFile = context.getFilesDir();

        //2. Iterate over all files and select only the ones that have a .tmp extension
        for (String nameFile : tempDelFile.list()) {

            //2.1 Check if the file's name contains .tmp
            if (nameFile.contains(".tmp") || nameFile.contains(".pdf")) {

                File delFile = new File(tempDelFile, nameFile);
                boolean delSucc = delFile.delete();

                //System.out.println("Deleted: " + nameFile + ": " +delSucc);
            }
        }
    }

    /* Delete one photo */
    public static Boolean deleteOnePhoto(Context context, String photoName) {

        //1. Get a list with all stored files on the app's internal storage
        File tempDelFile = context.getFilesDir();

        //2. Iterate over all files and select only the ones that have a .tmp extension
        for (String nameFile : tempDelFile.list()) {

            if (nameFile.equals(photoName)) {
                File delFile = new File(tempDelFile, nameFile);
                boolean delSucc = delFile.delete();

                return delSucc;
            }
        }
        return false;
    }

    /* Checks if photo exists locally */
    public static Boolean checkLocalPhoto(Context context, String photoName) throws Exception {

        //1. Get a list with all stored files on the app's internal storage
        File tempDelFile = context.getFilesDir();

        //2. Iterate over all files and select only check if photoName exists
        for (String nameFile : tempDelFile.list()) {

            //2.1 Check if the photoName exists
            if(nameFile.equals(photoName)) {

                return true;
            }
        }

        return false;
    }
}
