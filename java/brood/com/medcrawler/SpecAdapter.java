package brood.com.medcrawler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SpecAdapter extends RecyclerView.Adapter<SpecAdapter.ViewHolder> implements Filterable {
    private List<Specialty> specList;
    private List<Specialty> filterSafeGuard;
    private Context context;
    private ArrayList<Specialty> filteredList;
    private MyFilter myFilter = new MyFilter();
    private Specialty tempSpec;
    private Dialog dialog;
    private String caller;
    private final String ADMIN = "admin";


    /* Interface for Filterable */
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Set vars
        CardView cv;
        TextView specName;
        ImageView specIcon;
        Dialog dialog;

        public ViewHolder(View v) {
            super(v);

            // Target XML objects present in each row
            specName = (TextView)itemView.findViewById(R.id.spec_textview);
            specIcon = (ImageView)itemView.findViewById(R.id.admin_search_user_spec_icon);
            cv = (android.support.v7.widget.CardView)itemView.findViewById(R.id.spec_cv);

            v.setClickable(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SpecAdapter(List<Specialty> specList, List<Specialty> filterSafeGuard, Context context,
                       Dialog dialog, String caller) {
        this.specList = specList;
        this.filterSafeGuard = filterSafeGuard;
        this.context = context;
        this.dialog = dialog;
        this.caller = caller;

        // Check if the currently logged user is an admin or a user
        ParseUser localUsr = ParseUser.getCurrentUser();

        // If the currently logged in is an admin show All before any other specialty
        if ((localUsr != null) && (localUsr.get("userCat").equals(ADMIN))) {

            // Insert an "All" value into the specList
            Specialty tempSpec = new Specialty("All", "");
            specList.add(0, tempSpec);
        }



    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Use this int so the original position param doesn't have to be final for inner class access
        final int tempPosition = position;


        // Set the spec name for each row
        holder.specName.setText(specList.get(tempPosition).getSpecName());
        holder.specIcon.setImageResource(R.drawable.spec_icon);

        // Assign a special icon to a row if it contains the string "all"
        if (holder.specName.getText().toString().toLowerCase().equals("all")) {
            holder.specIcon.setImageResource(R.drawable.all_specs_icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(caller.equals(AdminAddUser.CALLER)) {

                    Intent i = new Intent("a_Spec");
                    i.putExtra("textValue", holder.specName.getText().toString());
                    i.putExtra("textId", specList.get(position).getSpecId().toString());
                    context.sendBroadcast(i);

                }
                else if (caller.equals(UserMain.CALLER)) {


                    Intent i = new Intent("u_S");
                    i.putExtra("textValue", holder.specName.getText().toString());
                    i.putExtra("textId", specList.get(position).getSpecId().toString());
                    context.sendBroadcast(i);
                }

                else if (caller.equals(AdminUserSpecialty.CALLER)) {

                    Intent i = new Intent("s_Editor_spec");
                    i.putExtra("textValue", holder.specName.getText().toString());
                    i.putExtra("specId", specList.get(position).getSpecId().toString());

                    context.sendBroadcast(i);
                }

                dialog.dismiss();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return specList.size();
    }

    /* Prototype class for the filter */
    public class MyFilter extends Filter {

        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResult = new FilterResults();
            filteredList = new ArrayList<>();

            // Reset the data array to the Parse data every time the user deletes a letter from the
            // editText widget, this creating the illusion of going back before the last letter
            specList = filterSafeGuard;

            // If the constraint is 0 return the original data
            if (constraint.length() == 0) {

                filterResult.values = specList;
                filterResult.count = specList.size();
                return filterResult;

            }

            String tempConstraint = constraint.toString().toLowerCase();

            // Iterate through the original data array
            for (int i = 0; i < specList.size(); i++) {

                // Check to see if the constraint variable corresponds to the folder name or to the
                // number of cards belonging to this particular folder
                if (specList.get(i).getSpecName().toString().toLowerCase().contains(tempConstraint)) {

                    tempSpec = new Specialty(specList.get(i).getSpecName(), specList.get(i).getSpecId());

                    // Add this object to the updated filtered list
                    filteredList.add(tempSpec);

                }
            }

            // Assign the new list to the object filterResult for return
            filterResult.values = filteredList;
            filterResult.count = filteredList.size();

            return filterResult;
        }

        /* Needed for the filter */
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {

            if (results.values != null) {

                specList = (ArrayList<Specialty>) results.values;

            }

            notifyDataSetChanged();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SpecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_spec_view, parent, false);
        // set the view's size, margins, paddings and layout parameter

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
}