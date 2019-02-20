package com.wythe.mall.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wythe.mall.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by wythe on 2016/7/25.
 */
public class SuggestionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }
}
