package com.semwal.amit.bioscope.utils;

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
        public static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        public static final String SORT_KEY_PARAM = "sort_by";
        public static final String API_KEY_PARAM = "api_key";
    }

    public class LocalKeys {

        public static final String DETAIL_MOVIE_KEY = "MOVIE";
        public static final String SORT_SETTING_KEY = "sort_setting";
        public static final String SORT_POPULARITY_DESC = "popularity.desc";
        public static final String SORT_VOTE_AVERAGE_DESC = "vote_average.desc";
        public static final String HASHTAG = "  #Bioscope by AmitSemwal";
    }

}
