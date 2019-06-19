package brood.com.medcrawler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;


public class ParseProcessor {


    Context context;
    private SpecAdapter specAdapter;


    /* Class accessor - to get the specAdapter and use it for other widgets*/
    public SpecAdapter getSpecAdapter() {

        return specAdapter;
    }


    /* Logout Parse */
    public static void logOut() {
        ParseUser.logOut();
    }

//    /* Create Parse UserAdminMain */
//    public void createParseUser(final String email, final String password) {
//
//        // Check the device internet connection
//        if (DeviceConnection.testConnection(context)) {
//
//            ParseUser user = new ParseUser();
//            user.setUsername(email);
//            user.setPassword(password);
//            user.setEmail(email);
//
//            // Set up ACL for the new user
//            ParseACL userACL = new ParseACL();
//            userACL.setRoleWriteAccess("Admin", true);
//            user.setACL(userACL);
//
//
//            // other fields can be set just like this
//            //user.put("phone", "650-253-0000");
//
//            user.signUpInBackground(new SignUpCallback() {
//                public void done(ParseException e) {
//                    if (e == null) {
//
//                        Toast.makeText(context, "UserAdminMain created", Toast.LENGTH_LONG).show();
//                    } else {
//
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//
//        } else {
//
//            // Prompt the user with the network error
//            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
//        }
//
//    }

    /* Create Specialty */
    public static void createSpecialty(final Dialog dialog, final ProgressBar bar,
                                       final android.support.v7.widget.AppCompatEditText specEditText,
                                       final String specEditValue,
                                       final android.support.v7.widget.AppCompatButton yesBtn,
                                       final Context context,
                                       final String specType) {

        // Capitalize all text in the edit-text field
        final String captString = WordUtils.capitalizeFully(specEditValue);
        //String compareString = WordUtils.uncapitalize(specEditText.getText().toString());


        /**************************** Check if the spec entry already exists **********************/

        // Check the device internet connection
        if (DeviceConnection.testConnection(context)) {

            // Set the progress bar visible
            bar.setVisibility(View.VISIBLE);

            // Disable the yes btn
            yesBtn.setEnabled(false);

            ParseQuery<ParseObject> checkObjQuery = ParseQuery.getQuery("Spec");
            checkObjQuery.whereEqualTo("specName", captString);
            checkObjQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject obj, ParseException e) {

                    // No Parse errors
                    if (e == null) {

                        // Check if entry already exists
                        if (obj.getString("specName").toString().equals(captString)) {

                            Toast.makeText(context, "Specialty '" + captString + "' already exists",
                                    Toast.LENGTH_LONG).show();

                            // Set the progress bar invisible
                            bar.setVisibility(View.INVISIBLE);

                            // Enable the yes btn
                            yesBtn.setEnabled(true);

                            // Dismiss the dialog
                            dialog.dismiss();
                        }
                    }

                    // Yes Parse check for existent entry errors
                    else {

                        // This means the entry doesn't exist (so add it to Parse)
                        if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                            // Create a Parse object, pass the specName var and save it on the server
                            ParseObject specObject = new ParseObject("Spec");
                            specObject.put("specName", captString);

                            // Add the new entry here
                            if (specType.contains("Medical")) {
                                specObject.put("specType", "medical");
                            }
                            else if (specType.contains("Dental")) {
                                specObject.put("specType", "dental");
                            }

                            // Save Spec in background
                            specObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {

                                        /* No Parse errors on create a new entry*/
                                        // Set dialog progress bar visibility off
                                        bar.setVisibility(View.INVISIBLE);

                                        // Clear the spec editText field
                                        specEditText.setText("");

                                        // Enable the yes btn
                                        yesBtn.setEnabled(true);

                                        // Dismiss dialog
                                        dialog.dismiss();

                                        // Give feedback about successful spec addition
                                        Toast.makeText(context, "Specialty successfully saved", Toast.LENGTH_LONG).show();
                                    } else {

                                        /* Yes Parse Errors on create new entry */

                                        // Set dialog progress bar visibility off
                                        bar.setVisibility(View.INVISIBLE);

                                        // Enable the yes btn
                                        yesBtn.setEnabled(true);

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

                        // If this is a different error than OBJECT_NOT_FOUND just display it
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

                }
            });

        } else {

            /* Network Error */

            //Set dialog progress bar visibility off
            bar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
        }

    }

