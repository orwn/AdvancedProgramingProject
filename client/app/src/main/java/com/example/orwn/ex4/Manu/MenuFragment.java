package com.example.orwn.ex4.Manu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.orwn.ex4.MapsActivity;
import com.example.orwn.ex4.Screens.AddFriendActivity;
import com.example.orwn.ex4.FriendList.FriendListFragment;
import com.example.orwn.ex4.R;
import com.example.orwn.ex4.Screens.Settings_Activity;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents MenuFragment
 *
 * @author Or Weinstein
 */
public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manu, container, false);

        // Gets the views of this fragment layout
        ListView lstMenu = (ListView) view.findViewById(R.id.lstMenu);
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.manu_frame_layout);

        // Set on click listener on the views
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv)
            {
                getActivity().onBackPressed();
            }
        });
        // adding options to the menu
        menuItems.add(new MenuItem(getString(R.string.menu_friend_list_lable), R.mipmap.ic_launcher, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Opens the friend list fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FriendListFragment flf = new FriendListFragment();
                ft.add(R.id.map_container,flf);
                ft.addToBackStack("Friend List");
                ft.commit();

            }
        }));
        menuItems.add(new MenuItem(getString(R.string.menu_add_a_new_friend), R.mipmap.ic_launcher, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Strting add friend activity
                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);
            }
        }));
        menuItems.add(new MenuItem(getString(R.string.menu_settings_lable), R.mipmap.ic_launcher, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting setting_activity
                Intent intent = new Intent(getActivity(), Settings_Activity.class);
                startActivity(intent);
            }
        }));

        // Create a new menuAdapter
        MenuAdapter menuAdapter = new MenuAdapter(getActivity(),menuItems);
        lstMenu.setAdapter(menuAdapter);


        return view;

    }

}
