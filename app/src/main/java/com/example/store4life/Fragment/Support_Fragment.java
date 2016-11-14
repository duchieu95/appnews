package com.example.store4life.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.store4life.Activity.Question_Activity;
import com.example.store4life.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class Support_Fragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    Button btMail, btnPhone,btnHoi;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.support_fragment,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnHoi = (Button) view.findViewById(R.id.btnHoi);
        btMail = (Button)view.findViewById(R.id.btnEmail);
        btnPhone = (Button)view.findViewById(R.id.btnPhone);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+"0946697194"));
                startActivity(callIntent);
            }
        });
        btMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Nhập Tiêu Đề");
                intent.putExtra(Intent.EXTRA_TEXT, "Nhập Nội Dung");
                intent.setData(Uri.parse("mailto:hieuhdps02924@fpt.edu.vn"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btnHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Question_Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng fpt = new LatLng(10.790868, 106.682691);
        mMap.addMarker(new MarkerOptions().position(fpt).title("Trường FPT Polytechnic"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fpt));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fpt,18));
    }

}
