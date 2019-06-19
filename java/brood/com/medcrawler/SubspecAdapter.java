package brood.com.medcrawler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SubspecAdapter extends RecyclerView.Adapter<SubspecAdapter.ViewHolder> implements Filterable {
    private List<Subspecialty> subSpecList;
    private List<Subspecialty> filterSafeGuard;
    private Context context;
    private String caller;
    private ArrayList<Subspecialty> filteredList;
    private MyFilter myFilter = new MyFilter();
    private Subspecialty tempSubspec;
    private Dialog dialog;
    private Boolean rowSelectedFlag = false;
    private Button passingWidget;


    /* Interface for Filterable */
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subspecName;
        ImageView subspecIcon;
        Dialog dialog;

        public ViewHolder(View v) {
            super(v);

            subspecName = (TextView)itemView.findViewById(R.id.spec_textview);
            subspecIcon = (ImageView)itemView.findViewById(R.id.admin_search_user_spec_icon);
            cv = (CardView)itemView.findViewById(R.id.spec_cv);

            v.setClickable(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SubspecAdapter(List<Subspecialty> subspecList, List<Subspecialty> filterSafeGuard, Context context,
                          Dialog dialog, Button widget, String caller) {
        this.subSpecList = subspecList;
        this.filterSafeGuard = filterSafeGuard;
        this.context = context;
        this.dialog = dialog;
        this.passingWidget = widget;
        this.caller = caller;

        // Check if the constructor is called from UserMain class
        if (caller.equals(UserMain.CALLER)) {

            // Insert an "all" value into the subSpecList
            Subspecialty tempSubSpec = new Subspecialty("All", "");
            subspecList.add(0, tempSubSpec);

        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Use this int so the original position param doesn't have to be final for inner class access
        final int tempPosition = position;

        // Set the spec name for each row
        holder.subspecName.setText(subSpecList.get(tempPosition).getSubspecName());

        // Change the collor of the first sub-spec entry in the list only if this is a search op.
        if (caller.equals(UserMain.CALLER)) {

            // Assign an icon to every row
            holder.subspecIcon.setImageResource(R.drawable.spec_icon);

            // Assign a special icon to a row if it contains the string "all"
            if (holder.subspecName.getText().toString().toLowerCase().equals("all")) {
                holder.subspecIcon.setImageResource(R.drawable.all_specs_icon);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(caller.equals("sub_reg")) {

                    Intent i = new Intent("a_subSpec");
                    i.putExtra("textValue", holder.subspecName.getText().toString());
                    context.sendBroadcast(i);

                }

                else if(caller.equals(AdminUserSpecialty.CALLER)) {

                    Intent i = new Intent("subspec_Editor_spec");
                    i.putExtra("textValue", holder.subspecName.getText().toString());
                    i.putExtra("subSpecId", subSpecList.get(position).getSubspecId());
                    context.sendBroadcast(i);
                }

                else if (caller.equals("edit_prof")) {
                    UserEditProfile.selSubspecWidget.setBackgroundColor(Color.parseColor("#455A64"));
                    UserEditProfile.selSubspecWidget.setText(holder.subspecName.getText());

                }

                else if (caller.equals(UserMain.CALLER)) {

                    Intent i = new Intent("u_SS");
                    i.putExtra("textValue", holder.subspecName.getText().toString());
                    context.sendBroadcast(i);
                }

                dialog.dismiss();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return subSpecList.size();
    }

    /* Prototype class for the filter */
    public class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResult = new FilterResults();
            filteredList = new ArrayList<>();

            // Reset the data array to the Parse data every time the user deletes a letter from the
            // editText widget, this creating the illusion of going back before the last letter
            subSpecList = filterSafeGuard;

            // If the constraint is 0 return the original data
            if (constraint.length() == 0) {

                filterResult.values = subSpecList;
                filterResult.count = subSpecList.size();
                return filterResult;

            }

            String tempConstraint = constraint.toString().toLowerCase();

            // Iterate through the original data array
            for (int i = 0; i < subSpecList.size(); i++) {

                // Check to see if the constraint variable corresponds to the folder name or to the
                // number of cards belonging to this particular folder
                if (subSpecList.get(i).getSubspecName().toString().toLowerCase().contains(tempConstraint)) {

                    tempSubspec = new Subspecialty(subSpecList.get(i).getSubspecName(), subSpecList.get(i).getSubspecId());

                    // Add this object to the updated filtered list
                    filteredList.add(tempSubspec);

                }
            }

            // Assign the new list to the object filterResult for return
            filterResult.values = filteredList;
            filterResult.count = filteredList.size();

            return filterResult;
        }

        /* Needed for the filter */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {

                subSpecList = (ArrayList<Subspecialty>) results.values;

            }

            notifyDataSetChanged();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubspecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_spec_view, parent, false);
        // set the view's size, margins, paddings and layout parameter

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
}