//    /* specParseList is already implemented in the AdminUserSpecialty class - keep active for ref.*/
//    public List<Specialty> specParseList(final ProgressBar progressBar, final RecyclerView rw) {
//
//        final List<Specialty> tempRetList = new ArrayList<>();
//
//        // Query the Spec class
//        ParseQuery query = new ParseQuery("Spec");
//        query.orderByAscending("specName"); // sort alphabetically by name
//
//        // Check the device internet connection
//        if (DeviceConnection.testConnection(context)) {
//
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> list, ParseException e) {
//
//                    if (e == null) {
//
//                        // Specialty object to work with temporarily
//                        Specialty tempSpecObj;
//
//                        // Iterate over the returning Parse list and assign values to a Specialty then
//                        // nest each Specialty object into a list that can be returned for data population
//                        for (ParseObject tempObj : list) {
//
//                            // Populate a Spec obj
//                            tempSpecObj = new Specialty(tempObj.get("specName").toString(),
//                                    tempObj.getObjectId().toString());
//
//                            // Add temp Spec. obj to the returning method's list
//                            tempRetList.add(tempSpecObj);
//                        }
//
//                        // Create an adapter for rw and assign it
//
//
//                        //specAdapter = new SpecAdapter(tempRetList, tempRetList, context);
//
//                        rw.setAdapter(specAdapter);
//                        //specAdapter.notifyDataSetChanged();
//
//                        //Set dialog progress bar visibility off
//                        progressBar.setVisibility(View.INVISIBLE);
//
//                    } else {
//
//                        // Show Parse error
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//            });
//
//        } else {
//
//            /* Network Error */
//
//            //Set dialog progress bar visibility off
//            progressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(context, "Network Error, check Internet connection", Toast.LENGTH_LONG).show();
//
//        }
//
//        return tempRetList;
//    }

    /* Delete an object by ID */
    public static void deleteSpec(final String objId, final ProgressBar progressBar, final Dialog dialog,
                                  final Button widget, final Button yesToBlock, final Context context) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Spec");

        // Set progress bar visible
        progressBar.setVisibility(View.VISIBLE);

        // Check for network connection
        if (DeviceConnection.testConnection(context)) {

            // Block button so users can't spam this method (create more queries than needed)
            yesToBlock.setEnabled(false);

            query.getInBackground(objId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {

                    // Check if the object exists or not
                    if (e == null) {

                        // Object exists, no Parse errors
                        // Delete the object
                        parseObject.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    // Look for all subspecialties that contain this spec's objId
                                    ParseQuery<ParseObject> relSubQ = ParseQuery.getQuery("SubSpec");
                                    relSubQ.whereEqualTo("parentId", objId);

                                    relSubQ.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {

                                            // Check if there are subspecialties attached to this spec
                                            // only if there are, perform a deleteAllInBackground
                                            if (!list.isEmpty()) {

                                                // There are subspecs belonging to the recently deleted
                                                // spec, delete all of them
                                                ParseObject.deleteAllInBackground(list, new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {

                                                        if (e == null) {

                                                            // Success prompt the user and modify UI
                                                            // Set progress bar invisible
                                                            progressBar.setVisibility(View.INVISIBLE);

                                                            // User feedback
                                                            Toast.makeText(context, "Specialty deleted successfully",
                                                                    Toast.LENGTH_LONG).show();

                                                            // We set the Spec widget back to select for future checking
                                                            // value needs to stay at "select" there is a check for it in the dialog
                                                            widget.setText("select");

                                                            // Unblock yes button
                                                            yesToBlock.setEnabled(true);

                                                            // Dismiss dialog
                                                            dialog.dismiss();
                                                        } else {


                                                            // This is critical, and it shouldn't happen
                                                            // if it does all the subspecs need
                                                            // to be cleaned by hand
                                                            // <marked>
                                                            if (e.getCode() == 209) {
                                                                ParseProcessor.createInvalidSessionD(context);
                                                            } else {
                                                                // Parse error, prompt with feedback
                                                                Toast.makeText(context, e.getMessage() + " " + e.getCode(), Toast.LENGTH_LONG).show();
                                                            }

                                                            // We set the Spec widget back to select for future checking
                                                            // value needs to stay at "select" there is a check for it in the dialog
                                                            widget.setText("select");

                                                            // Unblock yes button
                                                            yesToBlock.setEnabled(true);
                                                        }
                                                    }
                                                });
                                            }

                                            // If there are no subspecialties attached to this spec
                                            // delete the spec and prompt the user with feedback
                                            else {

                                                // User feedback
                                                Toast.makeText(context, "Specialty deleted successfully",
                                                        Toast.LENGTH_LONG).show();

                                                // We set the Spec widget back to select for future checking
                                                // value needs to stay at "select" there is a check for it in the dialog
                                                widget.setText("select");

                                                // Unblock yes button
                                                yesToBlock.setEnabled(true);

                                                // Dismiss dialog
                                                dialog.dismiss();
                                            }
                                        }
                                    });

                                }

                                // Error deleting Spec
                                else {

                                    // Set progress bar invisible
                                    progressBar.setVisibility(View.INVISIBLE);

                                    // Unblock yes button
                                    yesToBlock.setEnabled(true);

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

                        // Parse error when searching for the specialty, object doesn't exist
                    } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                        // Object doesn't exist anymore, somebody (admin) erased it

                        Toast.makeText(context, "Specialty has been already deleted", Toast.LENGTH_LONG).show();


                        // Set progress bar invisible
                        progressBar.setVisibility(View.INVISIBLE);

                        // Unblock yes button
                        yesToBlock.setEnabled(true);

                        // Unknown Parse error when searching for the specialty
                    } else {

                        // Set progress bar invisible
                        progressBar.setVisibility(View.INVISIBLE);

                        // Unblock yes button
                        yesToBlock.setEnabled(true);

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

    /* Create a Subspecialty */
    public static void createSubSpec(final String parentId, final String subSpecValue,
                                     final ProgressBar progressBar, final Dialog dialog,
                                     final Button yesToBlock, final android.support.v7.widget.
            AppCompatEditText subSpecEditText,
                                     final Context context) {

        final String captValue = WordUtils.capitalizeFully(subSpecValue);

        if (DeviceConnection.testConnection(context)) {

            // Filter only repeated subspecs (if they exists) == subSpecValue && == parentId
            ParseQuery<ParseObject> matchingSubSpecs = ParseQuery.getQuery("SubSpec");
            matchingSubSpecs.whereEqualTo("subSpecName", captValue);
            matchingSubSpecs.whereEqualTo("parentId", parentId);

            matchingSubSpecs.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    if (e == null) {

                        // List is empty create subspec
                        Boolean listState = list.isEmpty();

                        if (listState) {

                            // List is empty, no repetition, create a new Subspec
                            ParseObject specObject = new ParseObject("SubSpec");
                            specObject.put("subSpecName", captValue);
                            specObject.put("parentId", parentId);

                            specObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {

                                /* No Parse errors on create a new entry*/
                                        // Set dialog progress bar visibility off
                                        progressBar.setVisibility(View.INVISIBLE);

                                        // Clear the spec editText field
                                        subSpecEditText.setText("");

                                        // Enable the yes btn
                                        yesToBlock.setEnabled(true);

                                        // Dismiss dialog
                                        dialog.dismiss();

                                        // User feedback success
                                        Toast.makeText(context,
                                                "Subspecialty saved successfully",
                                                Toast.LENGTH_LONG).show();

                                    } else {

                                /* Unknown Parse error on save subspec */

                                        // Set dialog progress bar visibility off
                                        progressBar.setVisibility(View.INVISIBLE);

                                        // Enable the yes btn
                                        yesToBlock.setEnabled(true);

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

                            // Set dialog progress bar visibility off
                            progressBar.setVisibility(View.INVISIBLE);

                            // Enable the yes btn
                            yesToBlock.setEnabled(true);

                            // Subspec exists
                            Toast.makeText(context, "Subspecialty already exists", Toast.LENGTH_LONG).show();

                            // Dismiss dialog
                            dialog.dismiss();
                        }

                    }

                    // Parse error
                    else {

                        // Set dialog progress bar visibility off
                        progressBar.setVisibility(View.INVISIBLE);

                        // Enable the yes btn
                        yesToBlock.setEnabled(true);

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

    /* Select subspec on Spec ID -- REPEATED*/
    public static void selectSubspec(final Context context, final String parentId,
                                     final RecyclerView rw, final ProgressBar progressBar, final Dialog dialog,
                                     final android.support.v7.widget.AppCompatButton widget) {

        // Add the results into a list and expot it for the adapter
        final List<Subspecialty> tempList = new ArrayList<Subspecialty>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("SubSpec");

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

                        // Create an adapter for rw and assign it to the rw

                        //rw.setAdapter(new SubspecAdapter(tempList, tempList, context, dialog, widget));

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

    public static void deleteSubspec(final String subSpecId, final Context context,
                                     final ProgressBar progressBar, final Dialog dialog,
                                     final android.support.v7.widget.AppCompatButton widget,
                                     final android.support.v7.widget.AppCompatButton button) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("SubSpec");

        query.orderByAscending("SubSpec");

        // The Yes button should not be spammed
        button.setEnabled(false);

        // Check for local connection
        if (DeviceConnection.testConnection(context)) {

            // Connection is OK
            query.getInBackground(subSpecId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {


                    if (e == null) {

                        // Object exists, no Parse error
                        parseObject.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    // Set progress bar invisible
                                    progressBar.setVisibility(View.INVISIBLE);

                                    // Set the subspec widget's value back to "select"
                                    widget.setText("select");

                                    // Enable the yes button
                                    button.setEnabled(true);

                                    // Cancel dialog
                                    dialog.dismiss();

                                    // Deletion completed
                                    Toast.makeText(context, "Subspecialty successfully deleted",
                                            Toast.LENGTH_LONG).show();

                                } else {

                                    // Set progress bar invisible
                                    progressBar.setVisibility(View.INVISIBLE);

                                    // Enable the yes button
                                    button.setEnabled(true);

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

                        // Enable the yes button
                        button.setEnabled(true);

                        // Set progress bar invisible
                        progressBar.setVisibility(View.INVISIBLE);

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

    /* This method has been implemented in AdminKeyGen activity */
    public static void genKey(final Integer numOfKeys, final String userCat, final Context context,
                              final AdminKeyGenAdapter adapter, final ProgressBar progressBar,
                              final RecyclerView rw, final Activity activity) {

        // Set UI elements for key generation
        rw.setVisibility(View.GONE);
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
                                        Toast.makeText(context, "Key/s saved successfully",
                                                Toast.LENGTH_LONG).show();

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

                                                    // Update view and adapter here !!

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

//    /* This one is implemented in the AdminKeyGen activity */
//    public static void readKeyGen(final Context context, AdminKeyGenAdapter adapter,
//                                  RecyclerView rw) {
//
//        // Local variables
//        KeyRow myKeyRow; // instantiate later
//        final List<KeyRow> myKeyList = new ArrayList<>();
//
//        ParseQuery<ParseObject> readKeyQ = ParseQuery.getQuery("RegKeys");
//        readKeyQ.orderByAscending("userCategory");
//        readKeyQ.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//
//                // No errors, got the key list
//                if (e == null) {
//
//                    for (ParseObject iter : list) {
//                        myKeyList.add(new KeyRow(iter.get("regKey").toString(),
//                                iter.getObjectId(), iter.get("userCategory").toString(), true));
//                    }
//
//                    // Set up the adapter and the rw
//                    //adapter = new AdminKeyGenAdapter(myKeyList, context);
//
//                }
//
//                // Parse errors, show the error
//                else {
//
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }

    public static void moveToSharedKeys(final String keyId, final Context context,
                                        final AdminKeyGenAdapter adapter, final int position,
                                        final List<KeyRow> localList, final TextView placeHolder,
                                        final String keyOrigin) {

        // Check Internet state
        if (DeviceConnection.testConnection(context)) {

            // Internet works
            // 1. Make sure that the param key exists
            ParseQuery<ParseObject> checkKey = ParseQuery.getQuery("RegKeys");
            checkKey.getInBackground(keyId, new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject parseObject, ParseException e) {

                    // No Parse error, key exists
                    if (e == null) {

                        // Success, key exists
                        // 1.1 Check the sharedKeys to see if somehow, this key has already been added
                        ParseQuery<ParseObject> checkSharedKeys = ParseQuery.getQuery("SharedKeys");
                        checkSharedKeys.whereEqualTo("sharedKey", parseObject.get("regKey"));
                        checkSharedKeys.countInBackground(new CountCallback() {
                            @Override
                            public void done(int i, ParseException e) {

                                // Count has completed with no errors
                                if (e == null) {

                                    // The key has already been shared for some strange reason
                                    if (i > 0) {

                                        // Show error
                                        Toast.makeText(context, "Key has already been shared",
                                                Toast.LENGTH_LONG).show();
                                        //todo: UI update
                                    } else {

                                        // The key hasn't been shared, create a parse object and save it
                                        ParseObject sharedKey = new ParseObject("SharedKeys");
                                        sharedKey.put("sharedKey", parseObject.get("regKey"));
                                        sharedKey.put("userCategory", parseObject.get("userCategory"));

                                        // Save shared object
                                        sharedKey.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                                // Success on save shared object
                                                if (e == null) {

                                                    // Now you can delete the object from regKey
                                                    parseObject.deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {

                                                            // Delete successful
                                                            if (e == null) {

                                                                // Determine if visual feedback
                                                                // is necessary based on keyOrigin param
                                                                if(keyOrigin.equals("screen_share")) {

                                                                    // Success on deleting regKey object
                                                                    Toast.makeText(context,
                                                                            "Key successfully shared",
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                                else {

                                                                    // Do nothing (don't show toast)
                                                                }


                                                                // Delete the key from the list
                                                                localList.remove(position);

                                                                // Notify adapter
                                                                adapter.notifyDataSetChanged();

                                                                // If adapter is empty show placeHolder
                                                                if(adapter.getItemCount() == 0) {

                                                                    // Set list placeHolder to invisible
                                                                    placeHolder.setVisibility(
                                                                            View.VISIBLE);
                                                                } else {

                                                                    placeHolder.setVisibility(
                                                                            View.GONE);
                                                                }

                                                            }

                                                            // Error deleting
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
                                                // Parse error on save object
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
                                }

                                // Count has Parse errors
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

                    // Can't find the key anymore, it has prob. been deleted
                    else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                        // Show object deleted error
                        Toast.makeText(context, "Key has already been removed", Toast.LENGTH_LONG).show();
                    }

                    // Parse error
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

        // No Internet
        else {

            // Show network error
            Toast.makeText(context, "Network error, check Internet connection", Toast.LENGTH_LONG).show();
        }

    }

    /* Force the invoker to relog */
    public static void createInvalidSessionD(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        AlertDialog dialog;

        // Get the layout inflater
        LayoutInflater inflater = LayoutInflater.from(context);
        final View myDialogView = inflater.inflate(R.layout.d_menu_invsession_error, null);

        // Ok button
        AppCompatButton okBtn = (AppCompatButton)myDialogView.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Logout user
                ParseUser currUsr = ParseUser.getCurrentUser();
                currUsr.logOut();

                // Redirect to GeneralLogin
                Intent invSessionIntent = new Intent(context, GeneralLogin.class);
                invSessionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(invSessionIntent);
                ((Activity)context).finish();
            }
        });

        builder.setView(myDialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


}
