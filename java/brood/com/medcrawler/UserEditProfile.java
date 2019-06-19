package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class UserEditProfile extends AppCompatActivity {

    private Context context;
    private AppCompatEditText emailField;
    private AppCompatEditText firstNameField;
    private AppCompatEditText lastNameField;
    private AppCompatEditText addressField;
    private AppCompatEditText cityField;
    private AppCompatEditText zipField;
    private android.support.v7.widget.CardView cboxCard;
    private AppCompatCheckBox shareData;
    public  Button selSpecWidget;
    private AppCompatCheckBox clerkshipCheckbox;
    private AppCompatCheckBox residencyCheckbox;
    private AppCompatCheckBox fellowshipCheckbox;
    private AppCompatCheckBox jobCheckbox;
    private AppCompatCheckBox offerClerkshipCheckbox;
    private AppCompatCheckBox offerResidencyCheckbox;
    private AppCompatCheckBox offerFellowshipCheckbox;
    private AppCompatCheckBox offerJobCheckbox;
    private AppCompatCheckBox needFinHelpCheckbox;
    private List<RelativeLayout> activeChboxLaylist;
    private List<RelativeLayout> userCatChckBoxesList;
    private String specName;
    public static Button selSubspecWidget;
    private String specId;
    private static String MULTI_STATE = "Multiple";
    private static String SELECT_TXT = "select";

    private StringProcessor inputProcessor;
    private List<RegFeedback> listFeedback;
    private Toolbar toolbar;
    private UsrRegScrollV customScroll;
    private RelativeLayout layNeedClerkship;
    private RelativeLayout layNeedResidency;
    private RelativeLayout layNeedFellowship;
    private RelativeLayout layNeedJob;
    private LinearLayout layNeedFinHelp;
    private RelativeLayout layOfferClerkship;
    private RelativeLayout layOfferResidency;
    private RelativeLayout layOfferFellowship;
    private RelativeLayout layOfferJob;
    private RelativeLayout layOfferAssist;
    private LinearLayout layDateClerkship;
    private LinearLayout layDateResidency;
    private LinearLayout layDateFellowship;
    private LinearLayout layDateJob;
    private LinearLayout layDateOffClerkship;
    private LinearLayout layDateOffResidency;
    private LinearLayout layDateOffFellowship;
    private LinearLayout layDateOffJob;
    private LinearLayout userCatLay;
    private ProgressBar emailCheckPBar;
    private ProgressBar dataVerPBar;
    private ProgressBar delSpecProgressB;
    private AlertDialog dMenuSelSpec;
    private AlertDialog dMenuSelSubspec;
    private AlertDialog dMenuVerData;
    private AlertDialog dSOI;
    private AlertDialog dPassReset;
    private AlertDialog dDelAccount;
    private SpecAdapter specAdapter;
    private SubspecAdapter subspecAdapter;
    private RecyclerView specRecyclerView;
    private RecyclerView subSpecRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static String CALLER = "edit_prof";
    private int errorCheckSum = 10;
    private String tempFeedback;
    private AppCompatButton okBtn;
    private TextView userSuccessTxt;
    private TextView userCatTextView;
    private TextView dataVerTitle;
    private MenuItem regBtn;
    private Toast toast;
    private AppCompatSpinner userCatSpinner;
    private Button sorWidget;
    private Button resetPassBtn;
    private Button soiWidget;
    private AppCompatButton cancelBtn;
    private AppCompatSpinner clerkshipDate;
    private AppCompatSpinner residencyDate;
    private AppCompatSpinner fellowshipDate;
    private AppCompatSpinner jobDate;
    private AppCompatSpinner offClkDate;
    private AppCompatSpinner offResDate;
    private AppCompatSpinner offFellowDate;
    private AppCompatSpinner offJobDate;
    private AppCompatCheckBox assistIntervCheckbox;
    private LinearLayout instLayout;
    private AppCompatEditText instField;
    private ArrayList<String> passStates;
    private ArrayList<Integer> passPos;
    private ArrayList<String> popSOIArray;
    private ArrayList<String> prof_passStates;
    private LinearLayout soiLayout;
    private UserObject popUsrObj;
    private Bitmap userPicBmpSel;
    private ImageView userPhotoImg;
    private CardView photoUsrCV;
    private int PICK_IMAGE = 83;
    private Uri bmpUri;
    private Button accountDelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        // Set context
        context = this;

        // Instantiate popSOIArray
        popSOIArray = new ArrayList<>();

        Intent getDataIntent = getIntent();

        if (getDataIntent.hasExtra("editMyProfile")) {

            popUsrObj = (UserObject) getDataIntent.getSerializableExtra("editMyProfile");
            specId = popUsrObj.getUserSpecId();
        }

        // Target custom user scroll class & .xml element
        customScroll = (UsrRegScrollV) findViewById(R.id.user_reg_scroll);

        /************************ HACK **************************/
        customScroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        customScroll.setFocusable(true);
        customScroll.setFocusableInTouchMode(true);
        customScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        /************************ END HACK **********************/
        customScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

            }
        });

        // We use Toolbars instead of ActionBars as the latter was deprecated
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
            getSupportActionBar().setTitle("Edit My Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        // Hide the keyboard as as soon as the activity is loaded
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Instantialize the toast
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

        // Instantialize the active/visible checkboxs list
        activeChboxLaylist = new ArrayList<>();
        userCatChckBoxesList = new ArrayList<>();

        // Target the checkbox card
        cboxCard = (android.support.v7.widget.CardView) findViewById(R.id.checkbox_card);

        // Instantiate passStates & passPos arrays (pass data from adapter to broadcasterRCV)
        passStates = new ArrayList<>();
        passPos = new ArrayList<>();

        // Instantiate prof_passState & prof_passPos
        prof_passStates = new ArrayList<>();

        // Instantiate the user cat spinner's textview
        userCatTextView = (TextView) findViewById(R.id.user_category_textview);

        // Target emailCheckPBar
        emailCheckPBar = (ProgressBar) findViewById(R.id.email_check_pbar);
        emailCheckPBar.setVisibility(View.GONE);

        // Target email edit-text
        emailField = (AppCompatEditText) findViewById(R.id.admin_addUser_email_edit);

        //Populate email with email value from popUsrObj
        emailField.setText(popUsrObj.getEmail());

        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // When the user leaves the email field
                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.EMAIL,
                            emailField.getText().toString());
                    if (!listFeedback.isEmpty()) {
                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        emailField.setError(tempFeedback);

                    }

                    // If there is no error call Parse for email validation
                    if (emailField.getError() == null) {

                        // Take the txt value from the emailField
                        //String tempJunction = emailField.getText().toString();

                        // Call Parse's email validation
                        //rtValidateEmail(tempJunction, context, emailCheckPBar);
                    }
                }
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetPassBtn = (Button) findViewById(R.id.admin_reset_pass_btn);
        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If local email-field has the same value as the registered one - on Parse -
                // feed the registered email to the dialog method to send the link to
                // since the user hasn't updated the email address yet (locally).
                if (emailField.getText().toString().equals(popUsrObj.getEmail())) {

                    createPassResetD(popUsrObj.getEmail());
                } else {

                    // Check local email string for validity
                    StringProcessor strProc = new StringProcessor();
                    List<RegFeedback> tempErrorFeedback;

                    tempErrorFeedback = strProc.processInput(StringProcessor.Mode.EMAIL, emailField.getText().toString());

                    if (tempErrorFeedback.isEmpty()) {

                        createPassResetD(emailField.getText().toString());
                    } else {
                        Toast.makeText(context, "Invalid Email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        photoUsrCV = (CardView) findViewById(R.id.usr_prof_photo_cv);
        photoUsrCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                // Delete current local photo
                try {
                    PhotoProcessor.deleteOnePhoto(context, popUsrObj.getProfilePicture());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // User Photo
        userPhotoImg = (ImageView) findViewById(R.id.usr_prof_usrimg);
        try {



            // Set user photo
            // Read photo file from local once and reuse for bmp conversion
            byte[] tempRawData = PhotoProcessor.readPicFromLocal(popUsrObj.getProfilePicture(), context);
            if (tempRawData != null && tempRawData.length > 0) {
                userPicBmpSel = BitmapFactory.decodeByteArray(tempRawData, 0, tempRawData.length);
                userPhotoImg.setImageBitmap(userPicBmpSel);
            } else {

                // Error, prob. no picture, set a default one
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_photo);
                userPhotoImg.setImageBitmap(bm);
            }


        } catch (Exception je) {
            je.printStackTrace();
        }

        firstNameField = (AppCompatEditText)
                findViewById(R.id.admin_addUser_firstname_edit);

        //Populate firstName with the firstName value from popUsrObj
        firstNameField.setText(popUsrObj.getFirstName());

        firstNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.FIRSTNAME,
                            firstNameField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        firstNameField.setError(tempFeedback);
                    }

                }

            }
        });
        firstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputProcessor = new StringProcessor();
                listFeedback = inputProcessor.processInput(StringProcessor.Mode.FIRSTNAME,
                        firstNameField.getText().toString());

                // There are errors, so read them
                if (!listFeedback.isEmpty()) {

                    tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                    firstNameField.setError(tempFeedback);
                } else {
                    firstNameField.setError(null);
                }
            }
        });

        lastNameField = (AppCompatEditText)
                findViewById(R.id.admin_addUser_lastname_edit);

        //Populate lastName with the lastName value from popUsrObj
        lastNameField.setText(popUsrObj.getLastName());

        lastNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.LASTNAME,
                            lastNameField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        lastNameField.setError(tempFeedback);
                    }
                }
            }
        });
        lastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputProcessor = new StringProcessor();
                listFeedback = inputProcessor.processInput(StringProcessor.Mode.LASTNAME,
                        lastNameField.getText().toString());

                // There are errors, so read them
                if (!listFeedback.isEmpty()) {

                    tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                    lastNameField.setError(tempFeedback);
                } else {
                    lastNameField.setError(null);
                }
            }
        });

        addressField = (AppCompatEditText)
                findViewById(R.id.admin_addUser_address_edit);

        //Populate address with the address value from popUsrObj
        addressField.setText(popUsrObj.getAddress());

        addressField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.ADDRESS,
                            addressField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        addressField.setError(tempFeedback);
                    }
                }

            }
        });
        addressField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cityField = (AppCompatEditText)
                findViewById(R.id.admin_addUser_city_edit);

        //Populate city with the city value from popUsrObj
        cityField.setText(popUsrObj.getCity());

        cityField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.CITY,
                            cityField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        cityField.setError(tempFeedback);
                    }

                    //Log.i("crap", "shit");
                }
            }
        });
        cityField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                inputProcessor = new StringProcessor();
                listFeedback = inputProcessor.processInput(StringProcessor.Mode.CITY,
                        cityField.getText().toString());

                // There are errors, so read them
                if (!listFeedback.isEmpty()) {

                    tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                    cityField.setError(tempFeedback);
                } else {
                    cityField.setError(null);
                }
            }
        });

        zipField = (AppCompatEditText)
                findViewById(R.id.admin_addUser_zip_edit);

        //Populate zip with the zip value from popUsrObj
        zipField.setText(popUsrObj.getZip());

        zipField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        zipField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Layout Institution Name && Institution field
        instLayout = (LinearLayout) findViewById(R.id.admin_add_institution_layout);
        instField = (AppCompatEditText) findViewById(R.id.admin_addUser_institution_edit);

        // Show instLayout if user is a physician
        if (popUsrObj.getUserProff().toLowerCase().contains("physician")) {
            instLayout.setVisibility(View.VISIBLE);
            instField.setText(popUsrObj.getPhysicianInst());

        }

        instField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.INST,
                            instField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = listFeedback.get(0).getFeedbackText();
                        instField.setError(tempFeedback);
                    }

                }
            }
        });

        instField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                inputProcessor = new StringProcessor();
                listFeedback = inputProcessor.processInput(StringProcessor.Mode.INST,
                        instField.getText().toString());

                // There are errors, so read them
                if (!listFeedback.isEmpty()) {

                    tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                    instField.setError(tempFeedback);
                } else {
                    instField.setError(null);
                }
            }
        });

        // Target User Category layout
        userCatLay = (LinearLayout) findViewById(R.id.user_cat_layout);

        if (popUsrObj.getUserProff().toLowerCase().contains("physician")) {
            userCatLay.setVisibility(View.GONE);
        }


        // Target SubspecWidget (it's still a button, but it's meant for text display)
        selSpecWidget = (Button) findViewById(R.id.del_sub_spec_widget);
        selSpecWidget.setText(popUsrObj.getUserSpec());
        selSpecWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                // Check the value of the User Category widget
