package brood.com.medcrawler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AdminUserSpecialty extends AppCompatActivity {

    private Context context;
    private AlertDialog dMenuAddSpec;
    private AlertDialog dMenuSelSpec;
    private AlertDialog dMenuSelSubspec;
    private AlertDialog dMenuDelSpec;
    private AlertDialog dMenuAddSubSpec;
    private AlertDialog dMenuDelSubspec;
    private AppCompatEditText specEditText;
    private RecyclerView specRecyclerView;
    private RecyclerView subSpecRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SpecAdapter specAdapter;
    private SubspecAdapter subspecAdapter;
    private ProgressBar delSpecProgressB;
    private String specName;
    private String specId;
    private String subspecName;
    private String subspecId;
    private String widgetName;
    private AppCompatButton delSpecWidget;
    private Button selSpec_W;
    private AppCompatButton specWidgetdelSubspec;
    private AppCompatButton selSubspecWidget;
    private Button delSpecBtn;
    private Button addSubSpecBtn;
    private Button delSubSpecBtn;
    private AppCompatEditText subSpecEditTxt;
    public static String CALLER = "specEditor";
    private Spinner specTypeSpinner;
    private AppCompatButton addSpecBtn;
    private int buttonId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_specialty);

        // Get the context
        context = this;

        // We use Toolbars instead of ActionBars as the latter was deprecated
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
            getSupportActionBar().setTitle("Specialty Editor"); // <---- change this to dyn. username
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        // Hide the keyboard as soon as the activity is loaded
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Target the spec_type_spinner
        specTypeSpinner = (Spinner)findViewById(R.id.spec_type_spinner);
        // Create an ArrayAdapter using the auto-population method
        ArrayAdapter<CharSequence> adapter_spec_type = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_list_item_1, populateSpecType());
        // Apply the adapter to the spinner
        specTypeSpinner.setAdapter(adapter_spec_type);

        // Target the Spec EditText
        specEditText = (android.support.v7.widget.AppCompatEditText) findViewById(R.id.admin_userSpec_spec_edit);

        // Target AddSpec button
        addSpecBtn = (android.support.v7.widget.
                AppCompatButton) findViewById(R.id.add_spec_btn);

        addSpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the specEditText is empty or has more (or equal to) 25 characters
                if (specEditText.getText().toString().isEmpty()) {

                    // Error empty specEditText
                    Toast.makeText(context, "Specialty can't be empty", Toast.LENGTH_LONG).show();

                } else if (specEditText.getText().toString().equals(" ")) {

                    Toast.makeText(context, "Specialty can't be empty", Toast.LENGTH_LONG).show();
                } else if (specEditText.getText().toString().length() > 50) {  // max med input value

                    // Error too many chars in the specEditText
                    Toast.makeText(context, "Specialty max character excess", Toast.LENGTH_LONG).show();

                } else {

                    // Lower  soft-keyboard onClick (for some reason it doesn't work on a separate
                    // method, no idea why
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);


                    // No errors, instantiate Spec dialog
                    createAddSpecDg(specEditText.getText().toString().trim());

                }

            }
        });

        // Target DeleteSpec widged (it's still a button, but it's meant for text display)
        delSpecWidget = (android.support.v7.widget.
                AppCompatButton) findViewById(R.id.add_spec_widget);

        delSpecWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set btn caller ID to decide where to return result (see specEditorRcv broadcast receiver)
                buttonId = delSpecWidget.getId();

                // Launch the createSelSpecDg
                createSelSpecDg(delSpecWidget);
            }
        });

        // Target DelSubspecWidget (it's still a button, but it's meant for text display)
        specWidgetdelSubspec = (android.support.v7.widget.AppCompatButton) findViewById(R.id.del_sub_spec_widget);
        specWidgetdelSubspec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set btn caller ID to decide where to return result (see specEditorRcv broadcast receiver)
                buttonId = specWidgetdelSubspec.getId();

                // Launch the createSelSpecDg
                createSelSpecDg(specWidgetdelSubspec);
            }
        });

        // Target DeleteSpec Btn
        delSpecBtn = (Button) findViewById(R.id.delete_spec_btn);

        delSpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call the dialog for spec deletion
                createDelSpecDg();
            }
        });

        // Target the add_sub_spec_widget
        selSpec_W = (Button) findViewById(R.id.add_sub_spec_widget);
        selSpec_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set btn caller ID to decide where to return result (see specEditorRcv broadcast receiver)
                buttonId = selSpec_W.getId();

                // Load the createSelSpecDg() method
                createSelSpecDg(selSpec_W);
            }
        });

        // Target the select_sub_spec_widget
        selSubspecWidget = (android.support.v7.widget.AppCompatButton) findViewById(R.id.sel_sub_spec_widget);
        selSubspecWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1. Check if the subspec widget name is empty
                if (specWidgetdelSubspec.getText().toString().isEmpty() || specWidgetdelSubspec.getText().
                        toString().toLowerCase().equals("select")) {
                    // Error, prompt toast
                    Toast.makeText(context, "Please select a specialty first", Toast.LENGTH_LONG).show();
                } else {

                    //2. Call the create subspec_select menu
                    createSelSubspecDg();
                }

            }
        });

        // Target the subSpec editText
        subSpecEditTxt = (android.support.v7.widget.AppCompatEditText) findViewById(R.id.admin_userSubSpec_sub_spec_edit);

        // Target the addSubSpecBtn
        addSubSpecBtn = (Button) findViewById(R.id.add_sub_spec_btn);
        addSubSpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1. Check editText value for integrity
                if (subSpecEditTxt.getText().toString().isEmpty()) {

                    // User feedback on empty sub-spec editText
                    Toast.makeText(context, "Subspecialty can't be empty", Toast.LENGTH_LONG).show();
                } else if (subSpecEditTxt.getText().toString().equals(" ")) {

                    Toast.makeText(context, "Subspecialty can't be empty", Toast.LENGTH_LONG).show();
                }

                // 2. Check add_sub_spec_widget for selected value
                else if (selSpec_W.getText().toString().isEmpty() || selSpec_W.getText()
                        .toString().toLowerCase().equals("select")) {

                    // User feedback on spec not selected
                    Toast.makeText(context, "Please select a parent Specialty", Toast.LENGTH_LONG).show();
                } else if (subSpecEditTxt.getText().toString().length() > 25) {

                    // Error too many chars in the specEditText
                    Toast.makeText(context, "Subspecialty max character excess", Toast.LENGTH_LONG).show();

                } else {

                    // 3. Launch sub_spec addition dialog (pass in the parent spec's id)
                    createAddSubSpecDg(subSpecEditTxt.getText().toString().trim(), specId);
                }
            }
        });

        // Target the deleteSubSpec button
        delSubSpecBtn = (Button) findViewById(R.id.delete_sub_spec_btn);
        delSubSpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1. Check if the subspec widget has a value
                if (selSubspecWidget.getText().toString().isEmpty() || selSubspecWidget.getText().
                        toString().toLowerCase().equals("select")) {

                    // Prompt the user with subspec value selection
                    Toast.makeText(context, "Please select a subspecialty to delete",
                            Toast.LENGTH_LONG).show();
                } else {

                    // All good, create del subspecialty  dialog
                    createDelSubSpecDg();
                }

            }
        });

        // Broadcasters
        registerReceiver(specEditorRcv, new IntentFilter("s_Editor_spec"));
        registerReceiver(subSpecEditorRcv, new IntentFilter("subspec_Editor_spec"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CALLER = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {

            unregisterReceiver(specEditorRcv);
            unregisterReceiver(subSpecEditorRcv);

        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_user_specialty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Settings
        if (id == R.id.admin_spec_editor_about) {

            //Toast.makeText(context, "About", Toast.LENGTH_SHORT).show();
            // Launch an about dialog
            About about = new About();
            about.launch(context);

            return true;
        }

        else if (id == R.id.admin_spec_editor_logout) {

            ParseUser.logOut();

            // Redirect to the AdminMain
            Intent mainIntent = new Intent(AdminUserSpecialty.this, GeneralLogin.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AdminUserSpecialty.this.startActivity(mainIntent);
            AdminUserSpecialty.this.finish();
        }

        // Return arrow
        else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Make a Spec */
    private void createAddSpecDg(String specToAdd) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get SpecEditText's value
        final String tempSpecValue = specToAdd;

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        View myDialogView = inflater.inflate(R.layout.d_menu_add_spec, null);

        // Set the Progress Bar
        final ProgressBar progressBar = (ProgressBar) myDialogView.findViewById(R.id.add_spec_progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

        // Set value of SpecEditText to name_spec_textview
        TextView tempSpecName = (TextView) myDialogView.findViewById(R.id.name_spec_textview);

        final android.support.v7.widget.AppCompatButton btnYes = (android.support.v7.widget.AppCompatButton) myDialogView.findViewById(R.id.aff_spec_btn);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add to Parse
                ParseProcessor.createSpecialty(dMenuAddSpec, progressBar, specEditText, tempSpecValue, btnYes, context, specTypeSpinner.getSelectedItem().toString());

            }
        });
        android.support.v7.widget.AppCompatButton btnNo = (android.support.v7.widget.AppCompatButton) myDialogView.findViewById(R.id.neg_spec_btn);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Specialty not added", Toast.LENGTH_SHORT).show();
                dMenuAddSpec.dismiss();
            }
        });


        tempSpecName.setText(tempSpecValue);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuAddSpec = builder.create();
        dMenuAddSpec.show();

    }

    private void createAddSubSpecDg(final String subspecName, final String specId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get SubSpec EditText's value
        final String tempSubSpecValue = subspecName;

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        View myDialogView = inflater.inflate(R.layout.d_menu_add_sub_spec, null);

        // Set the Progress Bar
        final ProgressBar progressBar = (ProgressBar) myDialogView.findViewById(R.id.add_spec_progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

        // Set values for the
        TextView tempSubSpecName = (TextView) myDialogView.findViewById(R.id.select_sub_spec_textview);
        TextView tempSpecName = (TextView) myDialogView.findViewById(R.id.select_spec_textview);

        final android.support.v7.widget.AppCompatButton btnYes = (android.support.v7.widget.AppCompatButton) myDialogView.findViewById(R.id.aff_spec_btn);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add subspec to Parse
                ParseProcessor.createSubSpec(specId, tempSubSpecValue, progressBar,
                        dMenuAddSubSpec, btnYes, subSpecEditTxt, context);

            }
        });
        android.support.v7.widget.AppCompatButton btnNo = (android.support.v7.widget.AppCompatButton) myDialogView.findViewById(R.id.neg_spec_btn);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Specialty not added", Toast.LENGTH_SHORT).show();
                dMenuAddSubSpec.dismiss();
            }
        });


        // Set the name for the subspec and spec textviews (in this meny) - the dynamic fields
        tempSubSpecName.setText(tempSubSpecValue);
        tempSpecName.setText(selSpec_W.getText().toString());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuAddSubSpec = builder.create();
        dMenuAddSubSpec.show();

    }

    private void createSelSpecDg(Button widget) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View myDialogView = inflater.inflate(R.layout.d_menu_select_spec, null);

        // Set the Progress Bar
        delSpecProgressB = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        delSpecProgressB.setIndeterminate(true);
        delSpecProgressB.setVisibility(View.INVISIBLE);

        // Set dialog buttons
        android.support.v7.widget.AppCompatButton cancelBtn = (android.support.v7.widget.
                AppCompatButton) myDialogView.findViewById(R.id.cancel_spec_btn);

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
        specParseList(delSpecProgressB, specRecyclerView, widget);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myDialogView);

        dMenuSelSpec = builder.create();
        dMenuSelSpec.show();
    }

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
        android.support.v7.widget.AppCompatButton cancelBtn = (android.support.v7.widget.
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
        dMenuSelSubspec.show();

    }

    private void createDelSpecDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        View myDialogView = inflater.inflate(R.layout.d_menu_del_spec, null);

        // Set the Progress Bar
        final ProgressBar progressBar = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

        // Set value of SpecEditText to name_spec_textview
        TextView tempSpecName = (TextView) myDialogView.findViewById(R.id.name_del_spec_textview);

        // Make sure that the user has selected a spec to delete first before deleting it
        if (delSpecWidget.getText().toString().isEmpty() || delSpecWidget.getText().toString().
                toLowerCase().equals("select")) {

            // Throw an error asking the user to select a Spec first
            Toast.makeText(context, "Can't delete. No Specialty selected",
                    Toast.LENGTH_LONG).show();
        }

        // User has selected a spec
        else {

            // If there is a Specialty name selected transport it to the delete spec. dialog
            tempSpecName.setText(delSpecWidget.getText());

            // Target the Yes & No buttons
            final android.support.v7.widget.AppCompatButton btnYes = (android.support.v7.widget.
                    AppCompatButton) myDialogView.findViewById(R.id.aff_del_spec_btn);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Reset other spec widgets if they contain the same spec-name that is about
                    // to get deleted
                    if (selSpec_W.getText().toString().equals(delSpecWidget.getText().toString())) {
                        selSpec_W.setText("select");
                    }

                    if (specWidgetdelSubspec.getText().toString().equals(delSpecWidget.getText().toString())) {
                        specWidgetdelSubspec.setText("select");
                    }

                    // Yes button pressed, call delete on Parse
                    ParseProcessor.deleteSpec(specId, progressBar, dMenuDelSpec, delSpecWidget,
                            btnYes, context);
                }
            });

            android.support.v7.widget.AppCompatButton btnNo = (android.support.v7.widget.
                    AppCompatButton) myDialogView.findViewById(R.id.neg_del_spec_btn);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // No button pressed
                    dMenuDelSpec.dismiss();
                }
            });

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(myDialogView);

            dMenuDelSpec = builder.create();
            dMenuDelSpec.show();
        }


    }

    private void createDelSubSpecDg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        View myDialogView = inflater.inflate(R.layout.d_menu_del_subspec, null);

        // Set the Progress Bar
        final ProgressBar progressBar = (ProgressBar) myDialogView.findViewById(R.id.del_spec_progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

        // Set value of SpecEditText to name_spec_textview
        TextView tempSubspecName = (TextView) myDialogView.findViewById(R.id.name_del_subspec_textview);
        tempSubspecName.setText(selSubspecWidget.getText().toString());

        // Target the Yes & No buttons
        final android.support.v7.widget.AppCompatButton btnYes = (android.support.v7.widget.
                AppCompatButton) myDialogView.findViewById(R.id.aff_del_spec_btn);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Yes button pressed, call delete subspec on Parse
                ParseProcessor.deleteSubspec(subspecId, context, progressBar, dMenuDelSubspec,
                        selSubspecWidget, btnYes);

            }
        });

        android.support.v7.widget.AppCompatButton btnNo = (android.support.v7.widget.
                AppCompatButton) myDialogView.findViewById(R.id.neg_del_spec_btn);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No button pressed
                dMenuDelSubspec.dismiss();
            }
        });

        builder.setView(myDialogView);
        dMenuDelSubspec = builder.create();
        dMenuDelSubspec.show();
    }


    /* I don't like having to implement this method outside of ParseProcessor, but I can't seem
     * to be able to call it from Parse without affecting the SpecAdapter filter. I'll take another
     * swing at it later.
     */
    public List<Specialty> specParseList(final ProgressBar progressBar, final android.support.v7
            .widget.RecyclerView rw, final Button widget) {

        final List<Specialty> tempRetList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        // Query the Spec class
        ParseQuery query = new ParseQuery("Spec");
        query.orderByAscending("specName");

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
                        //specAdapter.notifyDataSetChanged();



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

    private BroadcastReceiver specEditorRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            specName = intent.getStringExtra("textValue");
            specId = intent.getStringExtra("specId");

            // Return spec name into the caller button/widget
            if (buttonId == selSpec_W.getId()) {
                selSpec_W.setText(specName);
            } else if (buttonId == delSpecWidget.getId()) {
                delSpecWidget.setText(specName);
            } else if (buttonId == specWidgetdelSubspec.getId()) {
                specWidgetdelSubspec.setText(specName);
            }
        }
    };

    private BroadcastReceiver subSpecEditorRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            subspecName = intent.getStringExtra("textValue");
            subspecId = intent.getStringExtra("subSpecId");

            selSubspecWidget.setText(subspecName);

        }
    };

    // Populates the spec type spinner
    private List<CharSequence> populateSpecType() {

        // Local ArrayAdapter
        List<CharSequence> tempRetList = new ArrayList<CharSequence>();
        // Add states to the returning list
        tempRetList.add("Medical Specialty");
        tempRetList.add("Dental Specialty");

        return tempRetList;
    }
}
