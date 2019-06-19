package brood.com.medcrawler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

public class SearchUserIAdapter extends RecyclerView.Adapter<SearchUserIAdapter.ViewHolder> {

    private List<String> dataFeeder;
    private Context context;
    private final String ADMIN = "admin";

    // Constructor
    public SearchUserIAdapter(List<String> dataFeeder, Context context) {

        // Check if the currently logged user is an admin or a user
        ParseUser localUsr = ParseUser.getCurrentUser();

        // If the currently logged in is an admin show All before any other specialty
        if ((localUsr != null)  && (localUsr.get("userCat").equals(ADMIN))) {

            dataFeeder.add(0, "All");
        }

        this.dataFeeder = dataFeeder;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userCat;
        CardView cv;
        ImageView iw;

        public ViewHolder(View itemView) {
            super(itemView);

            userCat = (TextView)itemView.findViewById(R.id.spec_textview);
            cv = (CardView)itemView.findViewById(R.id.spec_cv);
            iw = (ImageView)itemView.findViewById(R.id.admin_search_user_spec_icon);

            itemView.setClickable(true);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Assign values to the row here
        holder.userCat.setText(dataFeeder.get(position));
        final String valueToPass = holder.userCat.getText().toString();

        if (holder.userCat.getText().equals("All")) {
            holder.iw.setImageResource(R.drawable.all_specs_icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent("u_I");
                i.putExtra("textValue", valueToPass);
                context.sendBroadcast(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataFeeder.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spec_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

}
