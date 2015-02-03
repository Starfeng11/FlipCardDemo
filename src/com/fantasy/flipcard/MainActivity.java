package com.fantasy.flipcard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FlipCardContainer flipCardContainer = (FlipCardContainer) findViewById(R.id.flip_card_container);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        flipCardContainer.setCardSize((int) (dm.widthPixels * 0.7f), (int) (dm.heightPixels * 0.7f));

        if (savedInstanceState == null) {
            BaseCardFragment frontFragment = BaseCardFragment.newInstance("A");
            frontFragment.setCloseBtnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    flipCardContainer.flipCard();

                }
            });

            BaseCardFragment backFragment = BaseCardFragment.newInstance("B");
            backFragment.setCloseBtnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    flipCardContainer.backFlipCard();

                }
            });
            flipCardContainer.addFlipFragment(getSupportFragmentManager(), backFragment, frontFragment);
        }
    }
}
