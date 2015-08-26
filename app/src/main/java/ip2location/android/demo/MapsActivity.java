package ip2location.android.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import ip2location.appengine.api.Api;
import ip2location.appengine.api.model.IPLocation;

public class MapsActivity extends FragmentActivity {

    public static final int DEFAULT_ZOOM = 12;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }else{
                final int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if(errorCode != ConnectionResult.SUCCESS)
                    GooglePlayServicesUtil.showErrorDialogFragment(errorCode, this, 42);
                else
                    showMessage("can't find map");
            }
        }
    }

    private void setUpMap() {
        new GetLocation() {
            @Override
            protected void onError(Exception exception) {
                showMessage(exception.getMessage(), R.string.snackbar_action_retry, new Runnable() {
                    @Override
                    public void run() {
                        setUpMap();
                    }
                });

            }

            @Override
            protected void onSuccess(IPLocation loc) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                        .title(loc.getIp())
                        .snippet(loc.getCity() + ", " + loc.getCountry()));
                marker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        loc.getLatitude(), loc.getLongitude()), DEFAULT_ZOOM));
            }
        }.execute();
    }

    private void showMessage(String message) {
        showMessage(message, 0, null);
    }

    private void showMessage(String message, @StringRes int actionText, @Nullable final Runnable action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);

        if(action != null)
            snack.setAction(actionText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.run();
                }
            });
        snack.show();
    }


    private abstract static class GetLocation extends AsyncTask<Void, Void, Object> {

        private static Api service;

        public GetLocation() {
            if(service == null) {
                service = new Api.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl(BuildConfig.APPENGINE_ROOT_URL)
                        .build();
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                return service.get().execute();
            } catch (IOException e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if(result instanceof IPLocation)
                onSuccess((IPLocation) result);
            else
                onError((Exception) result);
        }

        protected abstract void onError(Exception exception);
        protected abstract void onSuccess(IPLocation ipLocation);
    }
}
