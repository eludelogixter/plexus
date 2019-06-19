package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UserMain extends AppCompatActivity {

    private Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView subSpecRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String MEDICAL_STRING = "Medical";
    private static String DENTAL_STRING = "Dental";
    private static String PHYSICIAN_STRING = "Physician";
    public static String DEF_REQ_W_VALUE = "tap to select";
    public static String CALLER = "search";
    public static String ALL_TXT = "all";
    private static String RESIDENT_STRING = "Resident";
    private static String FELLOW_STRING = "Fellow";
    private static String STUDENT_STRING = "Student";
    private AlertDialog dMenuSearchUserSOI;
    private AlertDialog dMenuSearchUserI;
    private AlertDialog dMenuSearchUserSD;
    private AlertDialog dMenuSearchUsrSP;
    private AlertDialog dMenuSerchUsrSS;
    private AppCompatButton soiDCancelBtn;
    private AppCompatButton uPosDCancelBtn;
    private AppCompatButton SDDCancelBtn;
    private Button searchUsrSOI_W;
    private Button searchUsrSD_W;
    public static Button searchUsrPos_W;
    public static Button searchUsrS_W;
    public static Button searchUsrSS_W;
    private String[] userCats;
    private String[] userPosition = new String[] {"Clerkship", "Residency", "Fellowship", "Job"};
    private SpecAdapter specAdapter;
    private SubspecAdapter subspecAdapter;
    private String userSubCat;
    private String rowId;
    private RelativeLayout searchUsrSubspecL;
    private RelativeLayout emptySearchListL;
    private Toast toast;
    private Integer reqWCounter;
    private ParseUser tempUsrCheck;
    private TextView soiTextView;
    private Boolean physicianCheck;
    private RelativeLayout userMainAllLay;
    private ProgressBar progressBar;
    private MenuItem favBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Check Parse userProff & set UI logic for search widgets
        tempUsrCheck = ParseUser.getCurrentUser();

        // Set the login context
        context = this;

        // Hide the keyboard as as soon as the activity is loaded
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set toolbar
        Toolbar myTb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myTb);
        getSupportActionBar().setTitle("Search Users");

        if ((tempUsrCheck != null) && (tempUsrCheck.getString("userCat").equals("admin"))) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        }


        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEnabled(false);

        // Layout of all elements
        userMainAllLay = (RelativeLayout)findViewById(R.id.user_main_all_lay);

        // Instantiate a toast for reuse
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG);

        // Instantiate the emptySearchListL
        emptySearchListL = (RelativeLayout)findViewById(R.id.empty_searchlist_feedback);

        // Target the slider
        final SlidingDrawer mySlider = (SlidingDrawer)findViewById(R.id.search_slider);
        mySlider.bringToFront(); // bring slider over recyclerview

        // Open Drawer listner
        mySlider.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                mySlider.setClickable(true);
            }
        });

        // Close Drawer listner
        mySlider.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                mySlider.setClickable(false);
            }
        });

        // Target handle's text
        final TextView handleTxt = (TextView)findViewById(R.id.handle_text);

        // Target the progress bar
        progressBar = (ProgressBar)findViewById(R.id.search_progressbar);

        // Target search button
        final Button searchButton = (Button)findViewById(R.id.search_user_button);

        // Lint keeps saying method .setOnClickListener on this object can return null
        if (searchButton != null) {

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int tempRtrn = checkDataOnWdg();
                    if (tempRtrn == 0) {

                        // Hide layout
                        userMainAllLay.setVisibility(View.GONE);

                        // Show pBar
                        progressBar.setVisibility(View.VISIBLE);

                        // Call Parse
                        srcParseUsers(progressBar);
                        mySlider.close();

                    }

                    checkDataOnWdg();

                }
            });
        }


        // Target search drawer widgets & needed layouts
        searchUsrSOI_W = (Button)findViewById(R.id.search_slider_state_interest_widget);
        searchUsrSOI_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createSearchUsrSOIDg();
            }
        });

        searchUsrSD_W = (Button)findViewById(R.id.search_slider_start_date_widget);
        searchUsrSD_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createSearchUsrSDDg();
            }
        });

        searchUsrPos_W = (Button)findViewById(R.id.search_slider_userinterest_widget);
        searchUsrPos_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Limit position to currently logged in user category (i.e: if the user is a
                 * fellow don't show residencies or clerkship */

                // Medical subCat
                if (userSubCat.toString().equals(MEDICAL_STRING)) {
                    if (tempUsrCheck.get("userProff").toString().contains(STUDENT_STRING)) {
                        userPosition = new String[]{"Clerkship", "Residency", "Fellowship", "Job"};
                    } else if (tempUsrCheck.get("userProff").toString().contains(RESIDENT_STRING)) {
                        userPosition = new String[]{"Residency", "Fellowship", "Job"};
                    } else if (tempUsrCheck.get("userProff").toString().contains(FELLOW_STRING)) {
                        userPosition = new String[]{"Fellowship", "Job"};
                    }
                }

                // Dental subCat affects the userPositions because dental does not have a clerkship
                // or a fellowship
                else {
                    // Both dental students & residents can only find either another residency
                    // position or a job
                    userPosition = new String[]{"Residency", "Job"};

                }

                createSearchUsrPositionDg();
            }
        });

        searchUsrS_W = (Button)findViewById(R.id.search_slider_userspec_widget);
        searchUsrS_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUserSpecDg(searchUsrS_W);
            }
        });

        searchUsrSS_W = (Button)findViewById(R.id.search_slider_usersubspec_widget);
        searchUsrSS_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // No subspec selection without spec value
                if (searchUsrS_W.getText().toString().equals(DEF_REQ_W_VALUE)) {

                    if (!(toast.getView().isShown())) {
                        toast = Toast.makeText(context, "'Specialty' is missing",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else if (searchUsrS_W.getText().toString().equals("Other") ||
                          searchUsrS_W.getText().toString().equals("Undecided")) {

                    if (!(toast.getView().isShown())) {
                        toast = Toast.makeText(context, "''Specialty' selected can not " +
                                "have 'Subspecialties'", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else {

                    createUserSubspecDg();
                }


            }
        });

        // Instantiate user subspec layout
        searchUsrSubspecL = (RelativeLayout)findViewById(R.id.search_slider_usersubspec_layout);

        // Instantiate soiTextView
        soiTextView = (TextView)findViewById(R.id.search_slider_state_interest_txt);


        /******************** User category check ********************/

        // Medical Check
        if(tempUsrCheck.get("userProff").toString().contains(MEDICAL_STRING)) {

            userSubCat = MEDICAL_STRING;

            // Show subspec layout
            searchUsrSubspecL.setVisibility(View.VISIBLE);

            // Physician Check
            if(tempUsrCheck.get("userProff").toString().contains(PHYSICIAN_STRING)) {

                // Initialize medical user types for physicians (practices)
                userCats = new String[] {"Student", "Resident", "Fellow"};

                // Change SOI TextView to read 'State' instead of 'State of Interest'
                soiTextView.setText("State:");

                // Physician state check
                physicianCheck = true;
            }

            // Non-physician check
            else {

                // Initialize medical user type for non-physicians
                userCats = new String[] {"Student", "Resident", "Fellow", "Physician"};

                // Change SOI TextView to read 'State of Interest' instead of 'State'
                soiTextView.setText("State of Interest:");

                // Physician state check
                physicianCheck = false;
            }
        }

        // Dental Check
        else if (tempUsrCheck.get("userProff").toString().contains(DENTAL_STRING)) {

            userSubCat = DENTAL_STRING;

            // Hide subspec layout
            searchUsrSubspecL.setVisibility(View.GONE);

            // Physician Check
            if (tempUsrCheck.get("userProff").toString().contains(PHYSICIAN_STRING)) {

                // Initialize dental user types
                userCats = new String[] {"Student", "Resident"};

                // Change SOI TextView to read 'State' instead of 'State of Interest'
                soiTextView.setText("State:");

                // Physician state check
                physicianCheck = true;
            }

            // Non-physician check
            else {

                // Initialize dental user type for non-physicians
                userCats = new String[] {"Student", "Resident", "Physician"};

                // Change SOI TextView to read 'State of Interest' instead of 'State'
                soiTextView.setText("State of Interest:");

                // Physician state check
                physicianCheck = false;
            }
        }



    }

    @Override
    public void onResume() {

        // Receive data from adapters via broadcasters
        registerReceiver(searchUserSOIRcv, new IntentFilter("u_SOI"));
        registerReceiver(searchUserSDRcv, new IntentFilter("u_SD"));
        registerReceiver(searchUserIRcv, new IntentFilter("u_I"));
        registerReceiver(searchUserSpecRcv, new IntentFilter("u_S"));
        registerReceiver(searchUserSubspecRcv, new IntentFilter("u_SS"));

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {

        /* try/catch is needed to avoid crashing the app (API problem) */
        try {
            unregisterReceiver(searchUserSOIRcv);
            unregisterReceiver(searchUserSDRcv);
            unregisterReceiver(searchUserIRcv);
            unregisterReceiver(searchUserSpecRcv);
            unregisterReceiver(searchUserSubspecRcv);
        } catch (IllegalArgumentException e) {

        }


        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_main, menu);

        // If activity is accessed from admin hide fav menu icon
        favBtn = menu.findItem(R.id.administration_main_action_fav);
        if (tempUsrCheck.getString("userCat").equals("admin")) {
            favBtn.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.administration_main_action_fav) {
            Intent favUsrIntent = new Intent(UserMain.this, FavUsers.class);
            UserMain.this.startActivity(favUsrIntent);
            //UserMain.this.finish();

            return true;
        }

        // About
        if (id == R.id.administration_main_action_about) {

            //Toast.makeText(this, "About", Toast.LENGTH_LONG).show();
            // Launch an about dialog
            About about = new About();
            about.launch(context);

            return true;


        // My Profile
        } else if (id == R.id.administration_main_action_profile) {

            // Redirect to the AdminMain
            Intent myProfIntent = new Intent(UserMain.this, UserProfile.class);
            myProfIntent.putExtra("myProfile", true);
            UserMain.this.startActivity(myProfIntent);
            //UserMain.this.finish();

            return true;

        // Logout
        } else if (id == R.id.administration_main_action_logout) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(UserMain.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            UserMain.this.startActivity(mainIntent);
            UserMain.this.finish();

            return true;
        }

        // Return arrow
        else if (id == android.R.id.home) {

            onBackPressed();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    /************************ Dialog Methods *************************/

    /* User interest search widget dialog */
    private void createSearchUsrPositionDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_search_user_i, null);

        // Set up the Cancel button
        uPosDCancelBtn = (android.support.v7.widget.AppCompatButton)
                myDialogView.findViewById(R.id.cancel_user_cat_btn);

        // RecyclerView
        final RecyclerView userCatRv = (RecyclerView)myDialogView.findViewById(R.id.search_user_cat_rv);
        userCatRv.setHasFixedSize(true);
        RecyclerView.LayoutManager tempLayManager = new LinearLayoutManager(this);
        userCatRv.setLayoutManager(tempLayManager);
        userCatRv.setEnabled(false);
        ArrayList<String> userIntrestList = new ArrayList<>();
        userIntrestList.addAll(Arrays.asList(userPosition));
        SearchUserIAdapter userCatAdapter = new SearchUserIAdapter(userIntrestList, context);
        userCatRv.setAdapter(userCatAdapter);

        uPosDCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cancel logic goes here
                dMenuSearchUserI.dismiss();
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSearchUserI = builder.create();
        dMenuSearchUserI.setCanceledOnTouchOutside(false);
        dMenuSearchUserI.show();

    }

    /* User state of Interest widget dialog */
    private void createSearchUsrSOIDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_search_user_soi, null);

        // Set up the Cancel button
        soiDCancelBtn = (android.support.v7.widget.AppCompatButton)
                myDialogView.findViewById(R.id.btn_cancel_SOI_D);

        // RecyclerView
        final RecyclerView userCatRv = (RecyclerView)myDialogView.findViewById(R.id.search_user_cat_rv);
        userCatRv.setHasFixedSize(true);
        RecyclerView.LayoutManager tempLayManager = new LinearLayoutManager(this);
        userCatRv.setLayoutManager(tempLayManager);
        userCatRv.setEnabled(false);
        SearchUserSOIAdapter userCatAdapter = new SearchUserSOIAdapter(populateSOI(), context, CALLER,
                userCatRv, null, null);
        userCatRv.setAdapter(userCatAdapter);

        soiDCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cancel logic goes here
                dMenuSearchUserSOI.dismiss();
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSearchUserSOI = builder.create();
        dMenuSearchUserSOI.setCanceledOnTouchOutside(false);
        dMenuSearchUserSOI.show();
    }

    /* User start date widget dialog */
    private void createSearchUsrSDDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_search_user_sd, null);;

        // Set up the Cancel button
        SDDCancelBtn = (android.support.v7.widget.AppCompatButton)
                myDialogView.findViewById(R.id.cancel_user_cat_btn);

        // RecyclerView
        final RecyclerView userCatRv = (RecyclerView)myDialogView.findViewById(R.id.search_user_cat_rv);
        userCatRv.setHasFixedSize(true);
        RecyclerView.LayoutManager tempLayManager = new LinearLayoutManager(this);
        userCatRv.setLayoutManager(tempLayManager);
        userCatRv.setEnabled(false);
        SearchUserSDAdapter userCatAdapter = new SearchUserSDAdapter(populateSD(), context);
        userCatRv.setAdapter(userCatAdapter);

        SDDCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cancel logic goes here
                dMenuSearchUserSD.dismiss();
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSearchUserSD = builder.create();
        dMenuSearchUserSD.setCanceledOnTouchOutside(false);
        dMenuSearchUserSD.show();
    }

    /* User Specialty widget dialog */
    private void createUserSpecDg(final Button widget) {

        // Local variables
        ProgressBar dUsrSpecProgressB;

        final RecyclerView specRecyclerView;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_select_spec, null);

        // Set the Progress Bar
        dUsrSpecProgressB = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        dUsrSpecProgressB.setIndeterminate(true);
        dUsrSpecProgressB.setVisibility(View.INVISIBLE);

        // Set dialog buttons
        android.support.v7.widget.AppCompatButton cancelBtn = (android.support.v7.widget.
                AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Disable subspec widget
                //selSubspecWidget.setText("select"); //todo reset subspec here

                // dismiss dialog
                dMenuSearchUsrSP.dismiss();
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
        specParseList(dUsrSpecProgressB, specRecyclerView, userSubCat);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSearchUsrSP = builder.create();
        dMenuSearchUsrSP.setCanceledOnTouchOutside(false);
        dMenuSearchUsrSP.show();
    }

    /* User Subspec widget dialog */
    private void createUserSubspecDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_select_subspec, null);

        ProgressBar searchSubspecPB;

        // Set the Progress Bar
        searchSubspecPB = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        searchSubspecPB.setIndeterminate(true);
        searchSubspecPB.setVisibility(View.INVISIBLE);

        // Set dialog buttons
        android.support.v7.widget.AppCompatButton cancelBtn = (android.support.v7.widget.
                AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // dismiss dialog
                dMenuSerchUsrSS.dismiss();
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
        selectSubspec(rowId, searchSubspecPB, searchUsrSS_W);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSerchUsrSS = builder.create();
        dMenuSerchUsrSS.setCanceledOnTouchOutside(false);
        dMenuSerchUsrSS.show();

    }



        /*****************************************************************/

    // Populates the state of interest widget
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

    // Populate start-in date widget
    private ArrayList<String> populateSD() {

        // Local ArrayAdapter
        ArrayList<String> tempArray = new ArrayList<String>();

        // 1. Get the current year and convert it into an int so you can add + 1 to it 3 times
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // 2. Populate the array with 4 year entries

        for (int i = 0; i < 6; i++) {

            if (i == 0) {
                tempArray.add("All");
                tempArray.add(String.valueOf(year));
            } else {
                year++;
                tempArray.add(String.valueOf(year));
            }

        }

        // 3. Return updated array
        return tempArray;
    }

    /* Check required widgets for selection & returns the number of widget errors */
    private Integer checkDataOnWdg() {

        // Instantiate & reset widget error counter
        reqWCounter = 0;

        //1. Position widget
        if (searchUsrPos_W.getText().toString().equals(DEF_REQ_W_VALUE)) {

            // Increment reqWCounter
            reqWCounter++;

            // UI feedback
            searchUsrPos_W.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));
        }

        //2. Specialty widget
        if (searchUsrS_W.getText().toString().equals(DEF_REQ_W_VALUE)) {

            // Increment reqWCounter
            reqWCounter++;

            // UI feedback
            searchUsrS_W.setBackground(getResources().getDrawable(R.drawable.buttonselector_red));
        }

        if (reqWCounter != 0) {

            if (!(toast.getView().isShown())) {
                toast = Toast.makeText(context, "Select a value for [ "+String.valueOf(reqWCounter)+" ] required filter(s)",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        return reqWCounter;
    }

    /***************** Adapter Receivers ***************/

    private BroadcastReceiver searchUserSOIRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            String value = i.getStringExtra("textValue");
            searchUsrSOI_W.setText(value);
            dMenuSearchUserSOI.dismiss();

        }
    };

    private BroadcastReceiver searchUserSDRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            String value = i.getStringExtra("textValue");
            searchUsrSD_W.setText(value);
            dMenuSearchUserSD.dismiss();
        }
    };

    private BroadcastReceiver searchUserIRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            String value = i.getStringExtra("textValue");
            searchUsrPos_W.setText(value);
            searchUsrPos_W.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            dMenuSearchUserI.dismiss();
        }
    };

    private BroadcastReceiver searchUserSpecRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            String value = i.getStringExtra("textValue");
            rowId = i.getStringExtra("textId");
            searchUsrS_W.setText(value);

            // Reset caller widget's color
            searchUsrS_W.setBackground(getResources().getDrawable(R.drawable.buttonselector));
            searchUsrSS_W.setText(UserMain.ALL_TXT);
            dMenuSearchUsrSP.dismiss();
        }
    };

    private BroadcastReceiver searchUserSubspecRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("textValue");
            searchUsrSS_W.setText(value);
            dMenuSerchUsrSS.dismiss();
        }
    };

    /***************************************************/

    /***************** Parse Methods *******************/
    /* Retrieve specialities */
    public List<Specialty> specParseList(final ProgressBar progressBar, final android.support.v7
            .widget.RecyclerView rw, final String userCat) {

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
                        specAdapter = new SpecAdapter(tempRetList, tempRetList, context, dMenuSearchUsrSP, CALLER);

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

    /* Select subspec based on a spec ID - Parse */
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
                                dMenuSerchUsrSS, widget, CALLER);

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

    /* Search for users on Parse */
    private void srcParseUsers(final ProgressBar progressBar) {

        // Hide emptySearchListL
        emptySearchListL.setVisibility(View.GONE);

        // Check for Internet
        if (DeviceConnection.testConnection(context)) {

            // Variable used to determine the field that starting-date applies to
            String controlVar = null;

            // Form user query
            ParseQuery<ParseUser> searchQuery = ParseUser.getQuery();

            //2. User Position filter
            /* Check if logged in user is physician or non-physician. If the user is physician
            /  look for "need<Position>" in the Parse section. Otherwise look for "offer<Position>" */
            if(tempUsrCheck.get("userProff").toString().contains(PHYSICIAN_STRING)) {

                // filter "need<Position> fields in Parse
                if (searchUsrPos_W.getText().toString().equals("Clerkship")) {
                    searchQuery.whereEqualTo("needClerkship", true);
                    controlVar = "dateNeedClerkship";
                }
                else if (searchUsrPos_W.getText().toString().equals("Residency")) {
                    searchQuery.whereEqualTo("needResidency", true);
                    controlVar = "dateNeedResidency";
                }
                else if (searchUsrPos_W.getText().toString().equals("Fellowship")) {
                    searchQuery.whereEqualTo("needFellowship", true);
                    controlVar = "dateNeedFellowship";
                }
                else if (searchUsrPos_W.getText().toString().equals("Job")) {
                    searchQuery.whereEqualTo("needJob", true);
                    controlVar = "dateNeedJob";
                }

            // Non-physicians
            } else {

                // filter "offer<Position> fields in Parse
                if (searchUsrPos_W.getText().toString().equals("Clerkship")) {
                    searchQuery.whereEqualTo("offerClerkship", true);
                    controlVar = "dateOfferClerkship";
                }
                else if (searchUsrPos_W.getText().toString().equals("Residency")) {
                    searchQuery.whereEqualTo("offerResidency", true);
                    controlVar = "dateOfferResidency";
                }
                else if (searchUsrPos_W.getText().toString().equals("Fellowship")) {
                    searchQuery.whereEqualTo("offerFellowship", true);
                    controlVar = "dateOfferFellowship";
                }
                else if (searchUsrPos_W.getText().toString().equals("Job")) {
                    searchQuery.whereEqualTo("offerJob", true);
                    controlVar = "dateOfferJob";
                }
            }

            //3. User spec filter
            String specWidgetVal = searchUsrS_W.getText().toString();
            searchQuery.whereEqualTo("userSpec", specWidgetVal);

            //4. User subspec filter
            if (!searchUsrSS_W.getText().toString().toLowerCase().equals("all")) {
                searchQuery.whereEqualTo("userSubspec", searchUsrSS_W.getText().toString());
            }

            //5. User state of interest filter

            // If the CLU (currently logged user) is not a practice/physician
            if (!physicianCheck) {

                // Filter SOI
                if (!searchUsrSOI_W.getText().toString().toLowerCase().equals("all")) {

                    // Limit query to 'state' instead of SOI as practices do not have an SOI but an SOR
                    searchQuery.whereEqualTo("state", searchUsrSOI_W.getText().toString());
                }

            // If the CLU is a practice search for S(s)OI as students can have up to 8 S(s)OI
            } else {

                if (!searchUsrSOI_W.getText().toString().toLowerCase().equals("all")) {

                    if (searchUsrSOI_W.getText().toString().equals("Alabama")) {
                        searchQuery.whereEqualTo("soi_alabama", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("Florida")) {
                        searchQuery.whereEqualTo("soi_florida", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("Georgia")) {
                        searchQuery.whereEqualTo("soi_georgia", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("Kentucky")) {
                        searchQuery.whereEqualTo("soi_kentucky", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("Missouri")) {
                        searchQuery.whereEqualTo("soi_missouri", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("North Carolina")) {
                        searchQuery.whereEqualTo("soi_nc", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("South Carolina")) {
                        searchQuery.whereEqualTo("soi_sc", true);
                    }
                    else if (searchUsrSOI_W.getText().toString().equals("Tennessee")) {
                        searchQuery.whereEqualTo("soi_tennessee", true);
                    }
                }
            }

            //6. User start date filter
            if (!searchUsrSD_W.getText().toString().toLowerCase().equals("all")) {
                if (controlVar != null) {

                    String tempDate = searchUsrSD_W.getText().toString();
                    searchQuery.whereEqualTo(controlVar, tempDate);
                }
            }

            searchQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {

                    if (e == null) {

                        // Create UserObject list
                        List<UserObject> userObjList = new ArrayList<>();

                        // Iterate over the user list
                        for (ParseUser tempUser : list) {

                            // Save each tempUser into a UserObj
                            final UserObject tempUsrObj = new UserObject();

                            tempUsrObj.setUserId(tempUser.getObjectId());
                            tempUsrObj.setEmail(tempUser.getEmail());
                            tempUsrObj.setFirstName(tempUser.getString("firstName"));
                            tempUsrObj.setLastName(tempUser.getString("lastName"));
                            tempUsrObj.setAddress(tempUser.getString("address"));
                            tempUsrObj.setCity(tempUser.getString("city"));
                            tempUsrObj.setZip(tempUser.getString("zip"));
                            tempUsrObj.setState(tempUser.getString("state"));
                            tempUsrObj.setUserSpec(tempUser.getString("userSpec"));
                            tempUsrObj.setUserSubspec(tempUser.getString("userSubspec"));
                            tempUsrObj.setUserSpecId(tempUser.getString("userSpecId"));
                            tempUsrObj.setUserProff(tempUser.getString("userProff"));
                            tempUsrObj.setUserCat(tempUser.getString("userCat"));

                            if(tempUsrObj.getUserProff().contains(PHYSICIAN_STRING)) {
                                // If the user
                                tempUsrObj.setPhysicianInst(tempUser.getString("instName"));
                            }

                            tempUsrObj.setSoiAlabama(tempUser.getBoolean("soi_alabama"));
                            tempUsrObj.setSoiFlorida(tempUser.getBoolean("soi_florida"));
                            tempUsrObj.setSoiGeorgia(tempUser.getBoolean("soi_georgia"));
                            tempUsrObj.setSoiKentucky(tempUser.getBoolean("soi_kentucky"));
                            tempUsrObj.setSoiMissouri(tempUser.getBoolean("soi_missouri"));
                            tempUsrObj.setSoiNc(tempUser.getBoolean("soi_nc"));
                            tempUsrObj.setSoiSc(tempUser.getBoolean("soi_sc"));
                            tempUsrObj.setSoiTennessee(tempUser.getBoolean("soi_tennessee"));

                            tempUsrObj.setNeedClerkship(tempUser.getBoolean("needClerkship"));
                            tempUsrObj.setDateNeedClerkship(tempUser.getString("dateNeedClerkship"));
                            tempUsrObj.setNeedResidency(tempUser.getBoolean("needResidency"));
                            tempUsrObj.setDateNeedResidency(tempUser.getString("dateNeedResidency"));
                            tempUsrObj.setNeedFellowship(tempUser.getBoolean("needFellowship"));
                            tempUsrObj.setDateNeedFellowship(tempUser.getString("dateNeedFellowship"));
                            tempUsrObj.setNeedJob(tempUser.getBoolean("needJob"));
                            tempUsrObj.setDateNeedJob(tempUser.getString("dateNeedJob"));

                            tempUsrObj.setOfferClerkship(tempUser.getBoolean("offerClerkship"));
                            tempUsrObj.setDateOfferClerkship(tempUser.getString("dateOfferClerkship"));
                            tempUsrObj.setOfferResidency(tempUser.getBoolean("offerResidency"));
                            tempUsrObj.setDateOfferResidency(tempUser.getString("dateOfferResidency"));
                            tempUsrObj.setOfferFellowship(tempUser.getBoolean("offerFellowship"));
                            tempUsrObj.setDateOfferFellowship(tempUser.getString("dateOfferFellowship"));
                            tempUsrObj.setOfferJob(tempUser.getBoolean("offerJob"));
                            tempUsrObj.setDateOfferJob(tempUser.getString("dateOfferJob"));

                            tempUsrObj.setNeedFinancialHelp(tempUser.getBoolean("needFinancialHelp"));
                            tempUsrObj.setAssistInterv(tempUser.getBoolean("assistInterv"));

                            // Get profile picture
                            final ParseFile profilePhotoRaw = tempUser.getParseFile("profilePicture");
                            byte[] bytes = new byte[0];

                            try {
                                bytes = profilePhotoRaw.getData();

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            try {

                                // Save the photo locally with a reference name
                                PhotoProcessor.savePicToLocal(profilePhotoRaw.getName(), bytes, context);
                                tempUsrObj.setProfilePicture(profilePhotoRaw.getName());

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }

                            // Add UserObj to the userObjList
                            userObjList.add(tempUsrObj);
                        }

                        // Show list
                        userMainAllLay.setVisibility(View.VISIBLE);

                        // Control empty search list UI visibility
                        if (list.isEmpty()) {

                            progressBar.setVisibility(View.GONE);
                            emptySearchListL.setVisibility(View.VISIBLE);
                        }
                        else if (!list.isEmpty()) {

                            progressBar.setVisibility(View.GONE);
                            emptySearchListL.setVisibility(View.GONE);
                        }

                        // Set the recyclerview's adapter
                        if (mRecyclerView != null) {
                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            mRecyclerView.setHasFixedSize(true);

                            // use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(context);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            // specify an adapter (see also next example)
                            mAdapter = new SearchUserAdapter(userObjList, context, "caca");
                            mRecyclerView.setAdapter(mAdapter);
                        }
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
        }
        else {

            // No Internet
            //Set dialog progress bar visibility off
            //progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }



    /***************************************************/

}
