package com.fantasy.flipcard;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

@SuppressLint("InlinedApi")
public class FlipCardContainer extends FrameLayout {

    private static final String FRAGMENT_TAG_FRONT = "front_fragment";
    private static final String FRAGMENT_TAG_BACK = "back_fragment";
    private FrameLayout mBackContainer;
    private FrameLayout mFrontContainer;
    private boolean isBackCard;
    private boolean isAnimating;
    private OnFlipCardChangeListener mOnFlipCardSwitchListener;

    public interface OnFlipCardChangeListener {

        /**
         * 开始执行翻牌
         * 
         * @param isFlip2backCard
         *            ， 是否是翻向背面卡片。<br>
         */
        void onStartFlip(boolean isFlip2backCard);

        /**
         * 执行翻牌结束
         * 
         * @param isShowFrontCard
         *            当前显示的是否是真实的正面卡片。<br>
         */
        void onFlipEnd(boolean isShowFrontCard);
    }

    public FlipCardContainer(Context context) {
        super(context);
        init();
    }

    public FlipCardContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.flip_switcher_layout, this, true);
        mBackContainer = (FrameLayout) findViewById(R.id.back_view);
        mFrontContainer = (FrameLayout) findViewById(R.id.front_view);
    }

    public void setOnFlipCardSwitchListener(OnFlipCardChangeListener onFlipListener) {
        mOnFlipCardSwitchListener = onFlipListener;
    }

    public boolean isCurrentBackContainer() {
        return isBackCard;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void addFlipView(View frontView, View backView) {
        mBackContainer.addView(backView);
        mFrontContainer.addView(frontView);
    }

    public int getFrontContainerId() {
        return R.id.front_view;
    }

    public int getBackContainerId() {
        return R.id.back_view;
    }

    public String getFrontContainerTag() {
        return FRAGMENT_TAG_FRONT;
    }

    public String getBackContainerTag() {
        return FRAGMENT_TAG_BACK;
    }

    public void setCardSize(int width,int height) {
        LayoutParams backCardLp = (LayoutParams) mBackContainer.getLayoutParams();
        backCardLp.width = width;
        backCardLp.height = height;
        mBackContainer.setLayoutParams(backCardLp);

        LayoutParams frontCardLp = (LayoutParams) mFrontContainer.getLayoutParams();
        frontCardLp.width = width;
        frontCardLp.height = height;
        mFrontContainer.setLayoutParams(frontCardLp);
    }

    public void addFlipFragment(FragmentManager fragmentManager, Fragment backFragment, Fragment frontFragment) {

        fragmentManager.beginTransaction().add(getFrontContainerId(), frontFragment, FRAGMENT_TAG_FRONT)
                .commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(getBackContainerId(), backFragment, FRAGMENT_TAG_BACK)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public void flipCard() {

        setHardwareLayerTypeBeforeAnim();
        Animator animatorLeftIn = AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_in);
        animatorLeftIn.setTarget(mBackContainer);

        Animator animatorLeftOut = AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_out);
        animatorLeftOut.setTarget(mFrontContainer);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorLeftIn, animatorLeftOut);
        animatorSet.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFrontContainer.setLayerType(View.LAYER_TYPE_NONE, null);
                mBackContainer.setLayerType(View.LAYER_TYPE_NONE, null);
                mFrontContainer.setVisibility(View.GONE);
                isAnimating = false;
                if (mOnFlipCardSwitchListener != null) {
                    mOnFlipCardSwitchListener.onFlipEnd(false);
                }
            }
        });
        isBackCard = true;
        isAnimating = true;
        if (mOnFlipCardSwitchListener != null) {
            mOnFlipCardSwitchListener.onStartFlip(true);
        }
        animatorSet.start();
    }

    public void backFlipCard() {

        setHardwareLayerTypeBeforeAnim();
        Animator animatorLeftIn = AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_left_in);
        animatorLeftIn.setTarget(mFrontContainer);

        Animator animatorLeftOut = AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_left_out);
        animatorLeftOut.setTarget(mBackContainer);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorLeftIn, animatorLeftOut);
        animatorSet.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFrontContainer.setLayerType(View.LAYER_TYPE_NONE, null);
                mBackContainer.setLayerType(View.LAYER_TYPE_NONE, null);
                mBackContainer.setVisibility(View.GONE);
                isAnimating = false;
                if (mOnFlipCardSwitchListener != null) {
                    mOnFlipCardSwitchListener.onFlipEnd(true);
                }
            }
        });
        isBackCard = false;
        isAnimating = true;
        if (mOnFlipCardSwitchListener != null) {
            mOnFlipCardSwitchListener.onStartFlip(false);
        }
        animatorSet.start();
    }

    private void setHardwareLayerTypeBeforeAnim() {
        mFrontContainer.setVisibility(View.VISIBLE);
        mBackContainer.setVisibility(View.VISIBLE);
        mFrontContainer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mFrontContainer.buildLayer();
        mBackContainer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mBackContainer.buildLayer();
    }

}
