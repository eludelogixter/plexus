package brood.com.medcrawler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class About {

    AlertDialog aboutDialog;

    protected void launch(final Context context) {
        //super(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View myDialogView = inflater.inflate(R.layout.d_menu_app_about, null);

        CardView contactAdminCW = (CardView)myDialogView.findViewById(R.id.card_d_menu_contact_admin);
        contactAdminCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Send email to admin", Toast.LENGTH_SHORT).show();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","rcmillsmd@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Question about Plexus");

                try {
                    context.startActivity(Intent.createChooser(emailIntent, "Choose email app:"));
                } catch (Exception e) {
                    Toast.makeText(context, "Can't send email. Is there an email app installed?", Toast.LENGTH_LONG).show();
                }

            }
        });


        builder.setView(myDialogView);
        aboutDialog = builder.create();
        aboutDialog.setCanceledOnTouchOutside(true);
        aboutDialog.show();
    }
}
