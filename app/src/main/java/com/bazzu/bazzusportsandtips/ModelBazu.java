package com.bazzu.bazzusportsandtips;

public class ModelBazu {
    private  int mImageResource;
    private  String mText1;
    public ModelBazu(int imageResource, String mtext1) {
        mImageResource = imageResource;
        mText1 = mtext1;
    }
    public String getText1() {
        return mText1;
    }
    public int getImageResource() {
        return mImageResource;
    }

}
