package com.yyx.GesturePassword;

/**
 * Created by java on 2017/4/20.
 */

public class PointViewData {
    private int row;//列
    private int col;//行
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int centerX;
    private int centerY;
    private int tag;

    public PointViewData() {
    }

    public PointViewData(int row, int col, int left, int right, int top, int bottom) {
        this.row = row;
        this.col = col;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        centerX = left+right/2;
        centerY = top+bottom/2;
    }

    public int getTag() {
        return tag;
    }

    public PointViewData setTag(int tag) {
        this.tag = tag;
        return this;
    }

    public int getRow() {
        return row;
    }

    public PointViewData setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public PointViewData setCol(int col) {
        this.col = col;
        return this;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
