package com.yakuhostal.trial.ui.galeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.yakuhostal.trial.R;
import com.yakuhostal.trial.ui.inicio.InicioViewModel;

public class GaleriaFragment extends Fragment {

    //Añadir imagenes por url
    String[] items={"https://ia601400.us.archive.org/5/items/surf_skola/Mesa_Jardin.jpg",
            "https://ia601400.us.archive.org/5/items/surf_skola/ba%C3%B1o.png",
            "https://ia601400.us.archive.org/5/items/surf_skola/casa_1.png",
            "https://ia801400.us.archive.org/5/items/surf_skola/casa_2.png",
            "https://ia801400.us.archive.org/5/items/surf_skola/comida.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/comida_2.jpg",
            "https://ia601400.us.archive.org/5/items/surf_skola/desayuno_1.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/desayuno_2.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/desayuno_3.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/ducha.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_2personas_1.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_2personas_2.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_grupo_1.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_grupo_2.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_grupo_3.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_grupo_4.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_grupo_5.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/hab_pareja.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/jardin.png",
            "https://ia801400.us.archive.org/5/items/surf_skola/lavadoras.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/mesilla_noche.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/recepcion.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/salon.jpg",
            "https://ia801400.us.archive.org/5/items/surf_skola/surf_playa.jpg",
            "https://archive.org/download/surf_skola/surf_skola.png",
            "https://ia801400.us.archive.org/5/items/surf_skola/taquillas.jpg"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InicioViewModel inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_galeria, container, false);
        GridView gridView = root.findViewById(R.id.gridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), items);

        gridView.setAdapter(gridViewAdapter);

        return root;
    }

}
