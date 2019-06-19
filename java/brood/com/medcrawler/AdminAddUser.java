package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
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
import android.text.InputType;
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
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AdminAddUser extends AppCompatActivity {

    private Context context;
    private android.support.v7.widget.AppCompatEditText emailField;
    private android.support.v7.widget.AppCompatEditText passwordField;
    private android.support.v7.widget.AppCompatEditText passwordRepField;
    private android.support.v7.widget.AppCompatEditText regKeyField;
    private android.support.v7.widget.AppCompatEditText firstNameField;
    private android.support.v7.widget.AppCompatEditText lastNameField;
    private android.support.v7.widget.AppCompatEditText addressField;
    private android.support.v7.widget.AppCompatEditText cityField;
    private android.support.v7.widget.AppCompatEditText zipField;
    private android.support.v7.widget.CardView cboxCard;
    private android.support.v7.widget.AppCompatCheckBox shareData;
    private android.support.v7.widget.AppCompatButton selSpecWidget;
    private android.support.v7.widget.AppCompatCheckBox clerkshipCheckbox;
    private android.support.v7.widget.AppCompatCheckBox residencyCheckbox;
    private android.support.v7.widget.AppCompatCheckBox fellowshipCheckbox;
    private android.support.v7.widget.AppCompatCheckBox jobCheckbox;
    private android.support.v7.widget.AppCompatCheckBox offerClerkshipCheckbox;
    private android.support.v7.widget.AppCompatCheckBox offerResidencyCheckbox;
    private android.support.v7.widget.AppCompatCheckBox offerFellowshipCheckbox;
    private android.support.v7.widget.AppCompatCheckBox offerJobCheckbox;
    private android.support.v7.widget.AppCompatCheckBox needFinHelpCheckbox;
    private List<RelativeLayout> activeChboxLaylist;

    private String PHYSICIAN_SHARE_DATA_TEXT = "I agree to share my data with potential residents or students";
    private String NON_PHYSICIAN_SHARE_DATA_TEXT = "I agree to share my data with potential practices";
    private int PICK_IMAGE = 83;

    private android.support.v7.widget.AppCompatButton selSubspecWidget;
    private String specId;
    private String specName;
    private String subspecName;
    private static String ERROR = "error";
    private static String SOR_DG_TITLE = "State";
    private static String MULTI_STATE = "Multiple";
    private static String SELECT_TXT = "select";
    private final String NONE = "none";
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
    private LinearLayout layDateOffFellowship;
    private ProgressBar keyCheckPBar;
    private ProgressBar emailCheckPBar;
    private ProgressBar dataVerPBar;
    private ProgressBar delSpecProgressB;
    private AlertDialog dMenuSelSpec;
    private AlertDialog dMenuSelSubspec;
    private AlertDialog dMenuVerData;
    private AlertDialog dSOI;
    private SpecAdapter specAdapter;
    private SubspecAdapter subspecAdapter;
    private android.support.v7.widget.RecyclerView specRecyclerView;
    private android.support.v7.widget.RecyclerView subSpecRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static String CALLER = "reg";
    private final String SUBCALLER = "sub_reg";
    private int errorCheckSum = 10;
    private String tempFeedback;
    private android.support.v7.widget.AppCompatButton okBtn;
    private TextView userSuccessTxt;
    private TextView userCatTextView;
    private TextView dataVerTitle;
    private MenuItem regBtn;
    private MenuItem logOutMenuBtn;
    private Toast toast;
    private List<String> keyUsrCatList;
    private AppCompatButton userCatWg;
    private AppCompatButton sorWidget;
    private AppCompatButton soiWidget;
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
    private LinearLayout soiLayout;
    private CardView photoUsrCV;
    private ImageView userPhotoImg;
    private Bitmap userPicBmpSel;
    private Uri bmpUri;
    private Boolean photoCtrlVar = false;
    private String tempUserCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        // Set context
        context = this;

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

                // To get the x & y coordinates use
                // int scrollX = <customScroll>.getScrollX();
                // int scrollY = <customScroll>.getScrollY();
            }
        });

        // We use Toolbars instead of ActionBars as the latter was deprecated
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
            getSupportActionBar().setTitle("User Registration"); // <---- change this to dyn. username
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setSubtitle("Add UserAdminMain");
        }

        // Hide the keyboard as as soon as the activity is loaded
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Instantialize the toast
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

        // Instantialize the active/visible checkboxs list
        activeChboxLaylist = new ArrayList<>();

        // Target the checkbox card
        cboxCard = (android.support.v7.widget.CardView) findViewById(R.id.checkbox_card);

        // Instantialize passStates & passPos arrays (pass data from adapter to broadcasterRCV)
        passStates = new ArrayList<>();
        passPos = new ArrayList<>();

        // Instantiate the user cat spinner's textview
        userCatTextView = (TextView) findViewById(R.id.user_category_textview);

        // Target emailCheckPBar
        emailCheckPBar = (ProgressBar) findViewById(R.id.email_check_pbar);
        emailCheckPBar.setVisibility(View.GONE);

        // Target email edit-text
        emailField = (android.support.v7.widget.
                AppCompatEditText) findViewById(R.id.admin_addUser_email_edit);
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
                        String tempJunction = emailField.getText().toString();

                        // Call Parse's email validation
                        rtValidateEmail(tempJunction, context, emailCheckPBar);
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

        // Target password edit-text
        passwordField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_password_edit);

        /* Set the password-field to default font using this hack */
        passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordField.setTypeface(Typeface.DEFAULT);
        /* ----------------------------------------------------- */

        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.PASSWORD,
                            passwordField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        passwordField.setError(tempFeedback);
                    }
                }
            }
        });
        passwordField.addTextChangedListener(new TextWatcher() {
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

        // Target password repeat edit-text
        passwordRepField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_reppassword_edit);

        /* Set the password-field to default font using this hack */
        passwordRepField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordRepField.setTypeface(Typeface.DEFAULT);
        /* ----------------------------------------------------- */

        passwordRepField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.REPPASSWORD,
                            passwordRepField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        passwordRepField.setError(tempFeedback);
                    }
                }

            }
        });
        passwordRepField.addTextChangedListener(new TextWatcher() {
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

        // Target keyCheckPBar
        keyCheckPBar = (ProgressBar) findViewById(R.id.key_check_pbar);
        keyCheckPBar.setVisibility(View.GONE);

        // Target regkey edit-text
        regKeyField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_keycode_edit1);
        regKeyField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {
                    inputProcessor = new StringProcessor();
                    listFeedback = inputProcessor.processInput(StringProcessor.Mode.REGKEY,
                            regKeyField.getText().toString());

                    // There are errors, so read them
                    if (!listFeedback.isEmpty()) {

                        tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                        regKeyField.setError(tempFeedback);

                        // Hide checkboxes
                        hideCheckBoxes();

                        // Reset user cat, spec & subspec widgets
                        userCatWg.setText("auto-populated");
                        selSpecWidget.setText(SELECT_TXT);
                        if ((tempUserCat != null) && (!tempUserCat.toLowerCase().contains("dental"))) {
                            selSubspecWidget.setText(SELECT_TXT);
                        }
                    }

                }

            }
        });
        regKeyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 8) {

                    // Take value from both reg-key fields, capitalize them & join them
                    String tempJunction = WordUtils.capitalize(regKeyField.getText().toString());

                    // Call Parse to validate the key
                    rtValidateRegKey(tempJunction, context, keyCheckPBar);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

            }
        });

        userPhotoImg = (ImageView) findViewById(R.id.usr_prof_usrimg);

        firstNameField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_firstname_edit);
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

        lastNameField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_lastname_edit);
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

        addressField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_address_edit);
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

        cityField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_city_edit);
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

        zipField = (android.support.v7.widget.AppCompatEditText)
                findViewById(R.id.admin_addUser_zip_edit);
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

                    //Log.i("crap", "shit");
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


        // Target DelSubspecWidget (it's still a button, but it's meant for text display)
        selSpecWidget = (android.support.v7.widget.AppCompatButton) findViewById(R.id.del_sub_spec_widget);
        selSpecWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check the value of the User Category widget
                if (userCatWg.getText().toString().contains("auto-populated")) {
                    if (!(toast.getView().isShown())) {
                        toast = Toast.makeText(context, "'User Category' is missing",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {

                    // Reset subspec widget to "select" - keeps both widgets in sync by forcing user
                    // to re-select a subspeciality
                    createSelSpecDg(selSpecWidget);

                    if (!tempUserCat.toLowerCase().contains("dental")) {
                        selSubspecWidget.setText(SELECT_TXT);
                    }

                }


            }
        });

        // Target the select_sub_spec_widget
        selSubspecWidget = (android.support.v7.widget.AppCompatButton) findViewById(R.id.sel_sub_spec_widget);

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

        // Set state-of-starting date widget
        sorWidget = (AppCompatButton) findViewById(R.id.admin_addUser_sor_w);

        sorWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createSORDg();
            }
        });

        /******************************************************************************************/

        // Set user-category widget
        userCatWg = (android.support.v7.widget.AppCompatButton) findViewById(R.id.admin_addUser_usercat_widget);
        //userCatWg.setClickable(false);
        userCatWg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userCatWg.getText().toString().equals("auto-populated")) {

                    if (!(toast.getView().isShown())) {
                        toast = Toast.makeText(context, "'User Category' requires a valid 'Registration Key'",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {

                    if (!(toast.getView().isShown())) {
                        toast = Toast.makeText(context, "'User Category' is key-dependent and can not be changed",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        /******************************************************************************************/

        // Set state-of-interest widget && layout
        soiLayout = (LinearLayout) findViewById(R.id.soi_layout);
        soiWidget = (AppCompatButton) findViewById(R.id.admin_addUser_soi_w);

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
        layDateClerkship.setVisibility(View.GONE);

        layNeedResidency = (RelativeLayout) findViewById(R.id.layout_res_south);
        layDateResidency = (LinearLayout) findViewById(R.id.layout_stardate_residency);
        layDateResidency.setVisibility(View.GONE);

        layNeedFellowship = (RelativeLayout) findViewById(R.id.layout_fellowship_south);
        layDateFellowship = (LinearLayout) findViewById(R.id.layout_stardate_fellowship);
        layDateFellowship.setVisibility(View.GONE);

        layNeedJob = (RelativeLayout) findViewById(R.id.layout_job_south);
        layDateJob = (LinearLayout) findViewById(R.id.layout_stardate_job);
        layDateJob.setVisibility(View.GONE);

        layOfferClerkship = (RelativeLayout) findViewById(R.id.layout_haveclerkship_pos);
        layDateOffClerkship = (LinearLayout) findViewById(R.id.layout_stardate_offer_clerkship);
        layDateOffClerkship.setVisibility(View.GONE);

        layOfferResidency = (RelativeLayout) findViewById(R.id.layout_haveres_pos);
        layDateResidency = (LinearLayout) findViewById(R.id.layout_stardate_offer_residency);
        layDateResidency.setVisibility(View.GONE);

        layOfferFellowship = (RelativeLayout) findViewById(R.id.layout_havefellow_pos);
        layDateOffFellowship = (LinearLayout) findViewById(R.id.layout_stardate_offer_fellowship);
        layDateOffFellowship.setVisibility(View.GONE);

        layOfferJob = (RelativeLayout) findViewById(R.id.layout_havejob_pos);
        layDateJob = (LinearLayout) findViewById(R.id.layout_stardate_offer_job);
        layDateJob.setVisibility(View.GONE);

        layOfferAssist = (RelativeLayout) findViewById(R.id.layout_assist_in_inter);

        layNeedFinHelp = (LinearLayout) findViewById(R.id.layout_checkbox_financial);

        // Target clerkship checkbox
        clerkshipCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_clerckship_ckbox);
        clerkshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout clerkshipStardateLay = (LinearLayout) findViewById
                        (R.id.layout_stardate_clerkship);

                if (b) {
                    // Display the date layout
                    clerkshipStardateLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the date layout
                    clerkshipStardateLay.setVisibility(View.GONE);
                }
            }
        });

        clerkshipDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_clerkship);
        clerkshipDate.setAdapter(adapter_startingdate);

        // Target residency checkbox
        residencyCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_residency_ckbox);
        residencyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout residencyStardateLay = (LinearLayout) findViewById
                        (R.id.layout_stardate_residency);

                if (b) {
                    // Display the date layout
                    residencyStardateLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the date layout
                    residencyStardateLay.setVisibility(View.GONE);
                }
            }
        });

        residencyDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_residency);
        residencyDate.setAdapter(adapter_startingdate);

        // Target fellowship checkbox
        fellowshipCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_fellow_ckbox);
        fellowshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout fellowshipStardateLay = (LinearLayout) findViewById
                        (R.id.layout_stardate_fellowship);

                if (b) {
                    // Display the date layout
                    fellowshipStardateLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the date layout
                    fellowshipStardateLay.setVisibility(View.GONE);
                }
            }
        });

        fellowshipDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_fellowship);
        fellowshipDate.setAdapter(adapter_startingdate);

        // Target job checkbox
        jobCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_job_ckbox);
        jobCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout jobStartdateLay = (LinearLayout) findViewById(R.id.layout_stardate_job);

                if (b) {
                    // Display the date layout
                    jobStartdateLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the date layout
                    jobStartdateLay.setVisibility(View.GONE);
                }
            }
        });

        jobDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_job);
        jobDate.setAdapter(adapter_startingdate);

        // Target offer clerkship checkbox
        offerClerkshipCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_hostckship_ckbox);
        offerClerkshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout offerClkerkshipLay = (LinearLayout) findViewById
                        (R.id.layout_stardate_offer_clerkship);

                if (b) {
                    // Display the date layout
                    offerClkerkshipLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the date layout
                    offerClkerkshipLay.setVisibility(View.GONE);
                }
            }
        });

        offClkDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_offer_clerkship);
        offClkDate.setAdapter(adapter_startingdate);

        // Target offer residency checkbox
        offerResidencyCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_haveres_ckbox);
        offerResidencyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout offerResLay = (LinearLayout) findViewById
                        (R.id.layout_stardate_offer_residency);

                if (b) {
                    // Display the date layout
                    offerResLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the date layout
                    offerResLay.setVisibility(View.GONE);
                }
            }
        });

        offResDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_offer_residency);
        offResDate.setAdapter(adapter_startingdate);

        // Target offer fellowship checkbox
        offerFellowshipCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_havefellow_ckbox);
        offerFellowshipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout offerFellowLay = (LinearLayout) findViewById(R.id.layout_stardate_offer_fellowship);

                if (b) {
                    // Display the date layout
                    offerFellowLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {
                    // Hide the data layout
                    offerFellowLay.setVisibility(View.GONE);
                }
            }
        });

        offFellowDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_offer_fellowship);
        offFellowDate.setAdapter(adapter_startingdate);

        // Target offer job checkbox
        offerJobCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_havejob_ckbox);
        offerJobCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout offerJobLay = (LinearLayout) findViewById(R.id.layout_stardate_offer_job);

                if (b) {

                    // Display the date layout
                    offerJobLay.setVisibility(View.VISIBLE);
                    // Hide all arrows
                    cboxArrowToGreen(activeChboxLaylist);
                } else {

                    // Hide the date layout
                    offerJobLay.setVisibility(View.GONE);
                }
            }
        });

        offJobDate = (android.support.v7.widget.AppCompatSpinner) findViewById
                (R.id.admin_addUser_date_offer_job);
        offJobDate.setAdapter(adapter_startingdate);

        // Target need financial help checkbox
        needFinHelpCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_financial_ckbox);
        needFinHelpCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        // Target assist interviewing checkbox
        assistIntervCheckbox = (android.support.v7.widget.AppCompatCheckBox) findViewById
                (R.id.admin_addUser_assist_residency_ckbox);


        // Target shareData checkbox
        shareData = (android.support.v7.widget.AppCompatCheckBox) findViewById
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
    }

    @Override
    public void onResume() {

        registerReceiver(soiRcv, new IntentFilter("a_SOI"));
        registerReceiver(sorRcv, new IntentFilter("a_SOR"));
        registerReceiver(admin_specRcv, new IntentFilter("a_Spec"));
        registerReceiver(admin_subSpecRcv, new IntentFilter("a_subSpec"));

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

                    // Photo has been successfully cropped and return to profile
                    photoCtrlVar = true; // confirm photo has been selected

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                // Error on cropped and photo return to profile
                photoCtrlVar = false; // confirm photo has been selected
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        regBtn = menu.findItem(R.id.adm_add_usr_done_btn);
        logOutMenuBtn = menu.findItem(R.id.adm_add_usr_logout_txt);

        // Hide the logout menu-button if the user is not logged in - meaning if this activity is
        // accessed without logging credentials
        if (ParseUser.getCurrentUser() == null) {
            logOutMenuBtn.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_add_user, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Clean static objects
        selSpecWidget = null;
        PHYSICIAN_SHARE_DATA_TEXT = null;
        NON_PHYSICIAN_SHARE_DATA_TEXT = null;
        specName = null;
        selSubspecWidget = null;
        specId = null;
        subspecName = null;

        //todo: clean all static variables

    }

    @Override
    protected void onStop() {

        try {

            unregisterReceiver(soiRcv);
            unregisterReceiver(sorRcv);
            unregisterReceiver(admin_specRcv);
            unregisterReceiver(admin_subSpecRcv);

        } catch (Exception e) {
            //e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle_shape clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Return arrow
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        // Done button
        else if (id == R.id.adm_add_usr_done_btn) {

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

            /* Password */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.PASSWORD,
                    passwordField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                passwordField.setError(tempFeedback);

                errorCheckSum++;
                //dMenuVerData.dismiss();
            } else {

                if (passwordField.getError() == null) {
                /* Check if both passwords are equal */
                    // Compare passField and passRepField
                    if (!passwordField.getText().toString().
                            equals(passwordRepField.getText().toString())) {

                        // Fields don't have the same value, so error
                        //passwordRepField.setError("Passwords don't match");
                        passwordField.setError("Passwords don't match");

                        errorCheckSum++;
                        //dMenuVerData.dismiss();

                    } else {

                        // If passwords do mach dismiss the error
                        //passwordRepField.setError(null);
                        passwordField.setError(null);
                        passwordRepField.setError(null);

                    }
                }

            }

            /* Repeat Password */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.REPPASSWORD,
                    passwordRepField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                passwordRepField.setError(tempFeedback);

                errorCheckSum++;
                //dMenuVerData.dismiss();
            } else {

                if (passwordRepField.getError() == null) {
                /* Check if both passwords are equal */
                    // Compare passField and passRepField
                    if (!passwordField.getText().toString().
                            equals(passwordRepField.getText().toString())) {

                        // Fields don't have the same value, so error
                        passwordRepField.setError("Passwords don't match");

                        errorCheckSum++;
                        //dMenuVerData.dismiss();

                    } else {

                        // If passwords do mach dismiss the error
                        passwordRepField.setError(null);
                        passwordField.setError(null);
                    }
                }

            }

            /* Reg Key */
            inputProcessor = new StringProcessor();
            listFeedback = inputProcessor.processInput(StringProcessor.Mode.REGKEY,
                    regKeyField.getText().toString());
            if (!listFeedback.isEmpty()) {
                tempFeedback = new String(listFeedback.get(0).getFeedbackText());
                regKeyField.setError(tempFeedback);

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
                    soiWidget.setBackgroundColor(Color.RED);

                    errorCheckSum++;
                } else if (soiWidget.getText().toString().equals(soiWidget)) {
                    soiWidget.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    soiWidget.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            /* Mandatory Checkboxes */
            if (!activeChboxLaylist.isEmpty()) {

                if (!(cBoxIsVisible(activeChboxLaylist))) {

                    // At least one checkbox is selected - no error (good thing)

                } else {

                    // Add to error
                    errorCheckSum++;

                    // Mark visible mandatory check-boxes with errors
                    cboxArrowToError(activeChboxLaylist);
                }
            }

            /* Share data checkbox */
            if (!(shareData.isChecked())) {
                errorCheckSum++;
                Toast.makeText(context, "Please allow Plexus to share your data with other users",
                        Toast.LENGTH_LONG).show();
                //dMenuVerData.dismiss();
            }

            //Log.i("errorCheckSum", String.valueOf(errorCheckSum));
            //String tempString = selSpecWidget.getText().toString();
            if (!(errorCheckSum == 0)) {

                // Toast with visibility check
                if (!(toast.getView().isShown())) {
                    toast = Toast.makeText(context, "Please fix errors and try again",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {

                createDataVerDg();
                checkDataOnPress(emailField.getText().toString(), StringUtils.upperCase(regKeyField.
                        getText().toString()), null);
            }

            return true;
        }

        // About
        else if (id == R.id.adm_add_usr_about_txt) {

            //Toast.makeText(context, "About", Toast.LENGTH_SHORT).show();
            // Launch an about dialog
            About about = new About();
            about.launch(context);

            return true;
        }

        // Logout
        else if (id == R.id.adm_add_usr_logout_txt) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(AdminAddUser.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AdminAddUser.this.startActivity(mainIntent);
            AdminAddUser.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /* Create a spec selection dialog */
    private void createSelSpecDg(final android.support.v7.widget.AppCompatButton widget) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_select_spec, null);

        // Set the Progress Bar
        delSpecProgressB = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        delSpecProgressB.setIndeterminate(true);
        delSpecProgressB.setVisibility(View.INVISIBLE);

        // Set dialog buttons
        AppCompatButton cancelBtn = (AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        specRecyclerView = (android.support.v7.widget.RecyclerView) myDialogView.findViewById(R.id.spec_search_recycler_view);

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
        specParseList(delSpecProgressB, specRecyclerView, widget, userCatWg.getText().toString());

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
        final AppCompatButton cancelBtn = (AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

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

        subSpecRecyclerView = (android.support.v7.widget.RecyclerView) myDialogView.findViewById
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
        //dataVerPBar.setVisibility(View.VISIBLE);

        userSuccessTxt = (TextView) myDialogView.findViewById(R.id.text_success_reg);
        userSuccessTxt.setVisibility(View.GONE);

        dataVerTitle = (TextView) myDialogView.findViewById(R.id.verdata_dg_title);

        // Set up the OK button
        okBtn = (android.support.v7.widget.AppCompatButton)
                myDialogView.findViewById(R.id.dev_data_dg_btn);
        okBtn.setVisibility(View.GONE);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Redirect to login
                ParseUser.logOut();
                Intent mainIntent = new Intent(AdminAddUser.this, GeneralLogin.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                AdminAddUser.this.startActivity(mainIntent);
                finish();
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
        final AppCompatButton cancelBtn = (AppCompatButton) myDialogView.findViewById(R.id.btn_cancel_SOI_D);

        cancelBtn.setText("select");

        // RecyclerView
        final RecyclerView soiRv = (RecyclerView) myDialogView.findViewById(R.id.search_user_cat_rv);

        soiRv.setHasFixedSize(true);
        RecyclerView.LayoutManager tempLayManager = new LinearLayoutManager(this);
        soiRv.setLayoutManager(tempLayManager);
        soiRv.setEnabled(false);
        SearchUserSOIAdapter userCatAdapter = new SearchUserSOIAdapter(populateSOI(), context,
                CALLER, soiRv, cancelBtn, passStates);
        soiRv.setAdapter(userCatAdapter);

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

    /* User state of residency widget dialog */
    private void createSORDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_search_user_sor, null);

        // Set up the Cancel button
        final AppCompatButton cancelBtn = (AppCompatButton) myDialogView.findViewById(R.id.btn_cancel_SOR_D);

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

    // Parse reg-key check
    private void rtValidateRegKey(final String regKey, final Context context, final ProgressBar pbar) {

        pbar.setVisibility(View.VISIBLE);

        // Check for Internet
        if (DeviceConnection.testConnection(context)) {

            // Yes, Internet
            ParseQuery<ParseObject> checkSharedKeys = ParseQuery.getQuery("SharedKeys");
            checkSharedKeys.whereEqualTo("sharedKey", regKey);
            checkSharedKeys.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    if (e == null) {

                        pbar.setVisibility(View.GONE);
                        if (list.isEmpty()) {

                            // Bad news, reg key does not coincide with anything on the server
                            regKeyField.setError("Key doesn't exist. Please check the " +
                                    "spelling and try again.");

                            // Reset Pro. Info widgets (userCat, userSpec and userSubspec)
                            resetProInfo();

                            // Hide checkboxes
                            hideCheckBoxes();

                        } else {

                            // Reset Pro. Info widgets (userCat, userSpec and userSubspec)
                            resetProInfo();

                            tempUserCat = "error";

                            // Good news, reg key does coincide with a key on the server
                            // Auto-populate the User Cat widget
                            for (ParseObject temp : list) {
                                autoPopUsrCat(temp.getString("userCategory"));
                                tempUserCat = temp.getString("userCategory");
                            }

                            // Set sub-spec widget text to "none" if key is dental


                            // Show the checkboxes corresponding to the user cat >> Student <<
                            if (tempUserCat.toString().equals("Medical Student")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layNeedClerkship.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedClerkship);
                                layNeedResidency.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedResidency);
                                layNeedFellowship.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedFellowship);
                                layNeedJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedJob);
                                layNeedFinHelp.setVisibility(View.VISIBLE);
                                soiLayout.setVisibility(View.VISIBLE);

                                // Invisible
                                layOfferClerkship.setVisibility(View.GONE);
                                layOfferResidency.setVisibility(View.GONE);
                                layOfferFellowship.setVisibility(View.GONE);
                                layOfferJob.setVisibility(View.GONE);
                                layOfferAssist.setVisibility(View.GONE);

                                // Refresh checkbox card
                                cboxCard.refreshDrawableState();

                                // Hide the 'Institution Name' edit-text field
                                instLayout.setVisibility(View.GONE);

                            } else if (tempUserCat.toString().equals("Medical Physician")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layOfferClerkship.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layOfferClerkship);
                                layOfferResidency.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layOfferResidency);
                                layOfferFellowship.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layOfferFellowship);
                                layOfferJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layOfferJob);
                                layOfferAssist.setVisibility(View.VISIBLE);

                                // Invisible
                                layNeedClerkship.setVisibility(View.GONE);
                                layNeedResidency.setVisibility(View.GONE);
                                layNeedFellowship.setVisibility(View.GONE);
                                layNeedJob.setVisibility(View.GONE);
                                layNeedFinHelp.setVisibility(View.GONE);
                                soiLayout.setVisibility(View.GONE);

                                // Refresh checkbox card
                                cboxCard.refreshDrawableState();

                                // Show the institution name edit field for the physician
                                instLayout.setVisibility(View.VISIBLE);

                            } else if (tempUserCat.toString().equals("Medical Resident")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layNeedFellowship.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedFellowship);
                                layNeedJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedJob);
                                layNeedFinHelp.setVisibility(View.VISIBLE);
                                soiLayout.setVisibility(View.VISIBLE);

                                // Invisible
                                layNeedClerkship.setVisibility(View.GONE);
                                layNeedResidency.setVisibility(View.GONE);
                                layOfferClerkship.setVisibility(View.GONE);
                                layOfferResidency.setVisibility(View.GONE);
                                layOfferFellowship.setVisibility(View.GONE);
                                layOfferJob.setVisibility(View.GONE);
                                layOfferAssist.setVisibility(View.GONE);

                                // Refresh checkbox card
                                cboxCard.refreshDrawableState();

                                // Hide the 'Institution Name' edit-text field
                                instLayout.setVisibility(View.GONE);

                            } else if (tempUserCat.toString().equals("Medical Fellow")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layNeedJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedJob);
                                layNeedFinHelp.setVisibility(View.VISIBLE);
                                soiLayout.setVisibility(View.VISIBLE);

                                // Invisible
                                layNeedClerkship.setVisibility(View.GONE);
                                layNeedResidency.setVisibility(View.GONE);
                                layOfferClerkship.setVisibility(View.GONE);
                                layOfferResidency.setVisibility(View.GONE);
                                layOfferFellowship.setVisibility(View.GONE);
                                layNeedFellowship.setVisibility(View.GONE);
                                layOfferJob.setVisibility(View.GONE);
                                layOfferAssist.setVisibility(View.GONE);

                                // Hide the 'Institution Name' edit-text field
                                instLayout.setVisibility(View.GONE);

                            } else if (tempUserCat.toString().equals("Dental Student")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layNeedResidency.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedResidency);
                                layNeedJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedJob);
                                layNeedFinHelp.setVisibility(View.VISIBLE);
                                soiLayout.setVisibility(View.VISIBLE);

                                // Invisible
                                layOfferClerkship.setVisibility(View.GONE);
                                layNeedClerkship.setVisibility(View.GONE);
                                layOfferResidency.setVisibility(View.GONE);
                                layOfferFellowship.setVisibility(View.GONE);
                                layNeedFellowship.setVisibility(View.GONE);
                                layOfferJob.setVisibility(View.GONE);
                                layOfferAssist.setVisibility(View.GONE);

                                // Refresh checkbox card
                                cboxCard.refreshDrawableState();

                                // Hide the 'Institution Name' edit-text field
                                instLayout.setVisibility(View.GONE);

                                // Set "none" to sub-spec widget
                                selSubspecWidget.setText(NONE);
                                selSubspecWidget.setEnabled(false);

                            } else if (tempUserCat.toString().equals("Dental Resident")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layNeedJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layNeedJob);
                                layNeedFinHelp.setVisibility(View.VISIBLE);
                                soiLayout.setVisibility(View.VISIBLE);

                                // Invisible
                                layNeedClerkship.setVisibility(View.GONE);
                                layNeedResidency.setVisibility(View.GONE);
                                layOfferClerkship.setVisibility(View.GONE);
                                layOfferResidency.setVisibility(View.GONE);
                                layOfferFellowship.setVisibility(View.GONE);
                                layNeedFellowship.setVisibility(View.GONE);
                                layOfferJob.setVisibility(View.GONE);
                                layOfferAssist.setVisibility(View.GONE);

                                // Refresh checkbox card
                                cboxCard.refreshDrawableState();

                                // Hide the 'Institution Name' edit-text field
                                instLayout.setVisibility(View.GONE);

                                // Set "none" to sub-spec widget
                                selSubspecWidget.setText(NONE);
                                selSubspecWidget.setEnabled(false);

                            } else if (tempUserCat.toString().equals("Dental Physician")) {

                                // Clear active checkboxs list & add visible checkboxes in it
                                activeChboxLaylist.clear();

                                // Visible
                                layOfferResidency.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layOfferResidency);
                                layOfferJob.setVisibility(View.VISIBLE);
                                activeChboxLaylist.add(layOfferJob);

                                // Invisible
                                layNeedClerkship.setVisibility(View.GONE);
                                layOfferClerkship.setVisibility(View.GONE);
                                layNeedResidency.setVisibility(View.GONE);
                                layNeedFellowship.setVisibility(View.GONE);
                                layOfferFellowship.setVisibility(View.GONE);
                                layOfferAssist.setVisibility(View.GONE);
                                layNeedJob.setVisibility(View.GONE);
                                layNeedFinHelp.setVisibility(View.GONE);
                                soiLayout.setVisibility(View.GONE);

                                // Refresh checkbox card
                                cboxCard.refreshDrawableState();

                                // Show the institution name edit field for the physician
                                instLayout.setVisibility(View.VISIBLE);

                                // Set "none" to sub-spec widget
                                selSubspecWidget.setText(NONE);
                                selSubspecWidget.setEnabled(false);
                            }
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

    // Parse check on reg btn
    private void checkDataOnPress(final String email, final String regKey, final String usrCat) { //todo: fix this last param
        // Instantiate this list to store each key's user category
        keyUsrCatList = new ArrayList<>();

        if (DeviceConnection.testConnection(context)) {

            //1. Check email
            // Query creation and implementation
            ParseQuery<ParseUser> checkEmailQuery = ParseUser.getQuery();
            checkEmailQuery.whereEqualTo("email", email);
            checkEmailQuery.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if (e == null) {

                        if (!(i >= 1)) {
                            // Great, email is unique
                            //2. Check key
                            ParseQuery<ParseObject> checkSharedKeys = ParseQuery.getQuery("SharedKeys");
                            checkSharedKeys.whereEqualTo("sharedKey", regKey);
                            checkSharedKeys.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if (e == null) {

                                        if (!(list.isEmpty())) {
                                            // Good news, regkey does exist
                                            for (ParseObject temp : list) {
                                                keyUsrCatList.add(temp.getString("userCategory").
                                                        toString());
                                            }

                                            //3. Check if key belongs to chosen user cat
                                            if (keyUsrCatList.get(0).toString().equals(userCatWg.
                                                    getText().toString())) {

                                                // Change color for user cat spinner's textview
                                                userCatTextView.setTextColor(getResources().
                                                        getColor(R.color.colorPrimary));

                                                //4. Call addParseUser()
                                                addParseUser();


                                            } else {

                                                // Key and userCat spinner don't correspond
                                                dMenuVerData.dismiss();
                                                // Toast with visibility check
                                                if (!(toast.getView().isShown())) {
                                                    toast = Toast.makeText(context, "Please fix " +
                                                                    "errors and try again",
                                                            Toast.LENGTH_LONG);
                                                    toast.show();
                                                }

                                                userCatTextView.setTextColor(Color.RED);
                                                userCatTextView.setTypeface(null, Typeface.BOLD);
                                                // Toast with visibility check
                                                Toast.makeText(context, "Registration Key doesn't allow " +
                                                                "this User Category",
                                                        Toast.LENGTH_LONG).show();


                                            }

                                        } else {
                                            // Bad news, regkey does not exist
                                            dMenuVerData.dismiss();
                                            regKeyField.setError("Registration Key doesn't exist. " +
                                                    "Please check the spelling and try again");
                                        }

                                    } else {
                                        // Hide dialog
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

                        } else {

                            dMenuVerData.dismiss();
                            emailField.setError("Email already in use");
                        }

                    } else {

                        // Hide dialog
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
        } else {
            // No, Internet
            // Show network error
            dMenuVerData.dismiss();
            Toast.makeText(context, "Network error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

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
    public List<Specialty> specParseList(final ProgressBar progressBar, final android.support.v7
            .widget.RecyclerView rw, final android.support.v7.widget.AppCompatButton widget,
                                         final String userCat) {

        final List<Specialty> tempRetList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        // Query the Spec class
        ParseQuery query = new ParseQuery("Spec");
        query.orderByAscending("specName");

        // Check what user category spec should Parse bring back (medical vs dental)
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
                               final android.support.v7.widget.AppCompatButton widget) {

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
                                dMenuSelSubspec, widget, SUBCALLER);

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
    private void addParseUser() {
        if (DeviceConnection.testConnection(context)) {

            //1. Collect data from all fields & sanitize it
            /* Email */
            String tempEmailInput = emailField.getText().toString().trim();
            int index = tempEmailInput.indexOf('@');
            String tempDomain = StringUtils.lowerCase(tempEmailInput.substring(index + 1,
                    tempEmailInput.length()));
            String tempUserName = tempEmailInput.substring(0, index + 1);
            String finalEmail = tempUserName.concat(tempDomain); // < -- add to Parse

            /* Password */
            String tempPassInput = passwordField.getText().toString().trim(); // < -- add to Parse

            /* Repeat Password */

            /* Registration Key */
            final String tempRegKeyInput = regKeyField.getText().toString(); // < -- add to Parse

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
            String tempInstInput = WordUtils.capitalizeFully(instField.getText().toString().trim()); // < -- add to Parse

            /* State */
            String tempStateInput = sorWidget.getText().toString(); // < -- add to Parse

            /* User Category */
            String tempCatInput = userCatWg.getText().toString(); // < -- add to Parse

            /* User Specialty */
            String tempSpecInput = selSpecWidget.getText().toString(); // < -- add to Parse

            /* User Subspecialty */
            String tempSubspecInput = selSubspecWidget.getText().toString(); // < -- add to Parse

            /* Start Date */
            //String tempStartDate = startingDate.getSelectedItem().toString(); // < -- add to Parse

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

            //2. Create user as a Parse user
            final ParseUser user = new ParseUser();

            user.setUsername(finalEmail);
            user.setPassword(tempPassInput);
            user.setEmail(finalEmail);

            user.put("userCat", "user");            // change this to admin if needed
            user.put("regKey", tempRegKeyInput);
            user.put("userSpecId", specId);
            user.put("firstName", tempFNameInput);
            user.put("lastName", tempLNameInput);
            user.put("address", tempAddressInput);
            user.put("city", tempCityInput);
            user.put("zip", tempZipInput);
            user.put("instName", tempInstInput);
            user.put("state", tempStateInput);
            user.put("userProff", tempCatInput);
            user.put("userSpec", tempSpecInput);
            user.put("userSubspec", tempSubspecInput);

            // SOI
            if (passStates.contains("Alabama")) {
                user.put("soi_alabama", true);
            } else {
                user.put("soi_alabama", false);
            }
            if (passStates.contains("Florida")) {
                user.put("soi_florida", true);
            } else {
                user.put("soi_florida", false);
            }
            if (passStates.contains("Georgia")) {
                user.put("soi_georgia", true);
            } else {
                user.put("soi_georgia", false);
            }
            if (passStates.contains("Kentucky")) {
                user.put("soi_kentucky", true);
            } else {
                user.put("soi_kentucky", false);
            }
            if (passStates.contains("Missouri")) {
                user.put("soi_missouri", true);
            } else {
                user.put("soi_missouri", false);
            }
            if (passStates.contains("North Carolina")) {
                user.put("soi_nc", true);
            } else {
                user.put("soi_nc", false);
            }
            if (passStates.contains("South Carolina")) {
                user.put("soi_sc", true);
            } else {
                user.put("soi_sc", false);
            }
            if (passStates.contains("Tennessee")) {
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

            if (photoCtrlVar) {
                // Dynamically generated photo name
                String tempPhotoName = PhotoProcessor.photoFullName(tempFNameInput, tempLNameInput);

                // Create a byteArrayStream, compress the bmp and save it to a bytearray
                // Then save the bytearray to an object of type ParseFile
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                userPicBmpSel.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();

                final ParseFile photoParseFile = new ParseFile(tempPhotoName, byteArray);

                photoParseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            user.put("profilePicture", photoParseFile);

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {

                                    if (e == null) {

                                        // Link user to the 'User' role
                                        ParseQuery<ParseRole> roleQuery = ParseRole.getQuery();
                                        roleQuery.whereEqualTo("name", "User");
                                        roleQuery.getFirstInBackground(new GetCallback<ParseRole>() {
                                            @Override
                                            public void done(ParseRole parseRole, ParseException e) {

                                                if (e == null) {

                                                    parseRole.getUsers().add(user);
                                                    parseRole.saveInBackground(); //don't forget to save it


                                                    //TODO: 4. Delete reg key used for this user
                                                    ParseQuery<ParseObject> delKeyQuery = ParseQuery.getQuery("SharedKeys");
                                                    delKeyQuery.whereEqualTo("sharedKey", tempRegKeyInput);

                                                    delKeyQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                                        @Override
                                                        public void done(ParseObject parseObject, ParseException e) {
                                                            if (e == null) {

                                                                parseObject.deleteInBackground(new DeleteCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {
                                                                        if (e != null) {

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
                                                }

                                                // Error on Role ACL
                                                else {

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

                                        //1. Hide the progress bar
                                        dataVerPBar.setVisibility(View.GONE);

                                        //2. Show okBtn & successTxt
                                        okBtn.setVisibility(View.VISIBLE);
                                        userSuccessTxt.setVisibility(View.VISIBLE);
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

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {

                        if (e == null) {

                            // Link user to the 'User' role
                            ParseQuery<ParseRole> roleQuery = ParseRole.getQuery();
                            roleQuery.whereEqualTo("name", "User");
                            roleQuery.getFirstInBackground(new GetCallback<ParseRole>() {
                                @Override
                                public void done(ParseRole parseRole, ParseException e) {

                                    if (e == null) {

                                        parseRole.getUsers().add(user);
                                        parseRole.saveInBackground(); //don't forget to save it


                                        //TODO: 4. Delete reg key used for this user
                                        ParseQuery<ParseObject> delKeyQuery = ParseQuery.getQuery("SharedKeys");
                                        delKeyQuery.whereEqualTo("sharedKey", tempRegKeyInput);

                                        delKeyQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if (e == null) {

                                                    parseObject.deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e != null) {

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
                                    }

                                    // Error on Role ACL
                                    else {

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

                            //1. Hide the progress bar
                            dataVerPBar.setVisibility(View.GONE);

                            //2. Show okBtn & successTxt
                            okBtn.setVisibility(View.VISIBLE);
                            userSuccessTxt.setVisibility(View.VISIBLE);
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
            }

        } else {
            //Set dialog progress bar visibility off
            dMenuVerData.dismiss();
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    /* Auto-populate the UserCat widget */
    private void autoPopUsrCat(final String userCat) {
        userCatWg.setText(userCat);
    }

    /* Custom scrollOnView */
    private final void focusOnView(final UsrRegScrollV scrollV, final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollV.scrollTo(0, view.getBottom());
            }
        });
    }

    /* Reset all visible checkboxes (except need financial help and agree to terms & conditions) */
    private void hideCheckBoxes() {

        // Clear active checkboxs list & add visible checkboxes in it
        activeChboxLaylist.clear();

        // Visible (none)

        // Invisible
        layNeedClerkship.setVisibility(View.GONE);
        layNeedResidency.setVisibility(View.GONE);
        layNeedFellowship.setVisibility(View.GONE);
        layNeedJob.setVisibility(View.GONE);
        layOfferClerkship.setVisibility(View.GONE);
        layOfferResidency.setVisibility(View.GONE);
        layOfferFellowship.setVisibility(View.GONE);
        layOfferJob.setVisibility(View.GONE);
        layOfferAssist.setVisibility(View.GONE);

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

    /* Method checks if there is at least one mandatory check-box visible */
    private boolean cBoxIsVisible(final List<RelativeLayout> list) {

        boolean listIsEmpty = true; // by default is empty (good thing)

        // Iterate through the list with visible checkbox layouts
        for (RelativeLayout linearLayout : list) {

            // Check if any of the mandatory checkboxes has been selected
            int children = linearLayout.getChildCount();
            for (int i = 0; i < children; ++i) {
                AppCompatCheckBox tempCheckBox = null;
                tempCheckBox = (AppCompatCheckBox) linearLayout.getChildAt(1);

                if (tempCheckBox.isChecked()) {

                    // If there is a selected checkbox exit
                    listIsEmpty = false; // bad thing
                    return listIsEmpty;
                }
            }
        }

        return listIsEmpty;
    }

    /* Method resets userCat + userSpec + userSubspec widgets */
    private void resetProInfo() {

        // Reset user cat, spec & subspec widgets
        userCatWg.setText("auto-populated");
        selSpecWidget.setText(SELECT_TXT);

        selSubspecWidget.setText(SELECT_TXT);

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
        tempRetList.add("Mississippi");
        tempRetList.add("North Carolina");
        tempRetList.add("South Carolina");
        tempRetList.add("Tennessee");


        return tempRetList;
    }

    /* BroadcastReceiver used for the SOI adapter */
    private BroadcastReceiver soiRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            passStates = intent.getStringArrayListExtra("textValue");
            //passPos = intent.getIntegerArrayListExtra("posValue");

            // Cancel dialog
            dSOI.dismiss();

            // On single SOI select show the name of the state, on multi-select show 'multiple',
            // On no selection show 'select'
            if (passStates.size() == 1) {
                soiWidget.setText(passStates.get(0));
                soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            } else if (passStates.size() > 1) {
                soiWidget.setText(MULTI_STATE);
                soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            } else {
                soiWidget.setText(SELECT_TXT);
                soiWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));
            }
        }
    };

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

    /* BroadcastReceiver used for the admin spec widget */
    private BroadcastReceiver admin_specRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            selSpecWidget.setText(SELECT_TXT);

            selSpecWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));

            if (intent != null) {

                // Get spec name & spec id from intent
                specName = intent.getStringExtra("textValue");
                specId = intent.getStringExtra("textId");

                selSpecWidget.setText(specName);

                // Handle spec "undecided" and "other" for subspec
                if (specName.toLowerCase().equals("other")) {

                    // Set subspecWidget to "other"
                    selSubspecWidget.setText("none");
                    selSubspecWidget.setEnabled(false);

                } else if (specName.toLowerCase().equals("undecided")) {

                    // Set subspecWidget to "undecided"
                    selSubspecWidget.setText("none");
                    selSubspecWidget.setEnabled(false);

                } else {

                    // Re-enable subspec widget on "non-other" or "non-undecided"
                    selSubspecWidget.setEnabled(true);
                }

            }
        }
    };

    /* BroadcastReceiver used for the AddUsr subspec widget */
    private BroadcastReceiver admin_subSpecRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Reset subspec widget color - recovering from error
            selSubspecWidget.setBackground(getResources().getDrawable(R.drawable.buttonselector));

            subspecName = intent.getStringExtra("textValue");

            // Set error message on widget if intent can't deliver
            if ((!subspecName.isEmpty()) || (subspecName != null)) {

                selSubspecWidget.setText(subspecName);

            }
        }
    };

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

    // Compress and resize for thumbnail
    private Bitmap compressForThumbnail(Bitmap fullSize) {

        // New resized bitmap
        Bitmap thumbnail = Bitmap.createScaledBitmap(fullSize, fullSize.getWidth() / 3,
                fullSize.getHeight() / 3, true);

        return thumbnail;
    }
}

