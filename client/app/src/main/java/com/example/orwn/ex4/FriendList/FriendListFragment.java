package com.example.orwn.ex4.FriendList;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.orwn.ex4.Chat.ChatActivity;

import com.example.orwn.ex4.R;
import com.example.orwn.ex4.Swipe.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListFragment extends Fragment {


    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] names,statuses;
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        ListView lstMenu = (ListView) view.findViewById(R.id.lstFriends);
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.friend_list_fragment_layout);
        List<FriendItem> FriendItems = new ArrayList<FriendItem>();
        //FrameLayout fl = (FrameLayout) view.findViewById(R.id.fif);

        names = this.getResources().getStringArray(R.array.Friends_Names);
        statuses = this.getResources().getStringArray(R.array.Friends_Statuses);
        for (int i = 0; i < names.length; i++) {

            FriendItems.add(new FriendItem(names[i], statuses[i], R.mipmap.ic_launcher, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Friends List Cell", "Click");
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    startActivity(intent);
                }
            }));
         }

        Fragment f;
        FriendAdapter menuAdapter = new FriendAdapter(getActivity(),FriendItems);
        lstMenu.setAdapter(menuAdapter);




        return view;

    }

}
