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
import android.widget.Toast;

import java.util.List;

public class SearchUserSORAdapter extends RecyclerView.Adapter<SearchUserSORAdapter.ViewHolder> {

    private List<String> dataFeeder;
    private Context context;
    private RecyclerView paramRV;
    private Toast toast;


    // Constructor
    public SearchUserSORAdapter(List<String> dataFeeder, Context context,
                                RecyclerView rv, Toast toast) {
        this.dataFeeder = dataFeeder;
        this.context = context;
        this.paramRV = rv;
        this.toast = toast;
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
    public void onBindViewHolder(final ViewHolder holder, final int rowPosition) {

        // Assign values to the row here
        holder.userCat.setText(dataFeeder.get(rowPosition));
        final String valueToPass = holder.userCat.getText().toString();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass the data to a receiver
                Intent i = new Intent("a_SOR");
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
