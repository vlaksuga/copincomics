package com.copincomics.copinapp.tools;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemVerticalDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public SpacesItemVerticalDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = space;
        outRect.top = 0;
    }
}
