package com.example.melma.more;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < visibleItemCount) return;

        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        if (lastVisibleItem >= totalItemCount && !view.isStackFromBottom()) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
