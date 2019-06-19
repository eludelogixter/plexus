package brood.com.medcrawler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class AdminMain extends AppCompatActivity {

    private static String PHYSICIAN_STRING = "Physician";
    public static String CALLER = "adminMain";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private RelativeLayout mainWrapperLay;
    private ProgressBar adminMainPBar;
    private TextView medUsers;
    private TextView dentUsers;
    private TextView totalUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Administrator");
        setSupportActionBar(toolbar);

        setupBottomToolBar();
        setupTotalsCard();

        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.user_row_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEnabled(false);

        medUsers = (TextView) findViewById(R.id.registered_users_medical_totals_text);
        dentUsers = (TextView) findViewById(R.id.registered_users_dental_totals_text);
        totalUsers = (TextView) findViewById(R.id.registered_users_totals_text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_main, menu);
        return true;
    }

    @Override

    public void onResume() {
        super.onResume();

        // Hide wrapper layout until Parse gets something back & show loader
        mainWrapperLay = (RelativeLayout) findViewById(R.id.main_admin_wrapper_lay);
        mainWrapperLay.setVisibility(View.GONE);

        adminMainPBar = (ProgressBar) findViewById(R.id.admin_main_list_pbar);
        adminMainPBar.setVisibility(View.VISIBLE);

        // Call Parse for refreshed data
        getLastParseUsers(context, adminMainPBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Refresh
        if (id == R.id.administration_main_action_reload) {
            onResume();
            return true;
        }

        // Favs
        else if (id == R.id.administration_main_action_fav) {

            Intent favUsrIntent = new Intent(AdminMain.this, FavUsers.class);
            AdminMain.this.startActivity(favUsrIntent);
            return true;
        }

        // Create new user
        else if (id == R.id.administration_main_action_new_user) {

            // Redirect to the AdminAddUser
            Intent mainIntent = new Intent(AdminMain.this, AdminAddUser.class);
            AdminMain.this.startActivity(mainIntent);

            return true;

        } else if (id == R.id.administration_main_action_search) {

            // Redirect to the AdminSearchUsers
            Intent mainIntent = new Intent(AdminMain.this, AdminSearchUser.class);
            AdminMain.this.startActivity(mainIntent);

            return true;

        }

        // About
        else if (id == R.id.administration_main_action_about) {

            //Toast.makeText(this, "About", Toast.LENGTH_LONG).show();
            // Launch an about dialog
            About about = new About();
            about.launch(context);
            return true;

        }

        else if (id == R.id.administration_main_action_profile) {

            // Redirect to the AdminMain
            Intent myProfIntent = new Intent(AdminMain.this, UserProfile.class);
            myProfIntent.putExtra("myProfile", true);
            AdminMain.this.startActivity(myProfIntent);

            return true;

            // Logout
        }

        // Logout
        else if (id == R.id.administration_main_action_logout) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(AdminMain.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AdminMain.this.startActivity(mainIntent);
            AdminMain.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBottomToolBar() {
        ImageButton keyGenButton = (ImageButton) findViewById(R.id.administration_main_action_keygen);
        ImageButton specialityEditorButton = (ImageButton) findViewById(R.id.administration_main_action_speciality_editor);

        keyGenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Key Generator", Toast.LENGTH_LONG).show();
                // Redirect to the AdminKeyGen
                Intent mainIntent = new Intent(AdminMain.this, AdminKeyGen.class);
                AdminMain.this.startActivity(mainIntent);
            }
        });

        specialityEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirect to the AdminUserSpecialty
                Intent intentUserSpecialtyActivity = new Intent(getApplicationContext(), AdminUserSpecialty.class);
                startActivity(intentUserSpecialtyActivity);
            }
        });
    }

    private void setupTotalsCard() {
        TextView medicalTotalsTextView = (TextView) findViewById(R.id.registered_users_medical_totals_text);
        TextView dentalTotalsTextView = (TextView) findViewById(R.id.registered_users_dental_totals_text);
        TextView totalsTextView = (TextView) findViewById(R.id.registered_users_totals_text);

        medicalTotalsTextView.setText("453");
        dentalTotalsTextView.setText("324");
        totalsTextView.setText("777");
    }

    /* Parse query on last 5 registered users */
    private void getLastParseUsers(final Context context, final ProgressBar pBar) {

        if (DeviceConnection.testConnection(context)) {



            // User query
            ParseQuery<ParseUser> searchQuery = ParseUser.getQuery();
            searchQuery.orderByDescending("createdAt");
            //searchQuery.setLimit(5);
            searchQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {

                    // Set counters for med and dent user textview output
                    int med_count = 0;
                    int dent_count = 0;
                    int list_count = 0;

                    // Create UserObject list
                    List<UserObject> userObjList = new ArrayList<>();

                    if (e == null) {

                        for (ParseUser tempUser : list) {

                            // Save each tempUser into a UserObj
                            final UserObject tempUsrObj = new UserObject();

                            if (tempUser.getString("userProff").contains("Medical")) {
                                med_count++;
                            } else if (tempUser.getString("userProff").contains("Dental")) {
                                dent_count++;
                            }

                            // Add only the first 5 users to the list
                            if (list_count < 5) {

                                tempUsrObj.setUserId(tempUser.getObjectId());
                                tempUsrObj.setDateOfReg(tempUser.getCreatedAt().toString());
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

                                if (tempUsrObj.getUserProff().contains(PHYSICIAN_STRING)) {
                                    // If the user is a physician
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

                                // Increment list_count
                                list_count++;
                            }
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
                            mAdapter = new SearchUserAdapter(userObjList, context, "adminMain");
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        // Set TextView fields
                        medUsers.setText(String.valueOf(med_count));
                        dentUsers.setText(String.valueOf(dent_count));
                        totalUsers.setText(String.valueOf(list.size()));

                        // Set visibility for wrapper-layout to visible & pbar to invisible
                        mainWrapperLay.setVisibility(View.VISIBLE);
                        pBar.setVisibility(View.INVISIBLE);

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
            //progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
