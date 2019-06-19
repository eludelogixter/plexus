package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.util.List;


public class GeneralLogin extends AppCompatActivity {

    private Context context;
    private AppCompatEditText emailEditText;
    private AppCompatEditText passwordEditText;
    private ProgressBar progressBar;
    private Button loginBtn;
    private AlertDialog dMenuUsrEmailVal;
    private AlertDialog dMenuResetPsw;
    private String __sys_ppp;
    private String __sys_emm;
    private String DEBUG_TAG = "DEBUG";
    private Toast toast;
    private TextView forgetPassTW;
    private TextView registerTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_login);

        // Set the login context
        context = this;

        // Clean all temp photos from the internal storage (if any existent) before loading the app
        try {
            PhotoProcessor.deleteCashedFiles(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Instantialize the toast
        toast = Toast.makeText(context, " ", Toast.LENGTH_LONG);

        // Redirect to user or admin
        ParseUser tempUsrCheck = ParseUser.getCurrentUser();
        if (tempUsrCheck != null) {

            // Redirect logged
            if (tempUsrCheck.get("userCat").toString().equals("admin")) {

                // Redirect to the AdminMain
                Intent adminIntent = new Intent(GeneralLogin.this, AdminMain.class);
                GeneralLogin.this.startActivity(adminIntent);
                GeneralLogin.this.finish();

            } else if (tempUsrCheck.get("userCat").toString().equals("user")) {

                // Redirect to FavUsers
                Intent userIntent = new Intent(GeneralLogin.this, FavUsers.class);
                GeneralLogin.this.startActivity(userIntent);
                GeneralLogin.this.finish();
            }
        }

        // Set toolbar
        Toolbar myTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myTb);
        getSupportActionBar().setTitle("Plexus");

        // Hide the keyboard as as soon as the activity is loaded
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Target the email editText
        emailEditText = (android.support.v7.widget.AppCompatEditText) findViewById(R.id.email_login);
        passwordEditText = (android.support.v7.widget.AppCompatEditText) findViewById(R.id.password_login);

        /* Set the password-field to default font using this hack */
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setTypeface(Typeface.DEFAULT);
        /* ----------------------------------------------------- */
        // Progress bar
        progressBar = (ProgressBar) findViewById(R.id.signin_progress_bar);
        progressBar.setIndeterminate(true);

        //todo: add forget password feature

        // Target the Sign In btn
        loginBtn = (Button) findViewById(R.id.general_login_signin_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onParseLogin(v);
            }
        });

        // Target forget-password & register TWs
        forgetPassTW = (TextView) findViewById(R.id.forget_password);
        forgetPassTW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):

                        forgetPassTW.setTextColor(getResources().getColor(R.color.colorSelectMultiOption));
                        return true;

                    case (MotionEvent.ACTION_UP):

                        forgetPassTW.setTextColor(getResources().getColor(R.color.colorPrimary));
                        createPswResetD();
                        return true;

                    default:
                }
                return true;
            }
        });

        registerTW = (TextView) findViewById(R.id.create_account);
        registerTW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):

                        registerTW.setTextColor(getResources().getColor(R.color.colorSelectMultiOption));
                        return true;

                    case (MotionEvent.ACTION_UP):

                        registerTW.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Intent goToRegIntent = new Intent(context, AdminAddUser.class);
                        GeneralLogin.this.startActivity(goToRegIntent);
                        return true;

                    default:
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Create User
        if (id == R.id.gen_login_usreg_btn) {

            // Redirect to the AdminAddUser
            Intent mainIntent = new Intent(GeneralLogin.this, AdminAddUser.class);
            GeneralLogin.this.startActivity(mainIntent);

            return true;
        }

        // About
        else if (id == R.id.gen_login_action_settings) {

            // Launch an about dialog
            About about = new About();
            about.launch(context);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Bounce to a dialog when and if the user's email hasn't been verified */
    private void createEmailVerDg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final ParseUser tUsr = ParseUser.getCurrentUser();

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_email_val, null);
        AppCompatButton cancel = (AppCompatButton) myDialogView.findViewById(R.id.ver_email_cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dMenuUsrEmailVal.dismiss();
            }
        });

        AppCompatButton checkStatus = (AppCompatButton) myDialogView.findViewById(R.id.aff_del_spec_btn);
        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toast = Toast.makeText(context, "Checking account status...", Toast.LENGTH_LONG);
                toast.show();
                onParseLogin(v);
            }
        });


        TextView clickHereTV = (TextView) myDialogView.findViewById(R.id.click_here_txt);
        clickHereTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(toast.getView().isShown())) {
                    toast = Toast.makeText(context, "Sending...",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                resendEmailParse(tUsr.getObjectId());
            }
        });

        builder.setView(myDialogView);

        dMenuUsrEmailVal = builder.create();
        dMenuUsrEmailVal.setCanceledOnTouchOutside(false);
        dMenuUsrEmailVal.show();


        tUsr.logOut();
    }

    /* Create dialog for password reset */
    private void createPswResetD() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View myDialogView = inflater.inflate(R.layout.d_menu_reset_gl_password, null);

        AppCompatButton rstBtn = (AppCompatButton) myDialogView.findViewById(R.id.gl_psw_reset_btn);
        AppCompatButton cancelBtn = (AppCompatButton) myDialogView.findViewById(R.id.gl_psw_reset_cancel_btn);
        final EditText resetEmailET = (EditText) myDialogView.findViewById(R.id.gl_psw_reset_email_et);

        resetEmailET.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);


        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        rstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send psw_reset email to the email provided
                StringProcessor strProc = new StringProcessor();

                List<RegFeedback> tempErrorFeedback;

                tempErrorFeedback = strProc.processInput(StringProcessor.Mode.EMAIL, resetEmailET.getText().toString());

                if (!tempErrorFeedback.isEmpty()) {

                    String getError = tempErrorFeedback.get(0).getFeedbackText();
                    resetEmailET.setError(getError);

                } else {
                    // Hide the keyboard as as soon as the activity is loaded
                    // Check if no view has focus:
                    imm.hideSoftInputFromWindow(myDialogView.getWindowToken(), 0);
                    resetPass(resetEmailET.getText().toString(), myDialogView);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss this dialog
                // Hide the keyboard as as soon as the activity is loaded
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(myDialogView.getWindowToken(), 0);
                dMenuResetPsw.dismiss();
            }
        });

        builder.setView(myDialogView);
        dMenuResetPsw = builder.create();
        dMenuResetPsw.show();

    }

    /* Parse operations */

    // Login Parse user
    private void onParseLogin(final View v) {

        // Check the device internet connection
        if (DeviceConnection.testConnection(context)) {

            loginBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            ParseUser.logInInBackground(emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {

                            if (e == null) {

                                __sys_ppp = passwordEditText.getText().toString();
                                __sys_emm = emailEditText.getText().toString();

                                progressBar.setVisibility(View.INVISIBLE);

                                // User has already validated his/her email address proceed
                                if (user.getBoolean("emailVerified")) {

                                    String tempUserCat = user.get("userCat").toString();

                                    if (tempUserCat.equals("admin")) {

                                        // Redirect to the AdminMain
                                        Intent mainIntent = new Intent(GeneralLogin.this, AdminMain.class);
                                        GeneralLogin.this.startActivity(mainIntent);
                                        GeneralLogin.this.finish();
                                    } else if (tempUserCat.equals("user")) {

                                        // Redirect to the FavUsers
                                        Intent mainIntent = new Intent(GeneralLogin.this, FavUsers.class);
                                        GeneralLogin.this.startActivity(mainIntent);
                                        GeneralLogin.this.finish();
                                    }
                                }

                                // User has not verified his/her email address, cancel
                                else {

                                    // Call email-ver dialog
                                    createEmailVerDg();
                                    loginBtn.setVisibility(View.VISIBLE);
                                }


                            } else {

                                loginBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);

                                Snackbar mSnackbar = Snackbar.make(v, "Incorrect Login", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null);
                                View snackView = mSnackbar.getView();
                                snackView.setBackgroundColor(Color.RED);
                                TextView snackText = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                                snackText.setGravity(Gravity.CENTER_HORIZONTAL);
                                snackText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                mSnackbar.show();

                                emailEditText.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
                                passwordEditText.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);

                                
                            }
                        }
                    });

        } else {

            loginBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            // Network error
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    /*  */
    private void resendEmailParse(String usrID) {

        // Check the device internet connection
        if (DeviceConnection.testConnection(context)) {

            ParseUser.logInInBackground(__sys_emm, __sys_ppp, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {

                    if (e == null) {


                        /* Resetting the email-address trigger the email re-sent
                         * If the pre-existent email is substitute with itself
                         * the email notification won't be sent. This is why I set the address
                         * to a fake one first. */

                        user.setEmail("placeholder@rcmplexus.com");

                        user.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    // Keep it mute

                                } else {

                                    // <marked>
                                    if (e.getCode() == 209) {
                                        ParseProcessor.createInvalidSessionD(context);
                                    } else {
                                        // Parse error, prompt with feedback
                                        Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                                    }
                                    // Force user out
                                    ParseUser outUser = ParseUser.getCurrentUser();
                                    outUser.logOut();
                                }
                            }
                        });

                        user.setEmail(__sys_emm);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {


                                    toast = Toast.makeText(context, "Message Sent",
                                            Toast.LENGTH_SHORT);
                                    toast.show();


                                    logOutParse();

                                } else {

                                    // <marked>
                                    if (e.getCode() == 209) {
                                        ParseProcessor.createInvalidSessionD(context);
                                    } else {
                                        // Parse error, prompt with feedback
                                        Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                                    }

                                    logOutParse();
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

                        logOutParse();


                    }
                }
            });

        } else {

            loginBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            // Network error
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();

            logOutParse();
        }

        ParseUser outUsr = ParseUser.getCurrentUser();
        outUsr.logOut();
    }

    /*  */
    private void logOutParse() {
        ParseUser logOutUsr = ParseUser.getCurrentUser();
        if (logOutUsr != null && logOutUsr.isAuthenticated()) {
            logOutUsr.logOut();

        }

    }

    /* Sends pass reset link to email address */
    private void resetPass(final String email, final View myDialogView) {

        if (DeviceConnection.testConnection(context)) {

            toast = Toast.makeText(context, "Sending reset link...", Toast.LENGTH_LONG);
            toast.show();

            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        toast = Toast.makeText(context, "Reset link Sent", Toast.LENGTH_LONG);
                        toast.show();

                        // Close origin dialog
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(myDialogView.getWindowToken(), 0);
                        dMenuResetPsw.dismiss();
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
