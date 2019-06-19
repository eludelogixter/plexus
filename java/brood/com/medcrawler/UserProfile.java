package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    private Context context;
    private final String UNPROVIDED = "not provided";
    private final String MY_PROFILE = "myProfile";
    private final String USER_PROFILE = "userProfile";
    private final String FAV_USER_PROFILE = "fromFav";
    private final String RET_FROM_EDIT = "retFromEdit";
    private final String PHYSICIAN_STRING = "Physician";
    private final String ADMIN = "admin";
    private final String USER = "user";
    private TextView firstName;
    private TextView lastName;
    private TextView userCat;
    private TextView userSpec;
    private TextView userSubspec;
    private ImageView specIcon;
    private TextView userEmail;
    private TextView userAddress;
    private TextView userCity;
    private TextView userZip;
    private TextView userState;
    private RelativeLayout instLay;
    private TextView physicianInst;
    private TextView offOrLookTxt;
    private RelativeLayout clerkLay;
    private TextView clerkText;
    private TextView clerkDate;
    private RelativeLayout resLay;
    private TextView resText;
    private TextView resDate;
    private RelativeLayout fellowLay;
    private TextView fellowText;
    private TextView fellowDate;
    private RelativeLayout jobLay;
    private TextView jobText;
    private TextView jobDate;
    private RelativeLayout hostLay;
    private RelativeLayout finHelpLay;
    private RelativeLayout soiAlabamaLay;
    private RelativeLayout soiFloridaLay;
    private RelativeLayout soiGeorgiaLay;
    private RelativeLayout soiMissouriLay;
    private RelativeLayout soiKentuckyLay;
    private RelativeLayout soiNcLay;
    private RelativeLayout soiScLay;
    private RelativeLayout soiTnLay;
    private CardView allSoiCv;
    private Integer menuSwitch;
    private AlertDialog dMenuAddUsrToFav;
    private AlertDialog dMenuMakeAdmin;
    private AlertDialog dMenuMakeUser;
    private ParseUser localUsrParse;
    private RelativeLayout fatherLay;
    private Intent getDataIntent;
    private UserObject passUserObj;
    private ImageView userPhoto;
    private ProgressBar userProfPBar;
    private UsrRegScrollV layoutToHide;
    private String passUsrCat;
    private MenuItem makeAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Set the context for this activity
        context = this;

        // Set toolbar
        Toolbar myTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myTb);
        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Instantiate pbar for user profile & root-layout
        userProfPBar = (ProgressBar) findViewById(R.id.usrprof_progressbar);
        layoutToHide = (UsrRegScrollV) findViewById(R.id.user_reg_scroll);


        // Implement Activity textviews
        firstName = (TextView) findViewById(R.id.usr_prof_usrfname);
        lastName = (TextView) findViewById(R.id.usr_prof_usrlname);
        userCat = (TextView) findViewById(R.id.usr_prof_usrcat);
        userSpec = (TextView) findViewById(R.id.usr_spec_text);
        userSubspec = (TextView) findViewById(R.id.usr_prof_subspec_txt);
        specIcon = (ImageView) findViewById(R.id.usr_prof_pro_icon);
        userEmail = (TextView) findViewById(R.id.usr_prof_email_txt);
        userAddress = (TextView) findViewById(R.id.usr_prof_address_txt);
        userCity = (TextView) findViewById(R.id.usr_prof_city_txt);
        userZip = (TextView) findViewById(R.id.usr_prof_zip_txt);
        userState = (TextView) findViewById(R.id.usr_prof_state_txt);

        instLay = (RelativeLayout) findViewById(R.id.user_prof_institution_lay);
        physicianInst = (TextView) findViewById(R.id.usr_prof_instname_txt);

        offOrLookTxt = (TextView) findViewById(R.id.usr_prof_look_or_offer_txt);

        clerkLay = (RelativeLayout) findViewById(R.id.offer_need_clerkship_lay);
        clerkText = (TextView) findViewById(R.id.usr_prof_a_clerkship_txt);
        clerkDate = (TextView) findViewById(R.id.usr_prof_clerkship_sd_txt);

        resLay = (RelativeLayout) findViewById(R.id.offer_need_residency_lay);
        resText = (TextView) findViewById(R.id.usr_prof_a_residency_txt);
        resDate = (TextView) findViewById(R.id.usr_prof_residency_sd_txt);

        fellowLay = (RelativeLayout) findViewById(R.id.offer_need_fellowship_lay);
        fellowText = (TextView) findViewById(R.id.usr_prof_a_fellowship_txt);
        fellowDate = (TextView) findViewById(R.id.usr_prof_fellowship_sd_txt);

        jobLay = (RelativeLayout) findViewById(R.id.offer_need_job_lay);
        jobText = (TextView) findViewById(R.id.usr_prof_a_job_txt);
        jobDate = (TextView) findViewById(R.id.usr_prof_job_sd_txt);


        hostLay = (RelativeLayout) findViewById(R.id.user_prof_host_lay);
        finHelpLay = (RelativeLayout) findViewById(R.id.user_prof_fin_help_lay);

        soiAlabamaLay = (RelativeLayout) findViewById(R.id.soi_alabama_lay);
        soiFloridaLay = (RelativeLayout) findViewById(R.id.soi_florida_lay);
        soiGeorgiaLay = (RelativeLayout) findViewById(R.id.soi_georgia_lay);
        soiKentuckyLay = (RelativeLayout) findViewById(R.id.soi_kentucky_lay);
        soiMissouriLay = (RelativeLayout) findViewById(R.id.soi_missouri_lay);
        soiNcLay = (RelativeLayout) findViewById(R.id.soi_nc_lay);
        soiScLay = (RelativeLayout) findViewById(R.id.soi_sc_lay);
        soiTnLay = (RelativeLayout) findViewById(R.id.soi_tn_lay);

        allSoiCv = (CardView) findViewById(R.id.usr_prof_soi_cv);

        fatherLay = (RelativeLayout) findViewById(R.id.father_layout);

        userPhoto = (ImageView) findViewById(R.id.usr_prof_usrimg);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            /* When the menu is accessed from a user's profile load one xml file
             * Otherwise load the other xml file - one menu has the 'Edit My Profile'
             * while the other does not */
            if (menuSwitch == 0) {
                getMenuInflater().inflate(R.menu.menu_user_profile, menu);
            } else if (menuSwitch == 1) {
                getMenuInflater().inflate(R.menu.menu_my_profile, menu);
            } else if (menuSwitch == 2) {
                getMenuInflater().inflate(R.menu.menu_user_fav, menu);
            }

            // If menu access as an admin, show the admin_icon
            if ((localUsrParse.getString("userCat").equals(ADMIN)) && (menuSwitch != 1)) {

                // Check if the tapped user is currently a "user" or an "admin"
                if (passUsrCat.equals(USER)) {

                    makeAdmin = menu.findItem(R.id.make_admin);
                    makeAdmin.setIcon(R.drawable.admin_icon);
                    makeAdmin.setVisible(true);

                } else if (passUsrCat.equals(ADMIN)) {

                    makeAdmin = menu.findItem(R.id.make_admin);
                    makeAdmin.setIcon(R.drawable.admin_icon_green);
                    makeAdmin.setVisible(true);

                }

            }

            //dynMenu = menu;

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Edit My Profile
        if (id == R.id.edit_profile) {

            // Pass user to UserProfile
            Intent i = new Intent(context, UserEditProfile.class);
            i.putExtra("editMyProfile", passUserObj);
            context.startActivity(i);

            return true;
        }

        // Favorite Users
        else if (id == R.id.administration_usr_prof_favheart) {
            createAddUsrToFav_D();

            return true;

        }

        // Promote || Demote to Admin
        else if (id == R.id.make_admin) {

            // If tapped user is already an admin prompt demotion
            if (passUsrCat.equals(ADMIN)) {

                demoateToUser_D(context);

                // Else promote the user to admin
            } else if (passUsrCat.equals(USER)) {

                //makeAdmin.setIcon(R.drawable.admin_icon_green);
                userToAdmin_D(context);
            }
        }

        // About on all menus
        else if (id == R.id.administration_main_action_about) {

            // Launch an about dialog
            About about = new About();
            about.launch(context);
        }

        // Log out
        else if (id == R.id.administration_main_action_logout) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(UserProfile.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            UserProfile.this.startActivity(mainIntent);
            UserProfile.this.finish();

            return true;
        }

        // Return arrow
        else if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {

        // Caller intent
        getDataIntent = getIntent();

        // Local parse user
        localUsrParse = ParseUser.getCurrentUser();

        layoutToHide.setVisibility(View.GONE);
        userProfPBar.setVisibility(View.VISIBLE);

        /************************** Determine what type of menu to display ************************/
        // Activity called from search user list (
        if (getDataIntent.hasExtra(USER_PROFILE)) {

            menuSwitch = 0;
            passUserObj = (UserObject) getDataIntent.getSerializableExtra(USER_PROFILE);
            getParseUser(passUserObj.getUserId());

            // Get the userCat of the user that has been tapped on
            passUsrCat = new String();
            passUsrCat = passUserObj.getUserCat();

        }

        // Method called from Edit My Profile menu-option
        else if (getDataIntent.hasExtra(MY_PROFILE)) {

            getSupportActionBar().setTitle("My Profile");

            try {

                menuSwitch = 1;
                getParseUser(localUsrParse.getObjectId());
                passUsrCat = new String();
                passUsrCat = localUsrParse.getString("userCat");

            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        // Method called from Fav Users menu-option
        else if (getDataIntent.hasExtra(FAV_USER_PROFILE)) {

            menuSwitch = 2;

            passUserObj = (UserObject) getDataIntent.getSerializableExtra(FAV_USER_PROFILE);
            getParseUser(passUserObj.getUserId());
            passUsrCat = new String();
            passUsrCat = passUserObj.getUserCat();


        }
        // This comes as a return from EditMyProfile
        else if (getDataIntent.hasExtra(RET_FROM_EDIT)) {

            getParseUser(getDataIntent.getStringExtra(RET_FROM_EDIT));
            menuSwitch = 1;
            getSupportActionBar().setTitle("My Profile");
        } else {
            getParseUser(localUsrParse.getObjectId());
            passUsrCat = new String();
            passUsrCat = localUsrParse.getString("userCat");
        }

        /******************************************************************************************/

        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            PhotoProcessor.deleteCashedFiles(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Get Parse user by ID */
    private void getParseUser(String ID) {

        // Check the device internet connection
        if (DeviceConnection.testConnection(context)) {

            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.getInBackground(ID, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    if (e == null) {

                        passUserObj = new UserObject();
                        passUserObj.setUserCat(parseUser.getString("userCat"));
                        passUsrCat = new String();
                        passUsrCat = parseUser.getString("userCat");
                        passUserObj.setUserId(parseUser.getObjectId());
                        passUserObj.setUserSpecId(parseUser.getString("userSpecId"));
                        passUserObj.setFirstName(parseUser.getString("firstName").toString());
                        passUserObj.setLastName(parseUser.getString("lastName").toString());
                        passUserObj.setEmail(parseUser.getEmail());
                        passUserObj.setAddress(parseUser.getString("address"));
                        passUserObj.setCity(parseUser.getString("city"));
                        passUserObj.setZip(parseUser.getString("zip"));
                        passUserObj.setState(parseUser.getString("state"));
                        passUserObj.setUserSpec(parseUser.getString("userSpec"));
                        passUserObj.setUserSubspec(parseUser.getString("userSubspec"));
                        passUserObj.setUserProff(parseUser.getString("userProff"));
                        passUserObj.setUserSpecId(parseUser.getString("userSpecId"));

                        if (parseUser.getString("userProff").contains(PHYSICIAN_STRING)) {
                            passUserObj.setPhysicianInst(parseUser.getString("instName"));
                        }

                        passUserObj.setSoiAlabama(parseUser.getBoolean("soi_alabama"));
                        passUserObj.setSoiFlorida(parseUser.getBoolean("soi_florida"));
                        passUserObj.setSoiGeorgia(parseUser.getBoolean("soi_georgia"));
                        passUserObj.setSoiKentucky(parseUser.getBoolean("soi_kentucky"));
                        passUserObj.setSoiMissouri(parseUser.getBoolean("soi_missouri"));
                        passUserObj.setSoiSc(parseUser.getBoolean("soi_sc"));
                        passUserObj.setSoiNc(parseUser.getBoolean("soi_nc"));
                        passUserObj.setSoiTennessee(parseUser.getBoolean("soi_tennessee"));

                        passUserObj.setNeedClerkship(parseUser.getBoolean("needClerkship"));
                        passUserObj.setDateNeedClerkship(parseUser.getString("dateNeedClerkship"));
                        passUserObj.setNeedResidency(parseUser.getBoolean("needResidency"));
                        passUserObj.setDateNeedResidency(parseUser.getString("dateNeedResidency"));
                        passUserObj.setNeedFellowship(parseUser.getBoolean("needFellowship"));
                        passUserObj.setDateNeedFellowship(parseUser.getString("dateNeedFellowship"));
                        passUserObj.setNeedJob(parseUser.getBoolean("needJob"));
                        passUserObj.setDateNeedJob(parseUser.getString("dateNeedJob"));

                        passUserObj.setOfferClerkship(parseUser.getBoolean("offerClerkship"));
                        passUserObj.setDateOfferClerkship(parseUser.getString("dateOfferClerkship"));
                        passUserObj.setOfferResidency(parseUser.getBoolean("offerResidency"));
                        passUserObj.setDateOfferResidency(parseUser.getString("dateOfferResidency"));
                        passUserObj.setOfferFellowship(parseUser.getBoolean("offerFellowship"));
                        passUserObj.setDateOfferFellowship(parseUser.getString("dateOfferFellowship"));
                        passUserObj.setOfferJob(parseUser.getBoolean("offerJob"));
                        passUserObj.setDateOfferJob(parseUser.getString("dateOfferJob"));

                        passUserObj.setNeedFinancialHelp(parseUser.getBoolean("needFinancialHelp"));
                        passUserObj.setAssistInterv(parseUser.getBoolean("assistInterv"));

                        final ParseFile tempFile = parseUser.getParseFile("profilePicture");

                        if (tempFile != null) {

                            tempFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null) {

                                        // Get name of the photo file
                                        final String fileName = tempFile.getName();
                                        passUserObj.setProfilePicture(fileName);

                                        try {

                                            // Save the photo locally with a reference name
                                            PhotoProcessor.savePicToLocal(fileName, bytes, context);
                                            Bitmap tempBmp = BitmapFactory.decodeByteArray
                                                    (PhotoProcessor.readPicFromLocal(fileName, context),
                                                            0, PhotoProcessor.readPicFromLocal
                                                                    (fileName, context).length);
                                            userPhoto.setImageBitmap(tempBmp);

                                        } catch (Exception je) {
                                            je.printStackTrace();
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

                        firstName.setText(passUserObj.getFirstName());
                        lastName.setText(passUserObj.getLastName());
                        userCat.setText(passUserObj.getUserProff());
                        userSpec.setText(passUserObj.getUserSpec());

                        // Different spec. icon + subspec text field on med vs dent
                        if (passUserObj.getUserProff().toLowerCase().contains("medical")) {
                            specIcon.setImageResource(R.drawable.medical_icon);
                            userSubspec.setText(passUserObj.getUserSubspec());
                        } else {
                            specIcon.setImageResource(R.drawable.dental_icon);
                            userSubspec.setText("User Specialty " + UNPROVIDED);
                            userSubspec.setTextColor(getResources().getColor(R.color.listDeviderColor));
                        }

                        userEmail.setText(passUserObj.getEmail());

                        // Check if address is not provided and set userAddress field accordingly
                        if ((passUserObj.getAddress() == null) || (passUserObj.getAddress().isEmpty())) {
                            userAddress.setText("Address " + UNPROVIDED);
                            userAddress.setTextColor(getResources().getColor(R.color.listDeviderColor));
                        } else {
                            userAddress.setText(passUserObj.getAddress());
                        }

                        userCity.setText(passUserObj.getCity());

                        // Check if zipcode is not provided and set userZip field accordingly
                        if (passUserObj.getZip() == null || passUserObj.getZip().isEmpty()) {
                            userZip.setText("Zip Code " + UNPROVIDED);
                            userZip.setTextColor(getResources().getColor(R.color.listDeviderColor));
                        } else {
                            userZip.setText(passUserObj.getZip());
                        }

                        userState.setText(passUserObj.getState());

                        // Not a physician
                        if (!userCat.getText().toString().toLowerCase().contains("physician")) {

                            if (allSoiCv.getVisibility() == View.GONE) {
                                allSoiCv.setVisibility(View.VISIBLE);
                            }

                            // Hide Institution Name layout
                            if (instLay.getVisibility() == View.VISIBLE) {
                                instLay.setVisibility(View.GONE);
                            }

                            // Set visibility for SOI
                            if (passUserObj.getSoiAlabama()) {
                                soiAlabamaLay.setVisibility(View.VISIBLE);
                            } else {
                                soiAlabamaLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiFlorida()) {
                                soiFloridaLay.setVisibility(View.VISIBLE);
                            } else {
                                soiFloridaLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiGeorgia()) {
                                soiGeorgiaLay.setVisibility(View.VISIBLE);
                            } else {
                                soiGeorgiaLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiKentucky()) {
                                soiKentuckyLay.setVisibility(View.VISIBLE);
                            } else {
                                soiKentuckyLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiMissouri()) {
                                soiMissouriLay.setVisibility(View.VISIBLE);
                            } else {
                                soiMissouriLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiNc()) {
                                soiNcLay.setVisibility(View.VISIBLE);
                            } else {
                                soiNcLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiSc()) {
                                soiScLay.setVisibility(View.VISIBLE);
                            } else {
                                soiScLay.setVisibility(View.GONE);
                            }

                            if (passUserObj.getSoiTennessee()) {
                                soiTnLay.setVisibility(View.VISIBLE);
                            } else {
                                soiTnLay.setVisibility(View.GONE);
                            }

                            // Set OfferOrLook text
                            offOrLookTxt.setText("I'm looking for:");

                            // Clerkship text
                            if (passUserObj.getNeedClerkship()) {
                                clerkLay.setVisibility(View.VISIBLE);
                                clerkText.setText(Html.fromHtml("<b>a clerkship</b> position"));
                                clerkDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateNeedClerkship() + "</b>"));
                            } else {
                                clerkLay.setVisibility(View.GONE);
                            }

                            // Residency text
                            if (passUserObj.getNeedResidency()) {
                                resLay.setVisibility(View.VISIBLE);
                                resText.setText(Html.fromHtml("<b>a residency</b> position"));
                                resDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateNeedResidency() + "</b>"));
                            } else {
                                resLay.setVisibility(View.GONE);
                            }

                            // Fellowship text
                            if (passUserObj.getNeedFellowship()) {
                                fellowLay.setVisibility(View.VISIBLE);
                                fellowText.setText(Html.fromHtml("<b>a fellowship</b> position"));
                                fellowDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateNeedFellowship() + "</b>"));
                            } else {
                                fellowLay.setVisibility(View.GONE);
                            }

                            // Job text
                            if (passUserObj.getNeedJob()) {
                                jobLay.setVisibility(View.VISIBLE);
                                jobText.setText(Html.fromHtml("<b>a job</b> position"));
                                jobDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateNeedJob() + "</b>"));
                            } else {
                                jobLay.setVisibility(View.GONE);
                            }

                            hostLay.setVisibility(View.GONE);

                            // Need financial help
                            if (passUserObj.getNeedFinancialHelp()) {
                                finHelpLay.setVisibility(View.VISIBLE);
                            } else {
                                finHelpLay.setVisibility(View.GONE);
                            }

                        }

                        // Physicians
                        else {

                            // If user is a physician hide the soi card-view
                            if (allSoiCv.getVisibility() == View.VISIBLE) {
                                allSoiCv.setVisibility(View.GONE);
                            }

                            if (instLay.getVisibility() == View.GONE) {
                                instLay.setVisibility(View.VISIBLE);
                            }

                            // Institution Name
                            if ((passUserObj.getPhysicianInst() != null) && (!passUserObj.getPhysicianInst().isEmpty())) {
                                physicianInst.setText(passUserObj.getPhysicianInst());
                            } else {
                                physicianInst.setText("Institution " + UNPROVIDED);
                                physicianInst.setTextColor(getResources().getColor(R.color.listDeviderColor));
                            }

                            // Offer/Look for txt
                            offOrLookTxt.setText("I'm offering:");

                            // Clerkship text
                            if (passUserObj.getOfferClerkship()) {
                                clerkLay.setVisibility(View.VISIBLE);
                                clerkText.setText(Html.fromHtml("<b>clerkship</b> position(s)"));
                                clerkDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateOfferClerkship() + "</b>"));
                            } else {
                                clerkLay.setVisibility(View.GONE);
                            }

                            // Residency text
                            if (passUserObj.getOfferResidency()) {
                                resLay.setVisibility(View.VISIBLE);
                                resText.setText(Html.fromHtml("<b>residency</b> position(s)"));
                                resDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateOfferResidency() + "</b>"));
                            } else {
                                resLay.setVisibility(View.GONE);
                            }

                            // Fellowship text
                            if (passUserObj.getOfferFellowship()) {
                                fellowLay.setVisibility(View.VISIBLE);
                                fellowText.setText(Html.fromHtml("<b>fellowship</b> position(s)"));
                                fellowDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateOfferFellowship() + "</b>"));
                            } else {
                                fellowLay.setVisibility(View.GONE);
                            }

                            // Job text
                            if (passUserObj.getOfferJob()) {
                                jobLay.setVisibility(View.VISIBLE);
                                jobText.setText(Html.fromHtml("<b>job</b> position(s)"));
                                jobDate.setText(Html.fromHtml(", starting in " + "<b>"
                                        + passUserObj.getDateOfferJob() + "</b>"));
                            } else {
                                jobLay.setVisibility(View.GONE);
                            }

                            // Host Interviewees
                            if (passUserObj.getAssistInterv()) {
                                hostLay.setVisibility(View.VISIBLE);
                            } else {
                                hostLay.setVisibility(View.GONE);
                            }

                            // Hide Need financial help
                            finHelpLay.setVisibility(View.GONE);


                        }

                        // Dismiss loader & show UI
                        userProfPBar.setVisibility(View.GONE);
                        layoutToHide.setVisibility(View.VISIBLE);

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

            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    /* Create add_to_fav confirmation dialog */
    private void createAddUsrToFav_D() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_add_to_fav, null);

        // Set the Progress Bar
        ProgressBar searchSubspecPB;
        searchSubspecPB = (ProgressBar) myDialogView.findViewById(R.id.add_to_fav_pbar);
        searchSubspecPB.setIndeterminate(true);
        searchSubspecPB.setVisibility(View.GONE);

        AppCompatButton yesBtn = (AppCompatButton) myDialogView.findViewById(R.id.yes_add_to_fav_btn);
        AppCompatButton noBtn = (AppCompatButton) myDialogView.findViewById(R.id.no_add_to_fav_btn);
        TextView nameUser = (TextView) myDialogView.findViewById(R.id.usr_to_be_added);
        nameUser.setText("" + firstName.getText().toString() + " " + lastName.getText().toString());

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call Parse to add user as favorite
                saveUserToFav(localUsrParse.getObjectId(), passUserObj.getUserId());
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dMenuAddUsrToFav.dismiss();
            }
        });

        builder.setView(myDialogView);
        dMenuAddUsrToFav = builder.create();
        dMenuAddUsrToFav.setCanceledOnTouchOutside(false);
        dMenuAddUsrToFav.show();
    }

    /* Create demoteToUser_D */
    private void demoateToUser_D(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_make_user, null);

        final AppCompatButton yesBtn = (AppCompatButton) myDialogView.findViewById(R.id.d_make_user_yes_btn);
        AppCompatButton noBtn = (AppCompatButton) myDialogView.findViewById(R.id.d_make_user_no_btn);
        TextView nameUser = (TextView) myDialogView.findViewById(R.id.d_user_name_make_user_textview);
        nameUser.setText("" + firstName.getText().toString() + " " + lastName.getText().toString());
        final ProgressBar pBar = (ProgressBar) myDialogView.findViewById(R.id.d_make_user_pbar);
        pBar.setVisibility(View.GONE);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pBar.setVisibility(View.VISIBLE);
                makeAdmin.setIcon(R.drawable.admin_icon);
                yesBtn.setEnabled(false);
                callDemAdmin(passUserObj.getUserId(), dMenuMakeUser, context, yesBtn);
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dMenuMakeUser.dismiss();
            }
        });

        builder.setView(myDialogView);
        dMenuMakeUser = builder.create();
        dMenuMakeUser.setCanceledOnTouchOutside(false);
        dMenuMakeUser.show();
    }

    /* Create userToAdmin_D */
    private void userToAdmin_D(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_make_admin, null);

        final AppCompatButton yesBtn = (AppCompatButton) myDialogView.findViewById(R.id.d_make_admin_yes_btn);
        AppCompatButton noBtn = (AppCompatButton) myDialogView.findViewById(R.id.d_make_admin_no_btn);
        TextView nameUser = (TextView) myDialogView.findViewById(R.id.d_user_name_make_admin_textview);
        nameUser.setText("" + firstName.getText().toString() + " " + lastName.getText().toString());

        final ProgressBar pBar = (ProgressBar) myDialogView.findViewById(R.id.d_make_admin_pbar);
        pBar.setVisibility(View.GONE);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pBar.setVisibility(View.VISIBLE);
                makeAdmin.setIcon(R.drawable.admin_icon_green);
                yesBtn.setEnabled(false);
                callPromAdminOnCloud(passUserObj.getUserId(), dMenuMakeAdmin, context, yesBtn);
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dMenuMakeAdmin.dismiss();
            }
        });

        builder.setView(myDialogView);
        dMenuMakeAdmin = builder.create();
        dMenuMakeAdmin.setCanceledOnTouchOutside(false);
        dMenuMakeAdmin.show();

    }

    /* Parse method to save fav user */
    private void saveUserToFav(final String hostUsrId, final String addUsrId) {

        if (DeviceConnection.testConnection(context)) {

            ParseQuery<ParseUser> hostUsrQuery = ParseUser.getQuery();
            hostUsrQuery.getInBackground(hostUsrId, new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser hostParseUsr, ParseException e) {
                    if (e == null) {

                        // Check the existence of addUsr
                        ParseQuery<ParseUser> addUsrQuery = ParseUser.getQuery();
                        addUsrQuery.getInBackground(addUsrId, new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser addParseUsr, ParseException e) {
                                if (e == null) {

                                    ParseRelation<ParseUser> relation = hostParseUsr.getRelation("favUsers");

                                    relation.add(addParseUsr);

                                    // save a pointer to the hostUser
                                    hostParseUsr.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {

                                                // Pointer saved successfully
                                                Toast.makeText(context, "User added to Favorites",
                                                        Toast.LENGTH_LONG).show();
                                                dMenuAddUsrToFav.dismiss();
                                            } else {
                                                // <marked>
                                                if (e.getCode() == 209) {
                                                    ParseProcessor.createInvalidSessionD(context);
                                                } else {
                                                    // Parse error, prompt with feedback
                                                    Toast.makeText(context, e.getMessage() + " " +
                                                            e.getCode(), Toast.LENGTH_LONG).show();
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

            // No Internet
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    /* Change role to admin */
    private void addUserToAdminRole(final AlertDialog dialog, final Context context,
                                    final Button button) {
        if (DeviceConnection.testConnection(context)) {

            HashMap<String, Object> params = new HashMap<>();
            params.put("objectId", passUserObj.getUserId());
            ParseCloud.callFunctionInBackground("addUserToAdminRole", params, new FunctionCallback<String>() {
                public void done(String stringBack, ParseException e) {
                    if (e == null) {

                        forceUserLogout(context, dialog, button);
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

    /* Remove user from admin role */
    private void removeUserFromAdminRole(final AlertDialog dialog, final Context context,
                                         final Button button) {
        if (DeviceConnection.testConnection(context)) {

            HashMap<String, Object> params = new HashMap<>();
            params.put("objectId", passUserObj.getUserId());
            ParseCloud.callFunctionInBackground("removeUserToAdminRole", params, new FunctionCallback<String>() {
                public void done(String stringBack, ParseException e) {
                    if (e == null) {
                        //Toast.makeText(context, stringBack, Toast.LENGTH_LONG).show();
                        forceUserLogout(context, dialog, button);
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

    /* Call Parse cloud code to promote user to admin */
    private void callPromAdminOnCloud(final String userId, final AlertDialog dialog,
                                      final Context context, final Button button) {

        if (DeviceConnection.testConnection(context)) {

            HashMap<String, Object> params = new HashMap<>();
            params.put("objectId", userId);
            ParseCloud.callFunctionInBackground("promoteToAdmin", params, new FunctionCallback<String>() {
                public void done(String stringBack, ParseException e) {
                    if (e == null) {

                        //dialog.dismiss();
                        //Toast.makeText(context, "userCat modified", Toast.LENGTH_LONG).show();
                        addUserToAdminRole(dialog, context, button);
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

    /* Call Parse cloud code to demote admin to user */
    private void callDemAdmin(final String userId, final AlertDialog dialog, final Context context,
                              final Button button) {

        if (DeviceConnection.testConnection(context)) {

            HashMap<String, Object> params = new HashMap<>();
            params.put("objectId", userId);
            ParseCloud.callFunctionInBackground("demoteToUser", params, new FunctionCallback<String>() {
                public void done(String stringBack, ParseException e) {
                    if (e == null) {

                        //dialog.dismiss();
                        //Toast.makeText(context, "userCat modified", Toast.LENGTH_LONG).show();
                        //addUserToAdminRole(dialog, context);
                        removeUserFromAdminRole(dialog, context, button);
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

    /* Force user to logout */
    private void forceUserLogout(final Context context, final AlertDialog dialog, final Button btn) {
        if (DeviceConnection.testConnection(context)) {

            HashMap<String, Object> params = new HashMap<>();
            params.put("objectId", passUserObj.getUserId());
            ParseCloud.callFunctionInBackground("modSessions", params, new FunctionCallback<String>() {
                public void done(String stringBack, ParseException e) {
                    if (e == null) {
                        //Toast.makeText(context, stringBack, Toast.LENGTH_LONG).show();
                        //forceUserLogout(context, dialog, button);
                        dialog.dismiss();
                        Toast.makeText(context, "Operation Successful", Toast.LENGTH_LONG).show();
                    } else {
                        // <marked>
                        if (e.getCode() == 209) {
                            ParseProcessor.createInvalidSessionD(context);
                        } else {
                            // Parse error, prompt with feedback
                            Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                            //button.setEnabled(true);
                        }
                    }
                }

            });

        } else {
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
            btn.setEnabled(true);
        }
    }

    /* Refresh tapped user so that the userCat reflects the admin change */
    private void refreshTappedUser(final Context context, String userId) {
        if (DeviceConnection.testConnection(context)) {

            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("objectId", userId);
            userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                }
            });


        } else {
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
