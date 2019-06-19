package brood.com.medcrawler;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

/**
 * Application class, this helps initialize processes that need to be initialized only once (like Parse) :)
 * don't forget to add "android:name="brood.com.medcrawler.MedCrawler" under <application
 */
public class  MedCrawler extends Application {

    private final String APP_ID = "485Dv6hZTwWbtwBxzvBzII7U7BEU4a5JrwAITTyS";
    private final String MY_URL = "https://rcmplexus.com/parse/";


    @Override
    public void onCreate() {
        super.onCreate();

        //context = this;

        // Initialize Parse
        Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "485Dv6hZTwWbtwBxzvBzII7U7BEU4a5JrwAITTyS", "0R8Ka9RQlY7OqdUWPt7tPcwsJFdSoK1e2VEyUrXb");

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APP_ID)
                .clientKey(null)
                .server(MY_URL) // The trailing slash is important.
                .build()
        );
    }

}
