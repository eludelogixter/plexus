package brood.com.medcrawler;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AdminKeyGen extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AdminKeyGenAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button keyGenBtn;
    private Context context;
    private EditText numOfKeysEdit;
    private ProgressBar keyLoadingProgress;
    private TextView listPlaceholder;
    private Activity activity;
    private Button PDFGenBtn;
    private ImageView pdfImage;
    public final int SHARE_PDF_KEY_RESULT = 13;
    public static String PDF_FILE_NAME = "keys.pdf";
    private File tempFile = null;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 44;

    //private TalkToAdapter myInterface;
    private List<KeyRow> tmpSelList = new ArrayList<>(); // source data for the PDF generator


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_key_gen);

        // Target the context of this activity
        context = this;
        activity = this;


        // We use Toolbars instead of ActionBars as the latter was deprecated
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
            getSupportActionBar().setTitle("Key Generator"); // <---- change this to dyn. username
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Hide the keyboard as as soon as the activity is loaded
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        /******************************************************************************************/

        // Target numberOfKeysEdit
        numOfKeysEdit = (EditText) findViewById(R.id.admin_keygen_num_edit);

        // Target the key_loading_progressbar
        keyLoadingProgress = (ProgressBar) findViewById(R.id.key_loading_progressbar);

        // Target the keyList placeholder
        listPlaceholder = (TextView) findViewById(R.id.text_no_data);

        // Set user-category widget
        final AppCompatSpinner userCatList = (AppCompatSpinner) findViewById(R.id.admin_addUser_usercat_list);

        // ArrayAdapter feeder
        ArrayList<String> userCatArray = new ArrayList<>();
        userCatArray.add("Medical Student");
        userCatArray.add("Medical Resident");
        userCatArray.add("Medical Fellow");
        userCatArray.add("Medical Physician");
        userCatArray.add("Dental Student");
        userCatArray.add("Dental Resident");
        userCatArray.add("Dental Fellow");
        userCatArray.add("Dental Physician");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter_catlist = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, userCatArray);

        // Specify the layout to use when the list of choices appears
        adapter_catlist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        userCatList.setAdapter(adapter_catlist);

        /******************************************************************************************/

        // Target the KeyGen btn
        keyGenBtn = (Button) findViewById(R.id.keygen_btn);
        keyGenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Control variable
                int tempNoKey = 0;

                if (numOfKeysEdit.getText().toString().isEmpty()
                        || Integer.valueOf(numOfKeysEdit.getText().toString()) == 0) {

                    // numOhKeys value is null or 0, set the check variable to 0
                    tempNoKey = 0;
                }

                // numOfKeysEdit is not null or 0, so assign its value to the control variable
                else {

                    tempNoKey = Integer.valueOf(numOfKeysEdit.getText().toString());
                }


                // Check if the number of keys requested is null or 0
                if (tempNoKey <= 0 || numOfKeysEdit.getText().toString().isEmpty()) {

                    // Prompt the user with an error, can't be 0
                    Toast.makeText(context, "Key Num field can't be empty or 'O'", Toast.LENGTH_LONG).show();
                }

                // Number of keys is greater than 0, call genKey method
                else {

                    // Call genKey method
                    genKey(tempNoKey, numOfKeysEdit, userCatList.getSelectedItem().toString(),
                            context, keyLoadingProgress, mRecyclerView, activity);
                }

            }
        });

        // Target create .PDF button
        PDFGenBtn = (Button) findViewById(R.id.pdfgen_btn);
        PDFGenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a temp list with the selected keys to pass it into the PDF generator
                tmpSelList = mAdapter.passMyList();

                if (tmpSelList.size() > 0) {


                    // For SDK >= 6.0 ask in-app permissions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        // Check for permission to write the file on local storage
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.
                                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            // If permission is not present ... request it
                            ActivityCompat.requestPermissions(AdminKeyGen.this, new String[]{Manifest.
                                            permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {

                            // Permission has already been granted write file
                            // Generate PDF from keyrow list
                            createPDFFile(tmpSelList);
                            delKeysFromParse(tmpSelList);
                        }
                    }

                    // For SDK < 6.0 just write the file locally
                    else {

                        // Generate PDF from keyrow list
                        createPDFFile(tmpSelList);
                        delKeysFromParse(tmpSelList);
                    }

                } else {
                    Toast.makeText(context, "Select at least one key", Toast.LENGTH_LONG).show();
                }


            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.keygen_recycler_view);

        if (mRecyclerView != null) {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // Call parse readKeyGen
            readKeyGen(context, this, mRecyclerView, keyLoadingProgress);

            //mAdapter = new AdminKeyGenAdapter(getStaticKeys(), this);
            //mRecyclerView.setAdapter(mAdapter);
        }

        pdfImage = (ImageView) findViewById(R.id.pdf_icon);
        pdfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pdfImage.getAlpha() == 1.0f) {

                    // Create an email-specialized intent
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SENDTO);
                    shareIntent.setData(Uri.parse("mailto:"));
                    shareIntent.putExtra(Intent.EXTRA_EMAIL, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Auto-generated as a PDF file attachment");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Plexus registration keys");

                    if (tempFile != null) {
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                    }

                    if (shareIntent.resolveActivity(getPackageManager()) != null) {
                        AdminKeyGen.this.startActivityForResult(shareIntent, SHARE_PDF_KEY_RESULT);

                        deleteItemsFromRW(tmpSelList);
                    } else {
                        Toast.makeText(context, "Email app missing. Can't share PDF via email", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, "No key file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Catch the permission request here
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    // Generate PDF from keyrow list
                    createPDFFile(tmpSelList);
                    delKeysFromParse(tmpSelList);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "Need permission to write keys.pdf locally", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        readKeyGen(context, this, mRecyclerView, keyLoadingProgress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clean static variables
        PDF_FILE_NAME = null;
        if (tempFile != null) {
            tempFile.delete();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //todo: resultCode is always 0 (on email sent or app cancel)

        if (requestCode == AdminKeyGenAdapter.SHARE_KEY_RESULT) {
            if (resultCode == 0) {
                Log.i("RESULT_OK", "OK");
            } else {
                Log.i("RESULT_CANCELED", "CANCELED");
            }
        } else if (requestCode == SHARE_PDF_KEY_RESULT) {

            if (tempFile != null) {
                // delete the temporary pdf file created when genPDF button is tapped
                tempFile.delete();
                pdfImage.setAlpha(0.3f);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_key_gen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle_shape clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // About
        if (id == R.id.admin_key_gen_action_about) {

            // Launch an about dialog
            About about = new About();
            about.launch(context);

            return true;

        }

        else if (id == R.id.admin_key_gen_action_logout) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(AdminKeyGen.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AdminKeyGen.this.startActivity(mainIntent);
            AdminKeyGen.this.finish();

            return true;
        }

        // Return arrow
        else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Read generated keys from Parse */
    private void readKeyGen(final Context context,
                            final Activity activity,
                            final RecyclerView rw, final ProgressBar pb) {

        // Check Internet state
        if (DeviceConnection.testConnection(context)) {

            // Local variables
            //KeyRow myKeyRow; // instantiate later
            final List<KeyRow> myKeyList = new ArrayList<>();

            // Enable progressbar visibility
            pb.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            listPlaceholder.setVisibility(View.GONE);

            ParseQuery<ParseObject> readKeyQ = ParseQuery.getQuery("RegKeys");
            readKeyQ.orderByAscending("userCategory");
            readKeyQ.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    // No errors, got the key list
                    if (e == null) {

                        for (ParseObject iter : list) {
                            myKeyList.add(new KeyRow(iter.get("regKey").toString(),
                                    iter.getObjectId(), iter.get("userCategory").toString(), true));
                        }

                        // Set up the adapter and the rw
                        //int count = mAdapter.getItemCount();
                        //mAdapter.notifyItemRangeRemoved(0, count);
                        mAdapter = new AdminKeyGenAdapter(myKeyList, activity, context, listPlaceholder);
                        mAdapter.notifyDataSetChanged();

                        mRecyclerView.setAdapter(mAdapter);

                        // Set visibility to keyLoadingProgressbar to invisible & recyclerview to visible
                        pb.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        // Check if adapter is empty; if so display the placeholder
                        if (mAdapter.getItemCount() == 0) {

                            // Adapter is empty disable all other views except the placeholder
                            mRecyclerView.setVisibility(View.GONE);
                            listPlaceholder.setVisibility(View.VISIBLE);
                        } else {

                            // Set back visiblity on mRecyclerView
                            mRecyclerView.setVisibility(View.VISIBLE);
                            listPlaceholder.setVisibility(View.GONE);
                        }


                    }

                    // Parse errors, show the error
                    else {

                        // Set progress bar visiblity to invisible
                        pb.setVisibility(View.GONE);

                        // <marked>
                        if (e.getCode() == 209) {
                            ParseProcessor.createInvalidSessionD(context);
                        } else {
                            // Parse error, prompt with feedback
                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {

            /* Network Error */

            //Set dialog progress bar visibility off
            //progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
            listPlaceholder.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    /* Generate a reg-key */
    private void genKey(final Integer numOfKeys, final EditText editText, final String userCat,
                        final Context context,
                        final ProgressBar progressBar, final RecyclerView rw,
                        final Activity activity) {

        // Set UI elements for key generation
        rw.setVisibility(View.GONE);
        listPlaceholder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Test device Internet connection
        if (DeviceConnection.testConnection(context)) {

            ParseQuery<ParseObject> queryKey = ParseQuery.getQuery("RegKeys");
            queryKey.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    // Retrieve a list with all Parse saved keys
                    if (e == null) {

                        // Got all the existing keys from Parse
                        // Generate a key locally
                        if ((numOfKeys != null) || (numOfKeys == 0)) {

                            // Local key -- will be instantiated & overwritten in the loop below
                            String localkeyString;

                            // Parse key -- will be instantiated & overwritten in the 2nd loop below
                            String tempParse;

                            // Add capital letters to the userCat parameter -> "Medical Student"
                            String captValue = WordUtils.capitalizeFully(userCat);

                            // Local container for later storing to Parse
                            List<ParseObject> myLocalKeys = new ArrayList<ParseObject>();

                            // Temporal Parse object to use for the keys stored in the list
                            ParseObject tempKeyObj;

                            // Loop through generate keys for as many times as the use requested
                            for (int i = 0; i < numOfKeys; ++i) {

                                // Control key gen exception
                                try {

                                    // Local key
                                    localkeyString = new String(KeyProcessor.
                                            generateRandomString(KeyProcessor.Mode.ALPHANUMERIC));

                                    // Add newly generated key to the temp Parse obj
                                    tempKeyObj = new ParseObject("RegKeys");
                                    tempKeyObj.put("regKey", localkeyString);
                                    tempKeyObj.put("userCategory", captValue);

                                    // Add newly generated key Parse object to a list
                                    myLocalKeys.add(tempKeyObj);
                                }

                                // Display key gen exception (if present)
                                catch (Exception ex) {
                                    Log.i("Error: ", ex.toString());
                                }

                            } // end first for loop


                            // Save the list to parse in one query
                            ParseObject.saveAllInBackground(myLocalKeys, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    // Saving complete
                                    if (e == null) {

                                        // Save successful, prompt the user
                                        //Toast.makeText(context, "Key/s generated successfully",
                                        //        Toast.LENGTH_LONG).show();

                                        // Read the updated key list
                                        // Local variables
                                        KeyRow myKeyRow; // instantiate later
                                        final List<KeyRow> myKeyList = new ArrayList<>();

                                        ParseQuery<ParseObject> readKeyQ = ParseQuery.getQuery("RegKeys");
                                        readKeyQ.orderByAscending("userCategory");
                                        readKeyQ.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> list, ParseException e) {

                                                // No errors, got the key list
                                                if (e == null) {

                                                    for (ParseObject iter : list) {
                                                        myKeyList.add(new KeyRow(iter.get("regKey").
                                                                toString(),
                                                                iter.getObjectId(),
                                                                iter.get("userCategory").toString(),
                                                                true));
                                                    }

                                                    // Set up the adapter and the rw
                                                    mAdapter = new AdminKeyGenAdapter(myKeyList,
                                                            activity, context, listPlaceholder);
                                                    mAdapter.notifyDataSetChanged();
                                                    mRecyclerView.setAdapter(mAdapter);

                                                    // Set visibility to keyLoadingProgressbar to
                                                    // invisible & recyclerview to visible
                                                    progressBar.setVisibility(View.GONE);
                                                    mRecyclerView.setVisibility(View.VISIBLE);

                                                    // Reset the numOfKeys editText widget
                                                    editText.setText("");

                                                }

                                                // Parse errors, show the error
                                                else {

                                                    // <marked>
                                                    if (e.getCode() == 209) {
                                                        ParseProcessor.createInvalidSessionD(context);
                                                    } else {
                                                        // Parse error, prompt with feedback
                                                        Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        });

                                    }

                                    // Saving the key/s didn't work, show Parse error
                                    else {

                                        // Set UI elements for key generation
                                        rw.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                        // <marked>
                                        if (e.getCode() == 209) {
                                            ParseProcessor.createInvalidSessionD(context);
                                        } else {
                                            // Parse error, prompt with feedback
                                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }
                            });

                        }

                        // numOfKey parameter passed into this method is null or '0'
                        else {

                            Toast.makeText(context, "Can't generate '0' keys", Toast.LENGTH_LONG).show();

                            // Set UI elements for key generation
                            rw.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    // Can't retrieve Parse list of saved keys
                    else {

                        // Set UI elements for key generation
                        rw.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        // <marked>
                        if (e.getCode() == 209) {
                            ParseProcessor.createInvalidSessionD(context);
                        } else {
                            // Parse error, prompt with feedback
                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {

            /* Network Error */

            //Set dialog progress bar visibility off
            //progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();

        }
    }

    /* Create PDF file */
    private void createPDFFile(List<KeyRow> listForPDF) {
        // Create a PDF generator and get write the keys in a file
        AdminPDFGen pdfGen = new AdminPDFGen(listForPDF);
        tempFile = pdfGen.generateKeysPDF();

        if (tempFile != null) {

            // File is write on local storage
            pdfImage.setAlpha(1.0f);
        }
    }

    /* Delete entries from recyclerview */
    private void deleteItemsFromRW(List<KeyRow> dataFeeder) {

        for (KeyRow tempRow : dataFeeder) {
            if (dataFeeder.indexOf(tempRow) > -1) {

                // Get the index of current entry
                int position = dataFeeder.indexOf(tempRow);
                mRecyclerView.getAdapter().notifyItemRemoved(position);
            }
        }
    }

    /* Delete multiple keys in one query
     *
     * Just like with saveAll() and fetchAll()
     * deleteAll()
     *
     * */
    private void delKeysFromParse(final List<KeyRow> dataList) {

        final List<ParseObject> sharedKeysList = new ArrayList<>();
        final List<ParseObject> delKeysList = new ArrayList<>();
        if (DeviceConnection.testConnection(context)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("RegKeys");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    if (e == null) {

                        for (ParseObject tempParseObj : list) {

                            String key = tempParseObj.getString("regKey");

                            for (KeyRow row : tmpSelList) {

                                if (key.equals(row.getKey())) {

                                    // Add this ParseObject to the parseObj list
                                    ParseObject modParseObj = ParseObject.create("SharedKeys");
                                    modParseObj.put("sharedKey", key);
                                    modParseObj.put("userCategory", tempParseObj.getString("userCategory"));
                                    sharedKeysList.add(modParseObj);
                                    delKeysList.add(tempParseObj);

                                }
                            }
                        }

                        //TODO: change to async
                        ParseObject.saveAllInBackground(sharedKeysList);
                        ParseObject.deleteAllInBackground(delKeysList, new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    // Pull refreshed list from Parse
                                    readKeyGen(context, AdminKeyGen.this, mRecyclerView, keyLoadingProgress);

                                    // Set PDF icon/button to alpha 0.3 to prevent next press till
                                    // new PDF file creation
                                    // pdfImage.setAlpha(0.3f);

                                } else {

                                    // <marked>
                                    if (e.getCode() == 209) {
                                        ParseProcessor.createInvalidSessionD(context);
                                    } else {
                                        // Parse error, prompt with feedback
                                        Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

                    } else {

                        // <marked>
                        if (e.getCode() == 209) {
                            ParseProcessor.createInvalidSessionD(context);
                        } else {
                            // Parse error, prompt with feedback
                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


        } else {
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

}
