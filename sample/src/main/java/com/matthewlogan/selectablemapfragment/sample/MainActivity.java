package com.matthewlogan.selectablemapfragment.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.matthewlogan.selectablemapfragment.library.OnOverlayDragListener;
import com.matthewlogan.selectablemapfragment.library.SupportSelectableMapFragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MainActivity extends ActionBarActivity {

    private static LatLng sStartLocation = new LatLng(37.7833, -122.4167);
    private static float sStartZoom = 10.f;

    private SupportSelectableMapFragment mMapFragment;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView coordinatesTextView = (TextView) findViewById(R.id.coordinates_text_view);

        final NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(4);
        df.setMaximumFractionDigits(4);

        mMapFragment = new SupportSelectableMapFragment();
        mMapFragment.setOnOverlayDragListener(new OnOverlayDragListener() {
            @Override
            public void onOverlayDrag(LatLngBounds latLngBounds) {
                coordinatesTextView.setText(
                        "SW: (" + df.format(latLngBounds.southwest.latitude) + " N, "
                                + df.format(latLngBounds.southwest.longitude) + " E)"
                                + "\n"
                                + "NE: (" + df.format(latLngBounds.northeast.latitude) + " N, "
                                + df.format(latLngBounds.northeast.longitude) + " E)"
                );
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, mMapFragment)
                .commit();

        final Button selectionBoxButton = (Button) findViewById(R.id.show_selection_button);
        selectionBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (mMapFragment.isSelectionBoxVisible()) {
                    mMapFragment.setSelectionBoxVisible(false);
                    selectionBoxButton.setText("Show selection box");
                } else {
                    mMapFragment.setSelectionBoxVisible(true);
                    selectionBoxButton.setText("Hide selection box");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleMap == null) {
            mGoogleMap = mMapFragment.getMap();
            if (mGoogleMap != null) {
                moveCameraToStartLocation();
            }
        }
    }

    private void moveCameraToStartLocation() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sStartLocation, sStartZoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }
}
