package com.copincomics.copinapp.tools;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class SpacesItemHorizontalDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public SpacesItemHorizontalDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NotNull View view,
                               @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        outRect.right = space;
        outRect.bottom = 0;
        outRect.top = 0;
        outRect.left = 0;

    }
}
