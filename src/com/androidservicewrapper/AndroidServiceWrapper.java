
package com.androidservicewrapper;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Returns system service wrappers. Alternative to using Context.getSystemService
 * to return overridable/configurable wrapper service managers.
 */
public class AndroidServiceWrapper {
    private static final String TAG = AndroidServiceWrapper.class.getSimpleName();

    private static final HashMap<String, Object> mSystemServices = new HashMap<String, Object>();

    /**
     * @param context context to use
     * @param serviceName service to fetch
     * @return wrapper implementation of the object returned by
     * context.getSystemService(serviceName) if the mock service is not set. If the mock service
     * is set, returns the mock service set for serviceName.
     * @throws IllegalArgumentException if the service name is invalid
     */
    public static Object getSystemService(Context context, String serviceName) throws IllegalArgumentException {
        // check if mock object exists
        Object manager = mSystemServices.get(serviceName);

        // get the wrapper service manager if mock object does not exist
        if (manager == null) {
            if (Context.TELEPHONY_SERVICE.equals(serviceName)) {
                manager = new TelephonyManagerWrapper(context);
            } else if (Context.POWER_SERVICE.equals(serviceName)) {
                manager = new PowerManagerWrapper(context);
            } else if (Context.ALARM_SERVICE.equals(serviceName)) {
                manager = new AlarmManagerWrapper(context);
            } else if( Context.WIFI_SERVICE.equals(serviceName)) {
                manager = new WifiManagerWrapper(context);
            } else if( Context.LOCATION_SERVICE.equals(serviceName)){
                manager = new LocationManagerWrapper(context);
            } else {
                Log.e(TAG, "Unknown service: " + serviceName);
                throw new IllegalArgumentException("Unknown service: " + serviceName);
            }
        }
        return manager;
    }

    /**
     * Sets the mock service to return for the given serviceName.
     * @param serviceName service to mock
     * @param service the mock implementation of the service. If null, removes the mock service
     * for the serviceName.
     */
    public static void setMockSystemService(String serviceName, Object service) {
        if (service != null) {
            mSystemServices.put(serviceName, service);
        } else {
            mSystemServices.remove(serviceName);
        }
    }

    public static void clearMockSystemServices() {
        mSystemServices.clear();
    }
}