//                if (userCatSpinner.getSelectedItem().toString().contains("auto-populated")) {
//                    if (!(toast.getView().isShown())) {
//                        toast = Toast.makeText(context, "'User Category' is missing",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                } else {
//
//                }
                createSelSpecDg(selSpecWidget);


            }
        });

        // Target the select_sub_spec_widget
        selSubspecWidget = (Button) findViewById(R.id.sel_sub_spec_widget);
        selSubspecWidget.setText(popUsrObj.getUserSubspec());

        selSubspecWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check the current value (text) of the spec widget
                if (!(selSpecWidget.getText().toString().contains(SELECT_TXT))) {

                    // Create a subspec only when a spec has been chosen
                    createSelSubspecDg();
                } else {

                    if (!(toast.getView().isShown())) {
                        toast = Toast.makeText(context, "'User Specialty' is missing",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
        /******************************************************************************************/

        // Set state-of-residency
        sorWidget = (Button) findViewById(R.id.admin_addUser_sor_w);

        //Populate SORWidged with the SOR value from popUsrObj
        sorWidget.setText(popUsrObj.getState());

        sorWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createSORDg();
            }
        });

        /******************************************************************************************/

        // Set user-category widget
        ArrayList<String> spinnerArray = new ArrayList<>();

        // User is med
        if (popUsrObj.getUserProff().toString().toLowerCase().contains("medical")) {

            // Physician (SOI layout is hidden)
            // -------------------------------

            // Non-physician
            if (!popUsrObj.getUserProff().toString().toLowerCase().contains("physician")) {

                // Med Student
                if (popUsrObj.getUserProff().toString().toLowerCase().contains("student")) {

                    spinnerArray.add("Medical Student");
                    spinnerArray.add("Medical Resident");
                    spinnerArray.add("Medical Fellow");
                }

                // Med Resident
                else if (popUsrObj.getUserProff().toString().toLowerCase().contains("resident")) {

                    spinnerArray.add("Medical Resident");
                    spinnerArray.add("Medical Fellow");

                }

                // Med Fellow
                else if (popUsrObj.getUserProff().toString().toLowerCase().contains("fellow")) {

                    spinnerArray.add("Medical Fellow");
                }
            }

        }

        // User is dental
        if (popUsrObj.getUserProff().toString().toLowerCase().contains("dental")) {

            // Physician (SOI layout is hidden)
            // _______________________________

            // Non-physician
            if (popUsrObj.getUserProff().toString().toLowerCase().contains("student")) {

                spinnerArray.add("Dental Student");
                spinnerArray.add("Dental Resident");
            } else if (popUsrObj.getUserProff().toString().toLowerCase().contains("resident")) {

                spinnerArray.add("Dental Resident");
            }
        }


        userCatSpinner = (AppCompatSpinner) findViewById(R.id.admin_addUser_usercat_widget);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, spinnerArray);
        userCatSpinner.setAdapter(spinnerAdapter);


        /******************************************************************************************/

        // Set state-of-interest widget && layout
        soiLayout = (LinearLayout) findViewById(R.id.soi_layout);
        soiWidget = (Button) findViewById(R.id.admin_addUser_soi_w);

        // Show soiLayout if user is NOT a physician
        if (!popUsrObj.getUserProff().toLowerCase().contains("physician")) {

            soiLayout.setVisibility(View.VISIBLE);

            // Collect the chosen SOI from the ParseUser
            if (popUsrObj.getSoiAlabama()) {
                popSOIArray.add("Alabama");
            }
            if (popUsrObj.getSoiFlorida()) {
                popSOIArray.add("Florida");
            }
            if (popUsrObj.getSoiGeorgia()) {
                popSOIArray.add("Georgia");
            }
            if (popUsrObj.getSoiKentucky()) {
                popSOIArray.add("Kentucky");
            }
            if (popUsrObj.getSoiMissouri()) {
                popSOIArray.add("Missouri");
            }
            if (popUsrObj.getSoiNc()) {
                popSOIArray.add("North Carolina");
            }
            if (popUsrObj.getSoiSc()) {
                popSOIArray.add("South Carolina");
            }
            if (popUsrObj.getSoiTennessee()) {
                popSOIArray.add("Tennessee");
            }

            // Populate the SOI widget if there is only one value in the array
            if ((popSOIArray.size() != 0) && (popSOIArray.size() < 2)) {

                soiWidget.setText(popSOIArray.get(0));
            } else if (popSOIArray.size() > 1) {
                soiWidget.setText("Multiple");
            }
        }

        soiWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSOIDg();
            }
        });

        /******************************************************************************************/

        // Create an ArrayAdapter using the auto-population method
        ArrayAdapter<CharSequence> adapter_startingdate = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_list_item_1, populateDateWidget());

        /******************************************************************************************/

        // Target layouts of the 'Other' card
        layNeedClerkship = (RelativeLayout) findViewById(R.id.layout_clerkship_south);
        layDateClerkship = (LinearLayout) findViewById(R.id.layout_stardate_clerkship);

        layNeedResidency = (RelativeLayout) findViewById(R.id.layout_res_south);
        layDateResidency = (LinearLayout) findViewById(R.id.layout_stardate_residency);

        layNeedFellowship = (RelativeLayout) findViewById(R.id.layout_fellowship_south);
        layDateFellowship = (LinearLayout) findViewById(R.id.layout_stardate_fellowship);

        layNeedJob = (RelativeLayout) findViewById(R.id.layout_job_south);
        layDateJob = (LinearLayout) findViewById(R.id.layout_stardate_job);

        layOfferClerkship = (RelativeLayout) findViewById(R.id.layout_haveclerkship_pos);
        layDateOffClerkship = (LinearLayout) findViewById(R.id.layout_stardate_offer_clerkship);

        layOfferResidency = (RelativeLayout) findViewById(R.id.layout_haveres_pos);
        layDateOffResidency = (LinearLayout) findViewById(R.id.layout_stardate_offer_residency);

        layOfferFellowship = (RelativeLayout) findViewById(R.id.layout_havefellow_pos);
        layDateOffFellowship = (LinearLayout) findViewById(R.id.layout_stardate_offer_fellowship);

        layOfferJob = (RelativeLayout) findViewById(R.id.layout_havejob_pos);
        layDateOffJob = (LinearLayout) findViewById(R.id.layout_stardate_offer_job);

        layOfferAssist = (RelativeLayout) findViewById(R.id.layout_assist_in_inter);

        layNeedFinHelp = (LinearLayout) findViewById(R.id.layout_checkbox_financial);

        /*************************************** Checkboxes ***************************************/
        // Target clerkship checkbox
        clerkshipCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_clerckship_ckbox);
        clerkshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateClerkship.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layNeedClerkship)) {
                        activeChboxLaylist.add(layNeedClerkship);
                    }
                } else {
                    // Hide the date layout
                    layDateClerkship.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layNeedClerkship)) {
                        int temp_index = activeChboxLaylist.indexOf(layNeedClerkship);
                        activeChboxLaylist.remove(temp_index);
                    }

                }
            }
        });

        clerkshipDate = (AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_clerkship);
        clerkshipDate.setAdapter(adapter_startingdate);

        // Target residency checkbox
        residencyCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_residency_ckbox);
        residencyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateResidency.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layNeedResidency)) {
                        activeChboxLaylist.add(layNeedResidency);
                    }
                } else {

                    // Hide the date layout
                    layDateResidency.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layNeedResidency)) {
                        int temp_index = activeChboxLaylist.indexOf(layNeedResidency);
                        activeChboxLaylist.remove(temp_index);
                    }

                }
            }
        });

        residencyDate = (AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_residency);
        residencyDate.setAdapter(adapter_startingdate);

        // Target fellowship checkbox
        fellowshipCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_fellow_ckbox);
        fellowshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateFellowship.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layNeedFellowship)) {
                        activeChboxLaylist.add(layNeedFellowship);
                    }
                } else {
                    // Hide the date layout
                    layDateFellowship.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layNeedFellowship)) {
                        int temp_index = activeChboxLaylist.indexOf(layNeedFellowship);
                        activeChboxLaylist.remove(temp_index);
                    }
                }
            }
        });


        fellowshipDate = (AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_fellowship);
        fellowshipDate.setAdapter(adapter_startingdate);

        // Target job checkbox
        jobCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_job_ckbox);
        jobCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateJob.setVisibility(View.VISIBLE);

                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layNeedJob)) {
                        activeChboxLaylist.add(layNeedJob);
                    }
                } else {
                    // Hide the date layout
                    layDateJob.setVisibility(View.GONE);


                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layNeedJob)) {
                        int temp_index = activeChboxLaylist.indexOf(layNeedJob);
                        activeChboxLaylist.remove(temp_index);
                    }
                }
            }
        });

        jobDate = (AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_job);
        jobDate.setAdapter(adapter_startingdate);

        // Target need financial help checkbox
        needFinHelpCheckbox = (AppCompatCheckBox) findViewById
                (R.id.need_financial_ckbox);
        needFinHelpCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });


        // Target offer clerkship checkbox
        offerClerkshipCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_hostckship_ckbox);
        offerClerkshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateOffClerkship.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layOfferClerkship)) {
                        activeChboxLaylist.add(layOfferClerkship);
                    }
                } else {
                    // Hide the date layout
                    layDateOffClerkship.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layOfferClerkship)) {
                        int temp_index = activeChboxLaylist.indexOf(layOfferClerkship);
                        activeChboxLaylist.remove(temp_index);
                    }
                }
            }
        });

        offClkDate = (AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_offer_clerkship);
        offClkDate.setAdapter(adapter_startingdate);

        // Target offer fellowship checkbox
        offerFellowshipCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_havefellow_ckbox);
        offerFellowshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateOffFellowship.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layOfferFellowship)) {
                        activeChboxLaylist.add(layOfferFellowship);
                    }
                } else {
                    // Hide the data layout
                    layDateOffFellowship.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layOfferFellowship)) {
                        int temp_index = activeChboxLaylist.indexOf(layOfferFellowship);
                        activeChboxLaylist.remove(temp_index);
                    }
                }
            }
        });

        offFellowDate = (AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_offer_fellowship);
        offFellowDate.setAdapter(adapter_startingdate);

        // Target offer residency checkbox
        offerResidencyCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_haveres_ckbox);
        offerResidencyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // Display the date layout
                    layDateOffResidency.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layOfferResidency)) {
                        activeChboxLaylist.add(layOfferResidency);
                    }
                } else {
                    // Hide the date layout
                    layDateOffResidency.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layOfferResidency)) {
                        int temp_index = activeChboxLaylist.indexOf(layOfferResidency);
                        activeChboxLaylist.remove(temp_index);
                    }
                }
            }
        });

        offResDate = (AppCompatSpinner) findViewById(R.id.admin_addUser_date_offer_residency);
        offResDate.setAdapter(adapter_startingdate);

        // Target offerJobCheckbox
        offerJobCheckbox = (AppCompatCheckBox) findViewById(R.id.admin_addUser_havejob_ckbox);
        offerJobCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    // Display the date layout
                    layDateOffJob.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(userCatChckBoxesList);

                    // Add this layout to the active-list
                    if (!activeChboxLaylist.contains(layOfferJob)) {
                        activeChboxLaylist.add(layOfferJob);
                    }
                } else {
                    // Hide the data layout
                    layDateOffJob.setVisibility(View.GONE);

                    // Delete this layout from the active-list
                    if (activeChboxLaylist.contains(layOfferJob)) {
                        int temp_index = activeChboxLaylist.indexOf(layOfferJob);
                        activeChboxLaylist.remove(temp_index);
                    }
                }
            }
        });

        offJobDate = (AppCompatSpinner) findViewById(R.id.admin_addUser_date_offer_job);
        offJobDate.setAdapter(adapter_startingdate);

        // Target assist interviewing checkbox
        assistIntervCheckbox = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_assist_residency_ckbox);

        // Target shareData checkbox
        shareData = (AppCompatCheckBox) findViewById
                (R.id.admin_addUser_assist_sharedata_ckbox);
        shareData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {

                    // Change the UI of the share-data checkbox
                    shareData.setTextColor(Color.RED);
                } else {
                    //shareDataTxt = (TextView) findViewById(R.id.share_data_txtview);
                    shareData.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        /******************************************************************************************/

        /*********** Check if currently logged-in user is a physician or a non-physician **********/
        if (popUsrObj.getUserProff().toLowerCase().contains("physician")) {

            // >> User is a physician

            if (popUsrObj.getUserProff().toLowerCase().contains("medical")) {

                // >> User is a medical physician
                layOfferClerkship.setVisibility(View.VISIBLE);
                layOfferResidency.setVisibility(View.VISIBLE);
                layOfferFellowship.setVisibility(View.VISIBLE);
                layOfferJob.setVisibility(View.VISIBLE);
                layOfferAssist.setVisibility(View.VISIBLE);

                // User-cat default checkboxes
                userCatChckBoxesList.clear();
                userCatChckBoxesList.add(layOfferClerkship);
                userCatChckBoxesList.add(layOfferResidency);
                userCatChckBoxesList.add(layOfferFellowship);
                userCatChckBoxesList.add(layOfferJob);

                // Modify checkboxes to reflect popUsrObj 'Other' selections
                if (popUsrObj.getOfferClerkship()) {
                    offerClerkshipCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layOfferClerkship)) {
                        activeChboxLaylist.add(layOfferClerkship);
                    }

                    layDateOffClerkship.setVisibility(View.VISIBLE);
                    int tempOffClerkPos = adapter_startingdate.getPosition(popUsrObj.getDateOfferClerkship());
                    offClkDate.setSelection(tempOffClerkPos);
                }
                if (popUsrObj.getOfferResidency()) {
                    offerResidencyCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layOfferResidency)) {
                        activeChboxLaylist.add(layOfferResidency);
                    }

                    layDateOffResidency.setVisibility(View.VISIBLE);
                    int tempOffResPos = adapter_startingdate.getPosition(popUsrObj.getDateOfferResidency());
                    offResDate.setSelection(tempOffResPos);
                }
                if (popUsrObj.getOfferFellowship()) {
                    offerFellowshipCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layOfferFellowship)) {
                        activeChboxLaylist.add(layOfferFellowship);
                    }

                    layDateOffFellowship.setVisibility(View.VISIBLE);
                    int tempOffFellPos = adapter_startingdate.getPosition(popUsrObj.getDateOfferFellowship());
                    fellowshipDate.setSelection(tempOffFellPos);
                }
                if (popUsrObj.getOfferJob()) {
                    offerJobCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layOfferJob)) {
                        activeChboxLaylist.add(layOfferJob);
                    }

                    layDateOffJob.setVisibility(View.VISIBLE);
                    int tempOffJobPos = adapter_startingdate.getPosition(popUsrObj.getDateOfferJob());
                    jobDate.setSelection(tempOffJobPos);
                }
                if (popUsrObj.getAssistInterv()) {
                    assistIntervCheckbox.setChecked(true);
                }

            } else if (popUsrObj.getUserProff().toLowerCase().contains("dental")) {

                // >> User is a dental physician
                layOfferResidency.setVisibility(View.VISIBLE);
                layOfferJob.setVisibility(View.VISIBLE);
                layOfferAssist.setVisibility(View.VISIBLE);

                // User-cat default checkboxes
                userCatChckBoxesList.clear();
                userCatChckBoxesList.add(layOfferResidency);
                userCatChckBoxesList.add(layOfferJob);

                if (popUsrObj.getOfferResidency()) {
                    offerResidencyCheckbox.setChecked(true);

                    if (!activeChboxLaylist.contains(layOfferResidency)) {
                        activeChboxLaylist.add(layOfferResidency);
                    }

                    layDateOffResidency.setVisibility(View.VISIBLE);
                    int tempOffResPos = adapter_startingdate.getPosition(popUsrObj.getDateOfferResidency());
                    offResDate.setSelection(tempOffResPos);
                }
                if (popUsrObj.getOfferJob()) {
                    offerJobCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layOfferJob)) {
                        activeChboxLaylist.add(layOfferJob);
                    }

                    layDateOffJob.setVisibility(View.VISIBLE);
                    int tempOffJobPos = adapter_startingdate.getPosition(popUsrObj.getDateOfferJob());
                    jobDate.setSelection(tempOffJobPos);
                }
                if (popUsrObj.getAssistInterv()) {
                    assistIntervCheckbox.setChecked(true);
                }
            }

        } else {

            // >> User is a non-physician

            if (popUsrObj.getUserProff().toLowerCase().contains("medical")) {

                // >> User is a medical non-physician
                layNeedClerkship.setVisibility(View.VISIBLE);
                layNeedResidency.setVisibility(View.VISIBLE);
                layNeedFellowship.setVisibility(View.VISIBLE);
                layNeedJob.setVisibility(View.VISIBLE);
                layNeedFinHelp.setVisibility(View.VISIBLE);

                // User-cat default checkboxes
                userCatChckBoxesList.clear();
                userCatChckBoxesList.add(layNeedClerkship);
                userCatChckBoxesList.add(layNeedResidency);
                userCatChckBoxesList.add(layNeedFellowship);
                userCatChckBoxesList.add(layNeedJob);

                // Modify checkboxes to reflect popUsrObj 'Other' selections
                if (popUsrObj.getNeedClerkship()) {
                    clerkshipCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layNeedClerkship)) {
                        activeChboxLaylist.add(layNeedClerkship);
                    }

                    layDateClerkship.setVisibility(View.VISIBLE);
                    int tempClerkPos = adapter_startingdate.getPosition(popUsrObj.getDateNeedClerkship());
                    clerkshipDate.setSelection(tempClerkPos);
                }
                if (popUsrObj.getNeedResidency()) {
                    residencyCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layNeedResidency)) {
                        activeChboxLaylist.add(layNeedResidency);
                    }

                    layDateResidency.setVisibility(View.VISIBLE);
                    int tempResPos = adapter_startingdate.getPosition(popUsrObj.getDateNeedResidency());
                    residencyDate.setSelection(tempResPos);
                }
                if (popUsrObj.getNeedFellowship()) {
                    fellowshipCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layNeedFellowship)) {
                        activeChboxLaylist.add(layNeedFellowship);
                    }

                    layDateFellowship.setVisibility(View.VISIBLE);
                    int tempFellPos = adapter_startingdate.getPosition(popUsrObj.getDateNeedFellowship());
                    fellowshipDate.setSelection(tempFellPos);
                }
                if (popUsrObj.getNeedJob()) {
                    jobCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layNeedJob)) {
                        activeChboxLaylist.add(layNeedJob);
                    }

                    layDateJob.setVisibility(View.VISIBLE);
                    int tempJobPos = adapter_startingdate.getPosition(popUsrObj.getDateNeedJob());
                    jobDate.setSelection(tempJobPos);
                }
                if (popUsrObj.getNeedFinancialHelp()) {
                    needFinHelpCheckbox.setChecked(true);
                }

            } else if (popUsrObj.getUserProff().toLowerCase().contains("dental")) {

                // >> User is a dental non-physician
                layNeedResidency.setVisibility(View.VISIBLE);
                layNeedJob.setVisibility(View.VISIBLE);
                layNeedFinHelp.setVisibility(View.VISIBLE);

                // User-cat default checkboxes
                userCatChckBoxesList.clear();
                userCatChckBoxesList.add(layNeedResidency);
                userCatChckBoxesList.add(layNeedJob);

                if (popUsrObj.getNeedResidency()) {
                    residencyCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layNeedResidency)) {
                        activeChboxLaylist.add(layNeedResidency);
                    }

                    layDateResidency.setVisibility(View.VISIBLE);
                    int tempResPos = adapter_startingdate.getPosition(popUsrObj.getDateNeedResidency());
                    residencyDate.setSelection(tempResPos);
                }
                if (popUsrObj.getNeedJob()) {
                    jobCheckbox.setChecked(true);
                    if (!activeChboxLaylist.contains(layNeedJob)) {
                        activeChboxLaylist.add(layNeedJob);
                    }

                    layDateJob.setVisibility(View.VISIBLE);
                    int tempJobPos = adapter_startingdate.getPosition(popUsrObj.getDateNeedJob());
                    jobDate.setSelection(tempJobPos);
                }
                if (popUsrObj.getNeedFinancialHelp()) {
                    needFinHelpCheckbox.setChecked(true);
                }
            }
        }
        /******************************************************************************************/

        accountDelBtn = (Button)findViewById(R.id.del_myaccount_btn);
        accountDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDelAcc_D(context);
            }
        });

    }

    @Override
    public void onResume() {

        registerReceiver(sorRcv, new IntentFilter("a_SOR"));
        registerReceiver(soiEditRcv, new IntentFilter("a_edit_SOI"));
        registerReceiver(spEditMyProfRcv, new IntentFilter("u_edit_SP"));

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bmpUri = data.getData();
            setCropper(bmpUri);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {

                    // Retrieve temp_bmp file from uri
                    Bitmap tempBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);

                    // Compress bmp
                    userPicBmpSel = compressForThumbnail(tempBmp);

                    // Set compressed bmp file to imageView
                    userPhotoImg.setImageBitmap(userPicBmpSel);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        regBtn = menu.findItem(R.id.usr_edit_prof_done_btn);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Clean static objects
        selSubspecWidget = null;

    }

    @Override
    protected void onStop() {

        try {

            //unregisterReceiver(soiRcv);
            unregisterReceiver(sorRcv);
            unregisterReceiver(soiEditRcv);
            unregisterReceiver(spEditMyProfRcv);

        } catch (Exception e) {

        }

        super.onStop();
    }

    @Override
    public void onPause() {

        if ((dMenuVerData != null) && (dMenuVerData.isShowing())) {
            dMenuVerData.dismiss();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Back Arrow
        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }

        // Done button
        else if (id == R.id.usr_edit_prof_done_btn) {

            // Reset errorCheckSum with every regUser btn push
            errorCheckSum = 0;

            //1. Check if all fields are empty and set errors on mandatory ones
            /* Email */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.EMAIL,
                    emailField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                emailField.setError(tempFeedback);

                errorCheckSum++;
                //dMenuVerData.dismiss();
            }

            /* First Name */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.FIRSTNAME,
                    firstNameField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                firstNameField.setError(tempFeedback);

                errorCheckSum++;
                //dMenuVerData.dismiss();
            }

            /* Last Name */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.LASTNAME,
                    lastNameField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                lastNameField.setError(tempFeedback);

                errorCheckSum++;
                //dMenuVerData.dismiss();
            }

            /* City */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.CITY,
                    cityField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                cityField.setError(tempFeedback);

                errorCheckSum++;
                //dMenuVerData.dismiss();
            }

            /* State/Prov */
            if (sorWidget.getText().toString().equals(SELECT_TXT)) {
                // User hasn't selected a state/prof et, error!! beeeep!
                sorWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));

                errorCheckSum++;
            } else {
                sorWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            }

            /* Institution Name */
            // Check field errors only if Inst is visible
            if (instLayout.getVisibility() == View.VISIBLE) {

                inputProcessor = new StringProcessor();
                listFeedback = inputProcessor.processInput(StringProcessor.Mode.INST,
                        instField.getText().toString());
                if (!listFeedback.isEmpty()) {
                    tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                    instField.setError(tempFeedback);

                    errorCheckSum++;
                }
            }

            /* Check spec selection */
            if (selSpecWidget.getText().toString().equals(SELECT_TXT)) {
                // User hasn't selected a specialty yet, error!! beeeep!
                selSpecWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));

                errorCheckSum++;
                //dMenuVerData.dismiss();

            } else {
                selSpecWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            }

            /* Check subspec selection */
            if (selSubspecWidget.getText().toString().equals(SELECT_TXT)) {
                // User hasn't selected a subspecialty yet, error!! beeeep!
                selSubspecWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));

                errorCheckSum++;
                //dMenuVerData.dismiss();

            } else if (selSubspecWidget.getText().toString().equals("none")) {

                selSubspecWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            }

            /* Check SOI selection */
            if (soiLayout.getVisibility() == View.VISIBLE) {

                if (soiWidget.getText().toString().equals(SELECT_TXT)) {
                    // User hasn't selected a SOI yet, error!! beeeep!
                    soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));

                    errorCheckSum++;
                } else if (soiWidget.getText().toString().equals(soiWidget)) {
                    soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
                } else {
                    soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
                }
            }

            /* Mandatory Checkboxes */
            if (activeChboxLaylist.isEmpty()) {

                // No mandatory checkboxes selected
                // Add to error
                errorCheckSum++;

                // Mark visible mandatory check-boxes with errors
                cboxArrowToError(userCatChckBoxesList);


            } else {

                // At least one checkbox is selected - all good
            }

            /* Share data checkbox */
            if (!(shareData.isChecked())) {
                errorCheckSum++;
                Toast.makeText(context, "Please allow Plexus to share your data with other users",
                        Toast.LENGTH_LONG).show();
                //dMenuVerData.dismiss();
            }

            if (!(errorCheckSum == 0)) {

                // Toast with visibility check
                if (!(toast.getView().isShown())) {
                    toast = Toast.makeText(context, "Please fix errors and try again",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {

                // No errors, save to Parse
                createDataVerDg();

                editParseUser();
            }

            return true;
        }


        // About
        else if (id == R.id.usr_edit_prof_about_txt) {

            //Toast.makeText(context, "About", Toast.LENGTH_SHORT).show();
            About about = new About();
            about.launch(context);

            return true;
        }

        // Logout
        else if (id == R.id.usr_edit_prof_logout_txt) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(UserEditProfile.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            UserEditProfile.this.startActivity(mainIntent);
            UserEditProfile.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /* Create a spec selection dialog */
    private void createSelSpecDg(final Button widget) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_select_spec, null);

        // Set the Progress Bar
        delSpecProgressB = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        delSpecProgressB.setIndeterminate(true);
        delSpecProgressB.setVisibility(View.INVISIBLE);

        // Set dialog buttons
        AppCompatButton cancelBtn = (
                AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Disable subspec widget
                selSubspecWidget.setText(SELECT_TXT);

                // dismiss dialog
                dMenuSelSpec.dismiss();
            }
        });

        EditText searchField = (EditText) myDialogView.findViewById(R.id.search_spec_edit);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                // Filter the folder adapter
                specAdapter.getFilter().filter(s.toString());
                specAdapter.notifyDataSetChanged();
            }
        });


        // Target and populate the spec recycler view

        specRecyclerView = (RecyclerView) myDialogView.findViewById(R.id.spec_search_recycler_view);

        if (specRecyclerView != null) {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            //specListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            specRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            specRecyclerView.setLayoutManager(mLayoutManager);

        }

        // Call specParse from the local class
        specParseList(delSpecProgressB, specRecyclerView, widget, popUsrObj.getUserProff());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSelSpec = builder.create();
        dMenuSelSpec.setCanceledOnTouchOutside(false);
        dMenuSelSpec.show();
    }

    /* Create a subspec selection menu */
    private void createSelSubspecDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_select_subspec, null);

        // Set the Progress Bar
        delSpecProgressB = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        delSpecProgressB.setIndeterminate(true);
        delSpecProgressB.setVisibility(View.INVISIBLE);

        // Set dialog buttons
        AppCompatButton cancelBtn = (
                AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // dismiss dialog
                dMenuSelSubspec.dismiss();
            }
        });

        EditText searchField = (EditText) myDialogView.findViewById(R.id.search_spec_edit);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                // Filter the folder adapter
                subspecAdapter.getFilter().filter(s.toString());
                subspecAdapter.notifyDataSetChanged();
            }
        });

        subSpecRecyclerView = (RecyclerView) myDialogView.findViewById
                (R.id.subspec_search_recycler_view);

        if (subSpecRecyclerView != null) {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            //specListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            subSpecRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            subSpecRecyclerView.setLayoutManager(mLayoutManager);

        }

        // Call Parse selectSubspec method
        selectSubspec(specId, delSpecProgressB, selSubspecWidget);
        //Toast.makeText(context, specId, Toast.LENGTH_LONG).show();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSelSubspec = builder.create();
        dMenuSelSubspec.setCanceledOnTouchOutside(false);
        dMenuSelSubspec.show();

    }

    /* Create an AddUser data verification dialog */
    private void createDataVerDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_menu_ver_data, null);

        dataVerPBar = (ProgressBar) myDialogView.findViewById(R.id.ver_data_pbar);
        dataVerPBar.setIndeterminate(true);

        userSuccessTxt = (TextView) myDialogView.findViewById(R.id.text_success_reg);
        userSuccessTxt.setVisibility(View.GONE);

        dataVerTitle = (TextView) myDialogView.findViewById(R.id.verdata_dg_title);
        dataVerTitle.setText("Editing Profile");

        // Set up the OK button
        okBtn = (AppCompatButton)
                myDialogView.findViewById(R.id.dev_data_dg_btn);
        okBtn.setVisibility(View.GONE);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserEditProfile.this.finish();
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuVerData = builder.create();
        dMenuVerData.setCanceledOnTouchOutside(false);
        dMenuVerData.show();

    }

    /* User state of Interest widget dialog */
    private void createSOIDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_search_user_soi, null);

        // Set up the Cancel button
        cancelBtn = (AppCompatButton)
                myDialogView.findViewById(R.id.btn_cancel_SOI_D);
        cancelBtn.setText("select");

        // RecyclerView
        final RecyclerView soiRv = (RecyclerView) myDialogView.findViewById(R.id.search_user_cat_rv);

        soiRv.setHasFixedSize(true);
        RecyclerView.LayoutManager tempLayManager = new LinearLayoutManager(this);
        soiRv.setLayoutManager(tempLayManager);
        soiRv.setEnabled(false);
        SearchUserSOIAdapter userCatAdapter = new SearchUserSOIAdapter(populateSOI(), context,
                CALLER, soiRv, cancelBtn, popSOIArray);
        soiRv.setAdapter(userCatAdapter);

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dSOI = builder.create();
        dSOI.setCanceledOnTouchOutside(false);
        dSOI.show();
    }

    /* Password reset dialog method */
    private void createPassResetD(final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_menu_reset_password, null);

        AppCompatButton resetBtn = (AppCompatButton) myDialogView.findViewById(R.id.aff_del_spec_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPass(email);
                dPassReset.dismiss();
            }
        });

        AppCompatButton cancel = (AppCompatButton) myDialogView.findViewById(R.id.neg_del_spec_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dPassReset.dismiss();
            }
        });

        TextView emailTxt = (TextView) myDialogView.findViewById(R.id.conf_email_txt);
        emailTxt.setText(Html.fromHtml("<b>" + email + "</b>"));

        builder.setView(myDialogView);
        dPassReset = builder.create();
        dPassReset.setCanceledOnTouchOutside(false);
        dPassReset.show();
    }

    /* User state of residency widget dialog */
    private void createSORDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_search_user_sor, null);


        // Set up the Cancel button
        cancelBtn = (AppCompatButton)
                myDialogView.findViewById(R.id.btn_cancel_SOR_D);

        // Set up dialog title
        TextView dgTitle;
        dgTitle = (TextView) myDialogView.findViewById(R.id.SOR_dialog_title);
        dgTitle.setText("State");

        // RecyclerView
        final RecyclerView sorRv = (RecyclerView) myDialogView.findViewById(R.id.SOR_search_user_cat_rv);
        sorRv.setHasFixedSize(true);
        RecyclerView.LayoutManager tempLayManager = new LinearLayoutManager(this);
        sorRv.setLayoutManager(tempLayManager);
        sorRv.setEnabled(false);
        SearchUserSORAdapter userCatAdapter = new SearchUserSORAdapter(populateStates(), context,
                sorRv, toast);
        sorRv.setAdapter(userCatAdapter);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cancel logic goes here
                dSOI.dismiss();
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dSOI = builder.create();
        dSOI.setCanceledOnTouchOutside(false);
        dSOI.show();
    }

    /* Create verification dialog for account deletion */
    private void createDelAcc_D(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_menu_del_account, null);

        final Button yesBtn = (Button) myDialogView.findViewById(R.id.d_del_acc_yesBtn);
        final Button noBtn = (Button) myDialogView.findViewById(R.id.d_del_acc_noBtn);
        
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Call Parse del account
                yesBtn.setEnabled(false);
                deleteCurrentUser(context, yesBtn, dDelAccount);
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDelAccount.dismiss();
            }
        });

        builder.setView(myDialogView);
        dDelAccount = builder.create();
        dDelAccount.setCanceledOnTouchOutside(false);
        dDelAccount.show();

    }

    // Populate start-in date widget
    private List<CharSequence> populateDateWidget() {

        // Local ArrayAdapter
        List<CharSequence> tempArray = new ArrayList<CharSequence>();

        // 1. Get the current year and convert it into an int so you can add + 1 to it 3 times
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // 2. Populate the array with 4 year entries

        for (int i = 0; i < 6; i++) {

            if (i == 0) {
                tempArray.add(String.valueOf(year));
            } else {
                year++;
                tempArray.add(String.valueOf(year));
            }
        }

        // 3. Return updated array
        return tempArray;
    }

    // Parse email validation
    private void rtValidateEmail(final String email, final Context context, final ProgressBar pbar) {

        // Check for Internet
        if (DeviceConnection.testConnection(context)) {

            pbar.setVisibility(View.VISIBLE);

            // Query creation and implementation
            ParseQuery<ParseUser> checkSharedKeys = ParseUser.getQuery();
            checkSharedKeys.whereEqualTo("email", email);
            checkSharedKeys.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if (e == null) {

                        if (!(i >= 1)) {

                            // Good news, email is unique
                            pbar.setVisibility(View.GONE);
                        } else {

                            // Bad news, email exists
                            pbar.setVisibility(View.GONE);
                            emailField.setError("Email already in use");
                        }
                    }
                    // Parse error
                    else {

                        // Set pbar visibility to invisible
                        pbar.setVisibility(View.GONE);

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

            // No, Internet
            // Show network error
            Toast.makeText(context, "Network error, check Internet connection", Toast.LENGTH_LONG).show();

        }
    }

//    // Parse check on reg btn
//    private void checkEmailOnPress(final String email) {
//        // Instantiate this list to store each key's user category
//
//
//        if (DeviceConnection.testConnection(context)) {
//
//            //1. Check email
//            // Query creation and implementation
//            ParseQuery<ParseUser> checkEmailQuery = ParseUser.getQuery();
//            checkEmailQuery.whereEqualTo("email", email);
//            checkEmailQuery.countInBackground(new CountCallback() {
//                @Override
//                public void done(int i, ParseException e) {
//                    if (e == null) {
//
//                        if (!(i >= 1)) {
//
//                            // Keep it mute
//
//                        } else {
//
//                            dMenuVerData.dismiss();
//                            emailField.setError("Email already in use");
//                        }
//
//                    } else {
//
//                        // Hide dialog
//                        dMenuVerData.dismiss();
//
//                        // <marked>
//                        if (e.getCode() == 209) {
//                            ParseProcessor.createInvalidSessionD(context);
//                        } else {
//                            // Parse error, prompt with feedback
//                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//            });
//        } else {
//            // No, Internet
//            // Show network error
//            dMenuVerData.dismiss();
//            Toast.makeText(context, "Network error, check Internet connection", Toast.LENGTH_LONG).show();
//        }
//    }

    // Populates the state address spinner
    private List<String> populateStates() {

        // Local ArrayAdapter
        List<String> tempRetList = new ArrayList<String>();
        // Add states to the returning list
        tempRetList.add("Alabama");
        tempRetList.add("Alaska");
        tempRetList.add("Arizona");
        tempRetList.add("Arkansas");
        tempRetList.add("California");
        tempRetList.add("Colorado");
        tempRetList.add("Connecticut");
        tempRetList.add("Delaware");
        tempRetList.add("Florida");
        tempRetList.add("Georgia");
        tempRetList.add("Hawaii");
        tempRetList.add("Idaho");
        tempRetList.add("Illinois");
        tempRetList.add("Indiana");
        tempRetList.add("Iowa");
        tempRetList.add("Kansas");
        tempRetList.add("Kentucky");
        tempRetList.add("Louisiana");
        tempRetList.add("Maine");
        tempRetList.add("Maryland");
        tempRetList.add("Massachusetts");
        tempRetList.add("Michigan");
        tempRetList.add("Minnesota");
        tempRetList.add("Mississippi");
        tempRetList.add("Missouri");
        tempRetList.add("Montana");
        tempRetList.add("Nebraska");
        tempRetList.add("Nevada");
        tempRetList.add("New Hampshire");
        tempRetList.add("New Jersey");
        tempRetList.add("New Mexico");
        tempRetList.add("New York");
        tempRetList.add("North Carolina");
        tempRetList.add("North Dakota");
        tempRetList.add("Ohio");
        tempRetList.add("Oklahoma");
        tempRetList.add("Oregon");
        tempRetList.add("Pennsylvania");
        tempRetList.add("Rhode Island");
        tempRetList.add("South Carolina");
        tempRetList.add("South Dakota");
        tempRetList.add("Tennessee");
        tempRetList.add("Texas");
        tempRetList.add("Utah");
        tempRetList.add("Vermont");
        tempRetList.add("Virginia");
        tempRetList.add("Washington");
        tempRetList.add("West Virginia");
        tempRetList.add("Wisconsin");
        tempRetList.add("Wyoming");

        return tempRetList;
    }

    /* Parse shit */
    public List<Specialty> specParseList(final ProgressBar progressBar, final RecyclerView rw, final Button widget,
                                         final String userCat) {

        final List<Specialty> tempRetList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        // Query the Spec class
        ParseQuery query = new ParseQuery("Spec");
        query.orderByAscending("specName");

        // Check which user category spec Parse should bring back (medical vs dental)
        if (userCat.contains("Medical")) {
            query.whereContains("specType", "medical");
        } else if (userCat.contains("Dental")) {
            query.whereContains("specType", "dental");
        }

        // Check the device internet connection
        if (DeviceConnection.testConnection(context)) {

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    if (e == null) {

                        // Specialty object to work with temporarily
                        Specialty tempSpecObj;

                        // Iterate over the returning Parse list and assign values to a Specialty then
                        // nest each Specialty object into a list that can be returned for data population
                        for (ParseObject tempObj : list) {

                            // Populate a Spec obj
                            tempSpecObj = new Specialty(tempObj.get("specName").toString(),
                                    tempObj.getObjectId().toString());

                            // Add temp Spec. obj to the returning method's list
                            tempRetList.add(tempSpecObj);
                        }

                        // Create an adapter for rw and assign it to the rw
                        specAdapter = new SpecAdapter(tempRetList, tempRetList, context, dMenuSelSpec, CALLER);

                        rw.setAdapter(specAdapter);
                        specAdapter.notifyDataSetChanged();


                        //Set dialog progress bar visibility off
                        progressBar.setVisibility(View.INVISIBLE);

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

            /* Network Error */

            //Set dialog progress bar visibility off
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();

        }

        return tempRetList;
    }

    /* Select subspec on Spec ID - Parse */
    private void selectSubspec(final String parentId,
                               final ProgressBar progressBar,
                               final Button widget) {

        // Add the results into a list and expot it for the adapter
        final List<Subspecialty> tempList = new ArrayList<Subspecialty>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("SubSpec");
        query.orderByAscending("subSpecName");

        // Set progressBar to visible
        progressBar.setVisibility(View.VISIBLE);

        if (DeviceConnection.testConnection(context)) {

            // Local Internet OK
            query.whereEqualTo("parentId", parentId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    if (e == null) {

                        // No Parse errors, get the list
                        Subspecialty tempSubspec;
                        // Iterate through the list
                        for (ParseObject tempObj : list) {
                            //Log.i("SubSpec Name: ", tempObj.get("subSpecName").toString());
                            tempSubspec = new Subspecialty(tempObj.get("subSpecName").toString(),
                                    tempObj.getObjectId());

                            // Add this tempSubspec to the list
                            tempList.add(tempSubspec);
                        }

                        //TODO: Check if the adapter is empty set a new view with empty list user feedback


                        progressBar.setVisibility(View.INVISIBLE);

                        // Create an adapter for rw and assign it to the rw
                        subspecAdapter = new SubspecAdapter(tempList, tempList, context,
                                dMenuSelSubspec, widget, CALLER);

                        subSpecRecyclerView.setAdapter(subspecAdapter);


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

            /* Network Error */

            //Set dialog progress bar visibility off
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }

    }

    /* Add Parse user */
    private void editParseUser() {
        if (DeviceConnection.testConnection(context)) {

            // Fetch User from parse
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.getInBackground(popUsrObj.getUserId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null) {

                        //1. Collect data from all fields & sanitize it
                    /* Email */
                        String tempEmailInput = emailField.getText().toString().trim();
                        int index = tempEmailInput.indexOf('@');
                        String tempDomain = StringUtils.lowerCase(tempEmailInput.substring(index + 1,
                                tempEmailInput.length()));
                        String tempUserName = tempEmailInput.substring(0, index + 1);
                        String finalEmail = tempUserName.concat(tempDomain); // < -- add to Parse

                    /* First Name */
                        String tempFNameInput = WordUtils.capitalizeFully(firstNameField.getText().toString().trim()); // < -- add to Parse

                    /* Last Name */
                        String tempLNameInput = WordUtils.capitalizeFully(lastNameField.getText().toString().trim());  // < -- add to Parse

                    /* Address */
                        String tempAddressInput = null;  // < -- add to Parse
                        if (!(addressField.getText().toString().isEmpty()) ||
                                !(addressField.getText().toString().equals(" ") ||
                                        !(addressField.getText().toString().equals("  ")))) {

                            tempAddressInput = WordUtils.capitalize(addressField.getText().toString().trim());
                        } else {
                            tempAddressInput = "";
                        }

                    /* City */
                        String tempCityInput = WordUtils.capitalizeFully(cityField.getText().toString().trim()); // < -- add to Parse

                    /* Zip */
                        String tempZipInput = null; // < -- add to Parse
                        if (zipField.getText().toString().isEmpty()) {
                            tempZipInput = "";
                        } else {
                            tempZipInput = zipField.getText().toString();
                        }

                    /* Institution */
                        String tempInstInput = new String();
                        if (instField.getVisibility() == View.VISIBLE) {
                            tempInstInput = WordUtils.capitalizeFully(instField.getText().toString().trim()); // < -- add to Parse
                        }


                    /* State of Residence */
                        String tempStateInput = sorWidget.getText().toString(); // < -- add to Parse

                    /* User Category */
                        String tempCatInput = new String();
                        if (userCatLay.getVisibility() == View.VISIBLE) {
                            tempCatInput = userCatSpinner.getSelectedItem().toString(); // < -- add to Parse
                        }


                    /* User Specialty */
                        String tempSpecInput = selSpecWidget.getText().toString(); // < -- add to Parse

                    /* User Subspecialty */
                        String tempSubspecInput = selSubspecWidget.getText().toString(); // < -- add to Parse

                    /* Checkbox Need Clerkship */
                        Boolean cboxNeedClerkship = false;      // < -- add to Parse
                        String tempClerkshipDate = "";          // < -- add to Parse

                        if (clerkshipCheckbox.isChecked()) {
                            cboxNeedClerkship = true;
                            tempClerkshipDate = clerkshipDate.getSelectedItem().toString();

                        }

                    /* Checkbox Need Residency */
                        Boolean cboxNeedResidency = false;      // < -- add to Parse
                        String tempResidencyDate = "";          // < -- add to Parse

                        if (residencyCheckbox.isChecked()) {
                            cboxNeedResidency = true;
                            tempResidencyDate = residencyDate.getSelectedItem().toString();
                        }

                    /* Checkbox Need Fellowship */
                        Boolean cboxNeedFellowship = false;     // < -- add to Parse
                        String tempFellowshipDate = "";         // < -- add to Parse

                        if (fellowshipCheckbox.isChecked()) {
                            cboxNeedFellowship = true;
                            tempFellowshipDate = fellowshipDate.getSelectedItem().toString();
                        }

                    /* Checkbox Need Job */
                        Boolean cboxNeedJob = false;            // < -- add to Parse
                        String tempJobDate = "";                // < -- add to Parse

                        if (jobCheckbox.isChecked()) {
                            cboxNeedJob = true;
                            tempJobDate = jobDate.getSelectedItem().toString();
                        }

                    /* Checkbox Offer Clerkship */
                        Boolean cboxOfferClerkship = false;     // < -- add to Parse
                        String tempOfferClerkshipDate = "";     // < -- add to Parse

                        if (offerClerkshipCheckbox.isChecked()) {
                            cboxOfferClerkship = true;
                            tempOfferClerkshipDate = offClkDate.getSelectedItem().toString();
                        }

                    /* Checkbox Offer Residency */
                        Boolean cboxOfferResidency = false;     // < -- add to Parse
                        String tempOfferResidencyDate = "";     // < -- add to Parse

                        if (offerResidencyCheckbox.isChecked()) {
                            cboxOfferResidency = true;
                            tempOfferResidencyDate = offResDate.getSelectedItem().toString();
                        }

                    /* Checkbox Offer Fellowship */
                        Boolean cboxOfferFellowship = false;    // < -- add to Parse
                        String tempOfferFellowshipDate = "";    // < -- add to Parse

                        if (offerFellowshipCheckbox.isChecked()) {
                            cboxOfferFellowship = true;
                            tempOfferFellowshipDate = offFellowDate.getSelectedItem().toString();
                        }

                    /* Checkbox Offer Job */
                        Boolean cboxOfferJob = false;           // < -- add to Parse
                        String tempOfferJobDate = "";           // < -- add to Parse

                        if (offerJobCheckbox.isChecked()) {
                            cboxOfferJob = true;
                            tempOfferJobDate = offJobDate.getSelectedItem().toString();
                        }

                    /* Checkbox Need Financial Help */
                        Boolean cboxNeedFinancialHelp = false;  // < -- add to Parse

                        if (needFinHelpCheckbox.isChecked()) {
                            cboxNeedFinancialHelp = true;
                        }

                    /* Checkbox Assist Interviewing */
                        Boolean cboxAssistInterv = false;       // < -- add to Parse

                        if (assistIntervCheckbox.isChecked()) {
                            cboxAssistInterv = true;
                        }

                        user.setUsername(finalEmail);
                        user.setEmail(finalEmail);

                        user.put("userCat", "user");            // change this to admin if needed
                        user.put("firstName", tempFNameInput);
                        user.put("lastName", tempLNameInput);
                        user.put("address", tempAddressInput);
                        user.put("city", tempCityInput);
                        user.put("zip", tempZipInput);
                        if (instField.getVisibility() == View.VISIBLE) {
                            user.put("instName", tempInstInput);
                        }

                        user.put("state", tempStateInput);
                        if (userCatLay.getVisibility() == View.VISIBLE) {
                            user.put("userProff", tempCatInput);
                        }

                        user.put("userSpec", tempSpecInput);
                        user.put("userSubspec", tempSubspecInput);

                        // SOI
                        //TODO: check the popUsrObj first
                        if (popSOIArray.contains("Alabama")) {
                            user.put("soi_alabama", true);
                        } else {
                            user.put("soi_alabama", false);
                        }

                        if (popSOIArray.contains("Florida")) {
                            user.put("soi_florida", true);
                        } else {
                            user.put("soi_florida", false);
                        }

                        if (popSOIArray.contains("Georgia")) {
                            user.put("soi_georgia", true);
                        } else {
                            user.put("soi_georgia", false);
                        }

                        if (popSOIArray.contains("Kentucky")) {
                            user.put("soi_kentucky", true);
                        } else {
                            user.put("soi_kentucky", false);
                        }

                        if (popSOIArray.contains("Missouri")) {
                            user.put("soi_missouri", true);
                        } else {
                            user.put("soi_missouri", false);
                        }

                        if (popSOIArray.contains("North Carolina")) {
                            user.put("soi_nc", true);
                        } else {
                            user.put("soi_nc", false);
                        }

                        if (popSOIArray.contains("South Carolina")) {
                            user.put("soi_sc", true);
                        } else {
                            user.put("soi_sc", false);
                        }

                        if (popSOIArray.contains("Tennessee")) {
                            user.put("soi_tennessee", true);
                        } else {
                            user.put("soi_tennessee", false);
                        }

                        user.put("needClerkship", cboxNeedClerkship);
                        user.put("dateNeedClerkship", tempClerkshipDate);
                        user.put("needResidency", cboxNeedResidency);
                        user.put("dateNeedResidency", tempResidencyDate);
                        user.put("needFellowship", cboxNeedFellowship);
                        user.put("dateNeedFellowship", tempFellowshipDate);
                        user.put("needJob", cboxNeedJob);
                        user.put("dateNeedJob", tempJobDate);

                        user.put("offerClerkship", cboxOfferClerkship);
                        user.put("dateOfferClerkship", tempOfferClerkshipDate);
                        user.put("offerResidency", cboxOfferResidency);
                        user.put("dateOfferResidency", tempOfferResidencyDate);
                        user.put("offerFellowship", cboxOfferFellowship);
                        user.put("dateOfferFellowship", tempOfferFellowshipDate);
                        user.put("offerJob", cboxOfferJob);
                        user.put("dateOfferJob", tempOfferJobDate);

                        user.put("needFinancialHelp", cboxNeedFinancialHelp);
                        user.put("assistInterv", cboxAssistInterv);

                        // Don't save & compress localUser photo if it already exists
                        try {
                            if (! PhotoProcessor.checkLocalPhoto(context, popUsrObj.getProfilePicture())) {

                                // Dynamically generated photo name
                                String tempPhotoName = PhotoProcessor.photoFullName(tempFNameInput, tempLNameInput);

                                // Create a byteArrayStream, compress the bmp and save it to a bytearray
                                // Then save the bytearray to an object of type ParseFile
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                userPicBmpSel.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                byte[] byteArray = stream.toByteArray();
                                ParseFile photoParseFile = new ParseFile(tempPhotoName, byteArray);

                                user.put("profilePicture", photoParseFile);

                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    //TODO: 4. Delete reg key used for this user
                                    //1. Hide the progress bar
                                    dataVerPBar.setVisibility(View.GONE);

                                    //2. Show okBtn & successTxt
                                    okBtn.setVisibility(View.VISIBLE);
                                    userSuccessTxt.setVisibility(View.VISIBLE);
                                    userSuccessTxt.setText("Profile updated");
                                    dataVerTitle.setText("Congratulations!");

                                } else {

                                    // Dismiss dialog, show Parse error
                                    dMenuVerData.dismiss();
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
                    } else

                    {

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

        } else

        {
            //Set dialog progress bar visibility off
            dMenuVerData.dismiss();
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }

    }

    /* Method sets error arrows to visible */
    private void cboxArrowToError(final List<RelativeLayout> list) {

        if ((list != null) || !(list.isEmpty())) {

            // Iterate through the list with active checkboxes
            for (RelativeLayout linearLayout : list) {

                // Show the error arrow corresponding to this layout
                int children = linearLayout.getChildCount();
                for (int i = 0; i < children; ++i) {

                    // Target only arrows
                    ImageView tempView = null;
                    tempView = (ImageView) linearLayout.getChildAt(0);

                    if (tempView.getTag().equals(linearLayout.getTag().toString())) {

                        tempView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /* Method sets error arrows to invisible */
    private void cboxArrowToGreen(final List<RelativeLayout> list) {

        if ((list != null) || !(list.isEmpty())) {

            // Iterate through the list with active checkboxes
            for (RelativeLayout linearLayout : list) {

                // Show the error arrow corresponding to this layout
                int children = linearLayout.getChildCount();
                for (int i = 0; i < children; ++i) {

                    // Target only arrows
                    ImageView tempView = null;
                    tempView = (ImageView) linearLayout.getChildAt(0);

                    if (tempView.getTag().equals(linearLayout.getTag().toString())) {

                        tempView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    /* Populates the state of interest widget */
    private ArrayList<String> populateSOI() {

        // Local ArrayAdapter
        ArrayList<String> tempRetList = new ArrayList<String>();

        // Add states to the returning list
        tempRetList.add("All");
        tempRetList.add("Alabama");
        tempRetList.add("Florida");
        tempRetList.add("Georgia");
        tempRetList.add("Kentucky");
        tempRetList.add("Missouri");
        tempRetList.add("North Carolina");
        tempRetList.add("South Carolina");
        tempRetList.add("Tennessee");

        return tempRetList;
    }

    /* BroadcastReceiver used for the SOR adapter */
    private BroadcastReceiver sorRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Temp var with the value
            String sorValue = intent.getStringExtra("textValue");

            if ((sorValue != null) || (!sorValue.isEmpty())) {

                sorWidget.setText(sorValue);
                sorWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
                dSOI.dismiss();
            }
        }
    };

    /* BroadcastReceiver used for the editProfSOI adapter */
    private BroadcastReceiver soiEditRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            prof_passStates = intent.getStringArrayListExtra("textValue");

            dSOI.dismiss();

            if (prof_passStates.size() == 1) {
                soiWidget.setText(prof_passStates.get(0));
                soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            } else if (prof_passStates.size() > 1) {
                soiWidget.setText(MULTI_STATE);
                soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            } else {
                soiWidget.setText(SELECT_TXT);
                soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));
            }
        }
    };

    /* BroadcastReceiver used for the editProfSpec */
    private BroadcastReceiver spEditMyProfRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Reset spec & subspec widgets (for some strange reason I told myself I have to keep this)
            selSpecWidget.setText(SELECT_TXT);
            selSubspecWidget.setText(SELECT_TXT);

            // catch interface variables in here
            specName = intent.getStringExtra("textValue");
            specId = intent.getStringExtra("textId");

            // Set a spec-name into the spec-widget
            selSpecWidget.setText(specName);

            // If spec widget is set to "other" or "undecided" assign the same to subspec widget
            if (specName.toLowerCase().equals("other") ||
                    specName.toLowerCase().equals("undecided")) {

                selSubspecWidget.setText(specName);
            }

        }
    };

    private void resetPass(String email) {

        if (DeviceConnection.testConnection(context)) {

            toast = Toast.makeText(context, "Sending Link...", Toast.LENGTH_LONG);
            toast.show();

            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        toast = Toast.makeText(context, "Link Sent", Toast.LENGTH_LONG);
                        toast.show();
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

    /* Delete current user */
    private void deleteCurrentUser(final Context context, final Button button, final AlertDialog dialog) {
        if (DeviceConnection.testConnection(context)) {
            final ParseUser deluser = ParseUser.getCurrentUser();
            deluser.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        // Dismiss dialog
                        dialog.dismiss();
                        deluser.logOut();
                        Intent exitIntent = new Intent(context, GeneralLogin.class);
                        exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(exitIntent);
                        UserEditProfile.this.finish();

                    } else {
                        // <marked>
                        if (e.getCode() == 209) {
                            ParseProcessor.createInvalidSessionD(context);
                        } else {
                            // Parse error, prompt with feedback
                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                            button.setEnabled(true);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
        }
    }

    /* Creates a Cropper from the imported lib */
    private void setCropper(Uri uri) {
        CropImage.activity(uri)

                .setAllowRotation(true)
                .setAllowCounterRotation(true)
                .setAutoZoomEnabled(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setFixAspectRatio(true)
                .setShowCropOverlay(true)
                .setActivityTitle("Crop Selection")
                .start(this);
    }

    /*  */
    // Compress and resize for thumbnail
    private Bitmap compressForThumbnail(Bitmap fullSize) {

        // New resized bitmap
        Bitmap thumbnail = Bitmap.createScaledBitmap(fullSize, fullSize.getWidth() / 3,
                fullSize.getHeight() / 3, true);

        return thumbnail;
    }
}

