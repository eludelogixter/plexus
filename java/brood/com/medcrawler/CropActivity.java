package brood.com.medcrawler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

public class CropActivity extends AppCompatActivity {

    Toolbar toolbar;
    CropImageView myCropImgView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        context = this;

        // We use Toolbars instead of ActionBars as the latter was deprecated
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
            getSupportActionBar().setTitle("Crop Selection");

            myCropImgView = (CropImageView)findViewById(R.id.cropImageView);

            Uri uri = Uri.parse(getIntent().getStringExtra("imageURI"));
            try {
                Bitmap tempBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                myCropImgView.setImageBitmap(tempBmp);
                //userPhotoSW = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.crop_action) {

            Bitmap bitmap = myCropImgView.getCroppedImage();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] array = stream.toByteArray();

            try {
                PhotoProcessor.savePicToLocal("crop.tmp", array, context);

                File file = new File(context.getFilesDir(), "crop.tmp");
                Uri tempUri = Uri.fromFile(file);

                Intent blaPassIntent = new Intent(context, UserEditProfile.class);
                blaPassIntent.setData(tempUri);
                CropActivity.this.startActivityForResult(blaPassIntent, 33);
                CropActivity.this.finish();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return true;
    }
}
