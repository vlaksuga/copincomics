package com.copincomics.copinapp.tools;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int columnSpacing;
    private final int rowSpacing;
    private final int margin;

    public GridSpacingItemDecoration(int spanCount, int columnSpacing, int rowSpacing, int margin) {
        this.spanCount = spanCount;
        this.columnSpacing = columnSpacing;
        this.rowSpacing = rowSpacing;
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if (spanCount == 2) {
            if (column == 0) {
                outRect.left = margin;
                outRect.right = columnSpacing / 2;
                outRect.bottom = rowSpacing;
            } else {
                outRect.right = margin;
                outRect.left = columnSpacing / 2;
                outRect.bottom = rowSpacing;
            }
        } else if (spanCount == 3) {
            if (column == 0) {
                outRect.left = margin;
                outRect.right = columnSpacing / 2;
                outRect.bottom = rowSpacing;
            } else if (column == 1) {
                outRect.left = columnSpacing / 2;
                outRect.right = columnSpacing / 2;
                outRect.bottom = rowSpacing;
            } else if (column == 2) {
                outRect.right = margin;
                outRect.left = columnSpacing / 2;
                outRect.bottom = rowSpacing;
            }
        }
    }
}

