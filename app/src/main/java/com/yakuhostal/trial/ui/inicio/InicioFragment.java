package com.yakuhostal.trial.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.yakuhostal.trial.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;


public class InicioFragment extends Fragment  {

    private ImageView ivLogoInicio;
    private EasySlider Slider;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InicioViewModel inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        //final TextView tvInicio = root.findViewById(R.id.tvInicio);
        ivLogoInicio=root.findViewById(R.id.ivLogoInicio);
        Slider = root.findViewById(R.id.slider);
        visualizarSlider();


        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


                visualizarSlider();



            }
        });
        return root;
    }



    public void visualizarSlider(){

        //CARGA DE IMAGEN DESDE INTERNET
        String casa_1="https://ia801400.us.archive.org/5/items/surf_skola/casa_1.png";
        String hab_grupo_5="https://ia801400.us.archive.org/5/items/surf_skola/hab_grupo_5.jpg";
        String surf_playa="https://ia801400.us.archive.org/5/items/surf_skola/surf_playa.jpg";
        String logo="https://ia601504.us.archive.org/32/items/color-nuevo-logo_20201021_1041/Color%20nuevo%20logo.PNG";
        Picasso.get().load(logo).into(ivLogoInicio);


        /*--------
        | SLIDER  |
        ---------*/

        List<SliderItem> Sliders = new ArrayList<>();

        Sliders.add(new SliderItem("EL HOSTAL",casa_1));
        Sliders.add(new SliderItem("LA COMODIDAD A TU ALCANCE",hab_grupo_5));
        Sliders.add(new SliderItem("VEN A SURFEAR",surf_playa));
        Slider.setPages(Sliders);

        Slider.setTimer(9000);

    }
}