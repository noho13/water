package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Norman Hoeller on 9/19/13.
 * simple cursoradapter doing everything on mainthread which is bad, so that has to be changed
 */
public class MyAdapter extends CursorAdapter {

    private static final String TAG = MyAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public MyAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
//        do not do image decoding on UI Thread
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);
        textView.setText(path);
    }

}
