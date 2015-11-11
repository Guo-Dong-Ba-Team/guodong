package com.guodong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.guodong.R;
import com.guodong.util.Traffic;

/**
 * Created by yechy on 2015/10/30.
 */
public class DisplayImageFragment extends Fragment {

    private View view;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        String url = args.getString("imageUrl");
        String intro = args.getString("imageIntro", "");
        view = inflater.inflate(R.layout.fragment_display_image, container, false);

        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.image_netview);
        TextView textView = (TextView) view.findViewById(R.id.image_intro);
        textView.setText(intro);

        requestQueue = Volley.newRequestQueue(getActivity());
        Traffic.showNetworkImage(requestQueue, url, imageView);

        return view;
    }
}
