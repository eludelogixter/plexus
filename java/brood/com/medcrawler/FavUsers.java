package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FavUsers extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Context context;
    private RelativeLayout favUsrReLay;
    private final String PHYSICIAN_STRING = "Physician";
    private ProgressBar favPbar;
    private ParseUser localUsr;
    private String favUsrFLName;
    private String favUsrId;
    private AlertDialog dFavUsrDel;
    private Vibrator vib;
    private RelativeLayout favAllLay;
    private MenuItem searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_users);

        context = this;
        localUsr = ParseUser.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favorite Users");

        if (localUsr.getString("userCat").equals("admin")) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Get instance of Vibrator from current Context
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mRecyclerView = (RecyclerView)findViewById(R.id.fav_usr_list);
        mRecyclerView.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEnabled(false);

        favPbar = (ProgressBar)findViewById(R.id.fav_usr_get_progressbar);
        favUsrReLay = (RelativeLayout)findViewById(R.id.empty_favusrlist_feedback);

        favAllLay = (RelativeLayout)findViewById(R.id.fav_all_lay);

        registerReceiver(favUserDelRcv, new IntentFilter("favUsrDel"));

    }
    @Override
    public void onResume() {

        favAllLay.setVisibility(View.GONE);

        getFavUsers(favPbar, favUsrReLay, localUsr.getObjectId());
        super.onResume();
    }
    @Override
    protected void onStop() {
        try {
            unregisterReceiver(favUserDelRcv);
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav_users, menu);

        // If activity is accessed from admin hide fav menu icon
        searchBtn = menu.findItem(R.id.administration_main_action_search);
        if (localUsr.getString("userCat").equals("admin")) {
            searchBtn.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.administration_main_action_search) {
            Intent favUsrIntent = new Intent(FavUsers.this, UserMain.class);
            FavUsers.this.startActivity(favUsrIntent);
            FavUsers.this.finish();

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
            Intent myProfIntent = new Intent(FavUsers.this, UserProfile.class);
            myProfIntent.putExtra("myProfile", true);
            FavUsers.this.startActivity(myProfIntent);

            return true;

            // Logout
        } else if (id == R.id.administration_main_action_logout) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(FavUsers.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            FavUsers.this.startActivity(mainIntent);
            FavUsers.this.finish();

            return true;
        }

        // Return arrow
        else if (id == android.R.id.home) {

            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Retrieve fav users from Parse */
    private void getFavUsers(final ProgressBar pbar, final RelativeLayout noFavLay, final String userId) {

        if (DeviceConnection.testConnection(context)) {

            if (favAllLay.getVisibility() != View.GONE) {
                favAllLay.setVisibility(View.GONE);
            }

            // Show loader
            pbar.setVisibility(View.VISIBLE);

            // Get updated current user by id
            ParseQuery<ParseUser> luQuery = ParseUser.getQuery();
            luQuery.getInBackground(userId, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {
                        // Retrieve user's favUsers relation
                        ParseRelation<ParseUser> favRelation = parseUser.getRelation("favUsers");

                        // Retrieve all users from the relation
                        ParseQuery relQuery = favRelation.getQuery();

                        relQuery.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                if (e == null) {

                                    List<UserObject> userObjList = new ArrayList<>();
                                    for (ParseUser tempUser : list) {
                                        // Save each tempUser into a UserObj
                                        UserObject tempUsrObj = new UserObject();

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

                                        if(tempUsrObj.getUserProff().contains(PHYSICIAN_STRING)) {

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

                                    // Control empty search list UI visibility
                                    if (list.isEmpty()) {
                                        favUsrReLay.setVisibility(View.VISIBLE);

                                    }
                                    else if (!list.isEmpty()) {
                                        favUsrReLay.setVisibility(View.GONE);

                                    }

                                    // Set the recyclerview and the adapter for it
                                    if (mRecyclerView != null) {

                                        mAdapter = new SearchUserAdapter(userObjList, context, "fromFav");
                                        mRecyclerView.setAdapter(mAdapter);
                                    }

                                    // Hide progress bar
                                    pbar.setVisibility(View.GONE);

                                    // Set favAllLay to visible - show everything once loaded
                                    if (favAllLay.getVisibility() != View.VISIBLE) {
                                        favAllLay.setVisibility(View.VISIBLE);
                                    }
                                }
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

            // Populate search-adapter with the fav_user_list
        }

        else {

            // No internet
            pbar.setVisibility(View.INVISIBLE);
            noFavLay.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    /* Create del contextual menu */
    private void createDelFavD(String favUsrFLName, final String localParseUsrId, final String relationParseId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_menu_favusr_del, null);

        // Delete button
        AppCompatButton delBtn = (AppCompatButton)myDialogView.findViewById(R.id.fav_usr_del_btn);
        delBtn.setText("Remove " + favUsrFLName);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call Parse dialog
                remFavUsr(localParseUsrId, relationParseId);
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dFavUsrDel = builder.create();
        //dFavUsrDel.setCanceledOnTouchOutside(false);
        vib.vibrate(100);
        dFavUsrDel.show();
    }


    /* BroadcastReceiver used to delete a favUser from the list */
    private BroadcastReceiver favUserDelRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fname = intent.getStringExtra("favUsr_fname");
            String lname = intent.getStringExtra("favUsr_lname");
            favUsrFLName = fname + " " + lname;
            favUsrId = intent.getStringExtra("favUsr_ID");
            ParseUser localUsr = ParseUser.getCurrentUser();
            createDelFavD(favUsrFLName, localUsr.getObjectId(), favUsrId);
        }
    };

    /* Parse remove fav user */
    private void remFavUsr(String usrId, final String relationUsrId) {
        if (DeviceConnection.testConnection(context)) {

            ParseQuery<ParseUser> favUsrQuery = ParseUser.getQuery();
            favUsrQuery.getInBackground(usrId, new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser tempLU, ParseException e) {
                    if (e == null) {

                        // Retrieve user's favUsers relation
                        final ParseRelation<ParseUser> favRelation = tempLU.getRelation("favUsers");
                        final ParseQuery<ParseUser> relationUser = favRelation.getQuery();
                        relationUser.getInBackground(relationUsrId, new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {

                                    favRelation.remove(parseUser);
                                    tempLU.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {

                                                dFavUsrDel.dismiss();
                                                getFavUsers(favPbar, favUsrReLay, localUsr.getObjectId());
                                                Toast.makeText(context, "User removed", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                // <marked>
                                                if (e.getCode() == 209) {
                                                    ParseProcessor.createInvalidSessionD(context);
                                                } else {
                                                    // Parse error, prompt with feedback
                                                    Toast.makeText(context, e.getMessage() + " " + e.getCode(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    });
                                }
                                else {
                                    // <marked>
                                    if (e.getCode() == 209) {
                                        ParseProcessor.createInvalidSessionD(context);
                                    } else {
                                        // Parse error, prompt with feedback
                                        Toast.makeText(context, e.getMessage() + " " + e.getCode(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }

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

        else {
            //Set dialog progress bar visibility off
            //dMenuVerData.dismiss();
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
