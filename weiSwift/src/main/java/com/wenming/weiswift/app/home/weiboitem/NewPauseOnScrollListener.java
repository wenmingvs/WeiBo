package com.wenming.weiswift.app.home.weiboitem;

import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class NewPauseOnScrollListener extends RecyclerView.OnScrollListener {

    private ImageLoader imageLoader;

    private final boolean pauseOnScroll;
    private final boolean pauseOnSettling;
    private final RecyclerView.OnScrollListener externalListener;

    public NewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnSettling) {
        this(imageLoader, pauseOnScroll, pauseOnSettling, null);
    }

    public NewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnSettling,
                                    RecyclerView.OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnSettling = pauseOnSettling;
        externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (pauseOnScroll) {
                    imageLoader.pause();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if (pauseOnSettling) {
                    imageLoader.pause();
                }
                break;
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (externalListener != null) {
            externalListener.onScrolled(recyclerView, dx, dy);
        }
    }
}