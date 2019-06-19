/*This file is part of CardBox.

    CardBox is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CardBox is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CardBox.  If not, see <http://www.gnu.org/licenses/>.

    Author: Andrei Birsan */
package brood.com.medcrawler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * DeviceConnection is in charge of checking the network and the Internet
 * connection on the device where it is being implemented
 */

public class DeviceConnection {

    static String TAG = "NETWORK DATA - DeviceConnection";

    /**
     * testConnection calls the connectivity manager and checks the
     * connectivity_service status of the OS as well as the current status of
     * the local network status. This method returns a boolean depending on the
     * network status, mStateBoolean can be either true or false.
     *
     * @param context
     * @return
     */

    public static Boolean testConnection(Context context) {
        boolean mStateBoolean = false;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if ((networkInfo != null) && (networkInfo.isConnected())) {

            // log out the network
            mStateBoolean = true;
        }

        return mStateBoolean;
    }

}
