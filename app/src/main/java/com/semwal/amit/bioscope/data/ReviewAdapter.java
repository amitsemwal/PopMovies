package com.semwal.amit.bioscope.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.models.Review;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Amit on 18-Feb-16.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Context context, List<Review> reviewList) {
        super(context, 0, reviewList);
    }

    public ReviewAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Review review = getItem(position);
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.author.setText(review.getAuthor());
        viewHolder.content.setText(review.getContent());

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.author_name)
        TextView author;
        @Bind(R.id.review_content)
        TextView content;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
