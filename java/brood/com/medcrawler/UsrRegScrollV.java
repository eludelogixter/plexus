package brood.com.medcrawler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by nactus on 10/1/15.
 * In order to be able to catch the user scrolling we need a custom class that extends ScrollView
 * and overrides onScrollChanged, then we pass that via an interface to the activity
 * Also, don't forget to use a UsrRegScrollV type of object in the .xml layout file of the activity
 * in this case 'AdminAddUser'. A simple <ScrollView> won't work.
 */
public class UsrRegScrollV extends ScrollView {


    public interface ScrollViewInterface {

        void onScrollChanged(UsrRegScrollV scrollView, int x, int y, int oldx, int oldy);

    }

    // Constructors
    public UsrRegScrollV(Context context) {

        super(context);
    }

    public UsrRegScrollV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public UsrRegScrollV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

    }

}
