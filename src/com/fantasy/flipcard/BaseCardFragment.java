package com.fantasy.flipcard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseCardFragment extends Fragment {

    private String type;
    private View.OnClickListener mCloseBtnClickListener;

    public static BaseCardFragment newInstance(String type) {
        final BaseCardFragment bf = new BaseCardFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bf.setArguments(bundle);
        return bf;
    }

    public void setCloseBtnClickListener(View.OnClickListener onClickListener) {
        mCloseBtnClickListener = onClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_fragment, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.textView1);
        textView.setText(type);
        rootView.findViewById(R.id.imageView_close).setOnClickListener(mCloseBtnClickListener);
        return rootView;
    }
}
