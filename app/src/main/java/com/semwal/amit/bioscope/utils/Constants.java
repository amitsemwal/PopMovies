package com.semwal.amit.bioscope.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;

import com.semwal.amit.bioscope.BuildConfig;

/**
 * Created by Amit on 21-Feb-16.
 */
public class Constants {
    public class JsonTags {
        public static final String RESULTS = "results";

        public class Summary {

            public static final String MOVIE_ID = "id";
            public static final String ORIGINAL_TITLE = "original_title";
            public static final String POSTER_PATH = "poster_path";
            public static final String BACKDROP_PATH = "backdrop_path";
            public static final String OVERVIEW = "overview";
            public static final String VOTE_AVERAGE = "vote_average";
            public static final String RELEASE_DATE = "release_date";
            public static final String POPULARITY = "popularity";
            public static final String VOTE_COUNT = "vote_count";
        }

        public class Trailer {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String KEY = "key";
            public static final String URL = "url";
            public static final String THUMB = "thumb";

        }

        public class Review {

            public static final String ID = "id";
            public static final String NAME = "author";
            public static final String REVIEW = "content";

        }
    }

    public class Api {

        public static final String IMAGE_URL_HIGH_QUALITY = "http://image.tmdb.org/t/p/w342";
        public static final String IMAGE_URL_LOW_QUALITY = "http://image.tmdb.org/t/p/w185";
        public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        public static final String SORT_KEY_PARAM = "sort_by";
        public static final String API_KEY_PARAM = "?api_key=" + BuildConfig.MOVIEDB_API_KEY;


    }

    public class LocalKeys {

        public static final String DETAIL_MOVIE_KEY = "MOVIE";
        public static final String VIEW_MODE_KEY = "view_mode";
        public static final String MOST_POPULAR = "popular";
        public static final String HIGHEST_RATED = "top_rated";
        public static final String FAVOURITES = "favourites";
        public static final String HASHTAG = "  #Bioscope by AmitSemwal";
    }
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


}
