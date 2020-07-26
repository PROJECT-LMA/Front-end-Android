package com.laioffer.lma.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.laioffer.lma.ui.account.LoginFragment;
import com.laioffer.lma.ui.account.SignUpFragment;

public class OnBoardingPageAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 2;

    public OnBoardingPageAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LoginFragment.newInstance();
            case 1:
                return SignUpFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Login";
            case 1:
                return "Sign Up";
            default:
                return null;
        }
    }
}
