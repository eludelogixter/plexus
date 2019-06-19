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

import java.util.List;

public class SearchUserSDAdapter extends RecyclerView.Adapter<SearchUserSDAdapter.ViewHolder> {

    private List<String> dataFeeder;
    private Context context;

    // Constructor
    public SearchUserSDAdapter(List<String> dataFeeder, Context context) {

        this.dataFeeder = dataFeeder;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userCat;
        CardView cv;
        ImageView rowIw;

        public ViewHolder(View itemView) {
            super(itemView);

            userCat = (TextView)itemView.findViewById(R.id.spec_textview);
            cv = (CardView)itemView.findViewById(R.id.spec_cv);
            rowIw = (ImageView)itemView.findViewById(R.id.admin_search_user_spec_icon);

            itemView.setClickable(true);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Assign values to the row here
        holder.userCat.setText(dataFeeder.get(position));
        final String valueToPass = holder.userCat.getText().toString();

        // Assign an icon to every row
        holder.rowIw.setImageResource(R.drawable.spec_icon);

        // Assign a special icon to a row if it contains the string "all"
        if (holder.userCat.getText().toString().toLowerCase().equals("all")) {
            holder.rowIw.setImageResource(R.drawable.all_specs_icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent("u_SD");
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
