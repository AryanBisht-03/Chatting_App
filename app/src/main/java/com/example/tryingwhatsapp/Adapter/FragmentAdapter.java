package com.example.tryingwhatsapp.Adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tryingwhatsapp.Fragements.CallFragment;
import com.example.tryingwhatsapp.Fragements.ChatsFragment;
import com.example.tryingwhatsapp.Fragements.StatusFragments;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new ChatsFragment();
            case 1:
                return new StatusFragments();
            case 2:
                return new CallFragment();
            default:
                return new ChatsFragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;

        if(position==0)
        {
            title = "Chats";
        }
        else if(position==1){
            title = "Status";
        }
        else
        {
            title = "Call";
        }

        return title;
    }
}
