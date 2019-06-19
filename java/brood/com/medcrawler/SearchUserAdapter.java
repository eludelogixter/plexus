package brood.com.medcrawler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {
    private List<UserObject> searchUserList;
    private Context context;
    private String caller;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView userName;
        TextView userCat;
        TextView userSpec;
        TextView userSubSpec;
        TextView dateOfReg;
        ImageView userPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userCat = (TextView) itemView.findViewById(R.id.user_category);
            userSpec = (TextView) itemView.findViewById(R.id.user_spec);
            userSubSpec = (TextView) itemView.findViewById(R.id.user_sub_spec);

            // Date of registration only from Admin
            if (caller.equals(AdminMain.CALLER)) {
                dateOfReg = (TextView) itemView.findViewById(R.id.user_date);
            }
            userPhoto = (ImageView) itemView.findViewById(R.id.user_photo);

            itemView.setClickable(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchUserAdapter(List<UserObject> searchUserList, Context context, String caller) {
        this.searchUserList = searchUserList;
        this.context = context;
        this.caller = caller;
    }

    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        ViewHolder vh;

        // Load the user row with date of registration
        if(caller.equals(AdminMain.CALLER)) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_card_view_admin, parent, false);
            vh = new ViewHolder(v);
        }

        // Load the normal user row
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_card_view, parent, false);
            vh = new ViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, final int position) {

        holder.userName.setText(searchUserList.get(position).getFirstName()+ " " +
                searchUserList.get(position).getLastName());
        holder.userCat.setText(searchUserList.get(position).getUserProff());
        holder.userSpec.setText(searchUserList.get(position).getUserSpec());
        holder.userSubSpec.setText(searchUserList.get(position).getUserSubspec());

        // If the caller is AdminMain's populate the date
        if (caller.equals(AdminMain.CALLER)) {

            // Parse reg-date to show only month + day + year
            String split = searchUserList.get(position).getDateOfReg();

            String month = split.split(" ")[1];
            String day = split.split(" ")[2];
            String year = split.split(" ")[5];

            String date = month + " " + day + " " + year;

            holder.dateOfReg.setText(date);

        }

        // Make every other row a different color.
        if (position % 2 == 0) {
            holder.cv.setCardBackgroundColor(this.context.getResources().getColor(R.color.colorZebraRecycle));
        } else {
            holder.cv.setCardBackgroundColor(this.context.getResources().getColor(R.color.colorWhite));
        }

        try {
            byte [] array = PhotoProcessor.readPicFromLocal(searchUserList.get(position).getProfilePicture(), context);
            if (array != null && array.length > 0) {
                Bitmap tempBmp = BitmapFactory.decodeByteArray(array, 0, array.length);
                holder.userPhoto.setImageBitmap(tempBmp);
            } else {

                // Error, prob. no picture, set a default one
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_photo);
                holder.userPhoto.setImageBitmap(bm);
            }


        } catch (Exception e) {
            e.printStackTrace();

        }

        if (caller.contains("fromFav")) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, UserProfile.class);
                    i.putExtra("fromFav", searchUserList.get(position));
                    context.startActivity(i);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    // Setup a broadcaster
                    // Pass the data to a receiver
                    Intent i = new Intent("favUsrDel");
                    i.putExtra("favUsr_ID", searchUserList.get(position).getUserId());
                    i.putExtra("favUsr_fname", searchUserList.get(position).getFirstName());
                    i.putExtra("favUsr_lname", searchUserList.get(position).getLastName());
                    context.sendBroadcast(i);

                    return false;
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, UserProfile.class);
                    i.putExtra("userProfile", searchUserList.get(position));
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return searchUserList.size();  //this is important or else empty list!
    }
}
