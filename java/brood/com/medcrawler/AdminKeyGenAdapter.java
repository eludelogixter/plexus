package brood.com.medcrawler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AdminKeyGenAdapter extends RecyclerView.Adapter<AdminKeyGenAdapter.ViewHolder>{
    protected List<KeyRow> keys = new ArrayList<>(); //if this is not static the interface won't work :(
    private List<KeyRow> selectedKeys = new ArrayList<>();
    private KeyRow dumpObj;
    private Context context;
    private ActionMode actionMode;
    private Activity originActivity;
    private int selectListIndex = 0;
    public static int SHARE_KEY_RESULT = 120;
    private static String INTENT_SHARE = "intent_share";
    private static String SCREEN_SHARE = "screen_share";
    private AdminKeyGenAdapter refToSelf;
    private TextView listPH; // empty list placeholder passed through the constructor

    // Implement interface from AdminKeyGen
    public List<KeyRow> passMyList() {

        // Temp list for passing data via the interface
        List<KeyRow> passList = new ArrayList<>();

        // Iterate through the keys list & copy all selected keys to the passList
        for(KeyRow temp : keys) {
            if(temp.getCheckBoxValue() == true) {
                passList.add(temp);
            }
        }

        return passList;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        CardView cv;
        TextView key;
        TextView userCat;
        CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);

            cv = (CardView)itemView.findViewById(R.id.cv);
            key = (TextView)itemView.findViewById(R.id.key_textview);
            userCat = (TextView)itemView.findViewById(R.id.user_category);
            checkbox = (CheckBox)itemView.findViewById(R.id.key_row_checkbox);

            v.setClickable(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdminKeyGenAdapter(List<KeyRow> keys, Activity activity, Context context,
                              TextView placeHolder) {
        this.keys = keys;
        this.context = context;
        this.originActivity = activity;
        this.refToSelf = this;
        this.listPH = placeHolder;
    }

    // Empty constructor used for the AdminKeyGen interface
    public AdminKeyGenAdapter() {}

    // Create new views (invoked by the layout manager)
    @Override
    public AdminKeyGenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_key_view, parent, false);
        // set the view's size, margins, paddings and layout parameter

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.key.setText(keys.get(position).getKey());
        holder.userCat.setText(keys.get(position).getUserCategory());

        // If the key's member variable 'selected' is true update the UI to reflect it &
        // add the key to the 'selectedList' to later be passed into the PDFProcessor
        //if(keys.get(position).getCheckBoxValue()) {

        // The current row has its checkbox selected
        holder.checkbox.setChecked(true);

        // Instantiate the dumpObj and reconstruct this object from the keys' values
        dumpObj = new KeyRow(keys.get(position).getKey(), keys.get(position).getKeyId(),
                keys.get(position).getUserCategory(), keys.get(position).getCheckBoxValue());

        // Row tap callback
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Launch the interface
                if (actionMode == null) {

                    actionMode = originActivity.startActionMode(new ActionModeCallback(
                            keys.get(position).getKey(), keys.get(position).getKeyId(),
                            keys.get(position).getUserCategory(), refToSelf, position, listPH));
                    actionMode.setTitle("Edit Keys");

                }

                return true;
            }
        });

        // Row's checkbox tap callback
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // When the user selects a checkbox mark that row as a selected object
                if(isChecked) {

                    keys.get(position).setCheckboxValue(true);
                }

                // When the user deselects a checkbox mark that row as a deselected object
                else {

                    keys.get(position).setCheckboxValue(false);
                }
            }
        });

        // Make every other row a different color.
        if (position % 2 == 0) {
            holder.cv.setCardBackgroundColor(this.context.getResources().getColor(R.color.colorZebraRecycle));
        } else {
            holder.cv.setCardBackgroundColor(this.context.getResources().getColor(R.color.colorWhite));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return keys.size();
    }

    // Implement an ActionModeCallback class, this handles the actionModeCallback interface
    private class ActionModeCallback implements ActionMode.Callback {

        private String keyName;
        private String keyId;
        private String userCat;
        private AdminKeyGenAdapter adapter;
        private int objPosition;
        private TextView placeHolder;

        public ActionModeCallback(String keyName, String keyId, String userCat,
                                  AdminKeyGenAdapter adapter, int position, TextView placeH) {
            this.keyName = keyName;
            this.keyId = keyId;
            this.userCat = userCat;
            this.adapter = adapter;
            this.objPosition = position;
            this.placeHolder = placeH;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_admin_edit_key_gen, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.share_key:

                    // Launch share intent
                    shareKey(keyName, userCat);

                    // Launch moveToSharedKeys
                    ParseProcessor.moveToSharedKeys(keyId, context, adapter, objPosition, keys,
                            placeHolder, INTENT_SHARE);

                    mode.finish(); // action finished
                    return true;
                case R.id.delete_key:
                    //Toast.makeText(context, "Delete Key", Toast.LENGTH_LONG).show();
                    ParseProcessor.moveToSharedKeys(keyId, context, adapter, objPosition, keys,
                            placeHolder, SCREEN_SHARE);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null; // if this actionMode is not set to null it will get stuck
        }
    }

    // Share key & user category via email
    private void shareKey(final String key, final String userCat){

        // Create an email-specialized intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SENDTO);
        shareIntent.setData(Uri.parse("mailto:"));
        shareIntent.putExtra(Intent.EXTRA_EMAIL, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "" + key + " | " + userCat);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MedCrawler Registration Key");

        try {
            originActivity.startActivityForResult(shareIntent, SHARE_KEY_RESULT);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email apps installed", Toast.LENGTH_LONG).show();
        }


    }
}