package brood.com.medcrawler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchUserSOIAdapter extends RecyclerView.Adapter<SearchUserSOIAdapter.ViewHolder> {

    private List<String> dataFeeder;
    private Context context;
    private String caller;
    private int selectedItem = 0;
    private RecyclerView paramRV;
    private AppCompatButton button;
    private ArrayList<String> selecStatesArray = new ArrayList<>();

    // Constructor
    public SearchUserSOIAdapter(List<String> dataFeeder, Context context, String caller,
                                RecyclerView rv, AppCompatButton button, ArrayList<String> repopStates) {

        // Dynamic initializations - if CALLER is from AddUser
        if (caller.equals(AdminAddUser.CALLER)) {

            // Remove the 'All' entry from the dataFeeder if caller is from AdminAddUser
            dataFeeder.remove(0);


        } else if (caller.equals("edit_prof")) {

            // Remove the 'All' entry from the dataFeeder if caller is from AdminAddUser
            dataFeeder.remove(0);

        }

        this.dataFeeder = dataFeeder;
        this.context = context;
        this.caller = caller;
        this.paramRV = rv;
        this.button = button;
        this.selecStatesArray = repopStates;

    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Handle key up and key down and attempt to move selection
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userCat;
        CardView cv;
        ImageView rowIw;
        RelativeLayout cardLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            userCat = (TextView) itemView.findViewById(R.id.spec_textview);
            cv = (CardView) itemView.findViewById(R.id.spec_cv);
            rowIw = (ImageView) itemView.findViewById(R.id.admin_search_user_spec_icon);
            cardLayout = (RelativeLayout) itemView.findViewById(R.id.row_card_lay);

            itemView.setClickable(true);

            /* Row multi-select */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Redraw the old section and the new
                    notifyItemChanged(selectedItem);
                    selectedItem = paramRV.getChildAdapterPosition(v);
                    notifyItemChanged(selectedItem);

                }
            });
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int rowPosition) {

        // Assign values to the row here
        holder.userCat.setText(dataFeeder.get(rowPosition));
        final String valueToPass = holder.userCat.getText().toString();

        /************************************* Call from UserMain *********************************/

        if (caller.equals(UserMain.CALLER)) {
            // Assign different icons for rows in list based on position
            if (rowPosition == 0) {
                holder.rowIw.setImageResource(R.drawable.all_specs_icon);
            } else {
                holder.rowIw.setImageResource(R.drawable.spec_icon);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent("u_SOI");
                    i.putExtra("textValue", valueToPass);
                    context.sendBroadcast(i);
                }
            });
        }

        /******************************************************************************************/

        /*********************************** Call from AdminAddUser *******************************/

        else if (caller.equals(AdminAddUser.CALLER)) {

            // Iterate over data-array and determine which rows are selected
            for (int i = 0; i < selecStatesArray.size(); i++) {

                if (valueToPass.equals(selecStatesArray.get(i))) {


                    holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.colorLightPrimary));
                    holder.itemView.setSelected(true);
                    holder.rowIw.setImageResource(R.drawable.selected_states_icon);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // On click if row is not selected
                    if (!holder.itemView.isSelected()) {

                        holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.colorLightPrimary));
                        holder.itemView.setSelected(true);
                        holder.rowIw.setImageResource(R.drawable.selected_states_icon);

                        // Catch the state name
                        selecStatesArray.add(dataFeeder.get(rowPosition));

                        // On click if row is selected
                    } else {

                        holder.itemView.setSelected(false);
                        holder.rowIw.setImageResource(R.drawable.spec_icon);
                        holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));

                        //names.remove(dataFeeder.get(rowPosition));
                        selecStatesArray.remove(dataFeeder.get(rowPosition));
                    }
                }
            });

//            Intent i = new Intent();
//            i.putExtra("textValue", popStateNames);
//            context.sendBroadcast(i);

        }
        /******************************************************************************************/

        /********************************** Call from UserEditProfile *****************************/

        else if (caller.equals(UserEditProfile.CALLER)) {

            // Iterate over data-array and determine which rows are selected
            for (int i = 0; i < selecStatesArray.size(); i++) {

                if (valueToPass.equals(selecStatesArray.get(i))) {


                    holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.colorLightPrimary));
                    holder.itemView.setSelected(true);
                    holder.rowIw.setImageResource(R.drawable.selected_states_icon);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // On click if row is not selected
                    if (!holder.itemView.isSelected()) {

                        holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.colorLightPrimary));
                        holder.itemView.setSelected(true);
                        holder.rowIw.setImageResource(R.drawable.selected_states_icon);

                        // Catch the state name
                        selecStatesArray.add(dataFeeder.get(rowPosition));

                        // On click if row is selected
                    } else {

                        holder.itemView.setSelected(false);
                        holder.rowIw.setImageResource(R.drawable.spec_icon);
                        holder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));

                        //names.remove(dataFeeder.get(rowPosition));
                        selecStatesArray.remove(dataFeeder.get(rowPosition));
                    }
                }
            });
        }

        /******************************************************************************************/
    }

    @Override
    public int getItemCount() {
        return dataFeeder.size();
    }


    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int nextSelectItem = selectedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (nextSelectItem >= 0 && nextSelectItem < getItemCount()) {
            notifyItemChanged(selectedItem);
            selectedItem = nextSelectItem;
            notifyItemChanged(selectedItem);
            lm.scrollToPosition(selectedItem);
            return true;
        }

        return false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spec_view, parent, false);
        ViewHolder vh = new ViewHolder(v);

        // Two cases when the dialog button doesn't close the view but has to select multiple items
        if (caller.equals(AdminAddUser.CALLER)) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent("a_SOI");
                    i.putExtra("textValue", selecStatesArray);
                    context.sendBroadcast(i);

                }
            });
        } else if (caller.equals(UserEditProfile.CALLER)) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent("a_edit_SOI");
                    i.putExtra("textValue", selecStatesArray);
                    context.sendBroadcast(i);

                }
            });
        }

        return vh;
    }

}
