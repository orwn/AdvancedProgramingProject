package com.example.orwn.ex4.Screens;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.orwn.ex4.R;


/**
 * This class represents FriendInfoFrament
 *
 * @author Or Weinstein
 *
 */
public class FriendInfoFragment extends Fragment {

    // Private variables
    private String mParam1; // The name of the friend
    private String mParam2; // The status of the friend
    private int mSrc;       // The id of the image profile of the friend


    /**
     * Required to implement defualt constructor
     */
    public FriendInfoFragment()
    {

    }


    /**
     *
     * Construct a Friend Info with a string , a string and a src
     * @param m1 the name of the friend
     * @param m2 the status of the friend
     * @param src the id of the profile
     */
    public FriendInfoFragment(String m1,String m2,int src) {
        mParam1 = m1;
        mParam2 = m2;
        mSrc = src;
        // Required empty public constructor
    }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_friend_info, container, false);


        // Getting the views from the layout
        TextView t1 = (TextView) v.findViewById(R.id.friendInfo_txt1);
        TextView t2 = (TextView) v.findViewById(R.id.friendInfo_txt2);
        ImageView iv = (ImageView) v.findViewById(R.id.profile_picture);
        iv.setImageResource(mSrc);
        FrameLayout fl = (FrameLayout) v.findViewById(R.id.friend_info_layout);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.friend_info_relative);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv)
            {
                getActivity().onBackPressed();
            }
        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv)
            {
               // getActivity().onBackPressed();
            }
        });
        t1.setText(mParam1);
        t2.setText(mParam2);



        return v;
     }





}
