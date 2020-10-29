package com.yakuhostal.trial.ui.nosotros;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.yakuhostal.trial.R;

public class NosotrosFragment extends Fragment {

    private ImageView imgVFacebook;
    private ImageView imgVTwitter;
    private ImageView imgVYoutube;
    private ImageView imgVInstagram;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GaleriaViewModel galeriaViewModel = ViewModelProviders.of(this).get(GaleriaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nosotros, container, false);


        TextView tvTituloUbicacionGaleria = root.findViewById(R.id.tvUbicacion);
        final ImageView imgMapaCasa = root.findViewById(R.id.imgMapaCasa);
        imgVFacebook= root.findViewById(R.id.imgVFacebook);
        imgVTwitter= root.findViewById(R.id.imgVTwitter);
        imgVYoutube= root.findViewById(R.id.imgVYoutube);
        imgVInstagram= root.findViewById(R.id.imgVInstagram);
        TextView tvRSSS=root.findViewById(R.id.tvRRSS);

        galeriaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {



                imgMapaCasa.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        goToUrl ( "https://www.google.es/maps/place/Mungu%C3%ADa,+Vizcaya/@43.3416225,-2.8444595,470m/data=!3m1!1e3!4m5!3m4!1s0xd4e46bd3d40d485:0xef7f4686ea6bb116!8m2!3d43.3544186!4d-2.8467037");
                    }

                    private void goToUrl (String url) {
                        Uri uriUrl = Uri.parse(url);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                    }

                });
                imgVFacebook.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        goToUrl ( "http://facebook.com/");
                    }

                    private void goToUrl (String url) {
                        Uri uriUrl = Uri.parse(url);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                    }

                });

                imgVTwitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl ( "http://twitter.com/");
                    }

                    private void goToUrl (String url) {
                        Uri uriUrl = Uri.parse(url);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                    }

                });

                imgVInstagram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl ( "http://instagram.com/");
                    }

                    private void goToUrl (String url) {
                        Uri uriUrl = Uri.parse(url);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                    }


                });

                imgVYoutube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl ( "http://youtube.com/");
                    }

                    private void goToUrl (String url) {
                        Uri uriUrl = Uri.parse(url);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                    }


                });


                }
        });
        return root;
        
        
    }






}