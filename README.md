# PopMovies
Popular Movies App: A task from Android Nanodegree @ Udacity

**PopMovies** is a movies app that will allow a user to discover popular movies. [Read here](https://docs.google.com/document/d/1gtXUu1nzLGWrGfVCD6tEA0YHoYA9UNyT2yByqjJemp8/pub?embedded=true) the project description.

## To Do
  - Allow users to view and play trailers ( either in the youtube app or a web browser).
  - Allow users to read reviews of a selected movie.
  - Allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
  - Modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.
  - Optimize your app experience for tablet.
  
Implementation Guidance - Stage 2

Stage 2 - API Hints

To fetch the runtime of the film you will want to make a request to the /movies/{id} endpoint.
To fetch trailers you will want to make a request to the /movies/{id}/videos endpoint.
To fetch reviews you will want to make a request to the movies/{id}/reviews endpoint.
You should use an Intent to open a youtube link in either the native app or a web browser of choice.


## Consider using
  - [Fresco](http://frescolib.org/) for image loading
  - Retrifit for networking.
  - [Gson](https://github.com/google/gson) for JSON processing 
  - Automate Content Providers:
    - [ProviGen](https://github.com/TimotheeJeannin/ProviGen)
    - [schematic](https://github.com/SimonVT/schematic)
    - [simple provider](https://github.com/Triple-T/simpleprovider)
  - ORM instead of SQLite/ContentProvider:
    - [Realm](https://realm.io/docs/java)
    - [GreenDAO](http://greendao-orm.com/)
    - [Sugar ORM](http://satyan.github.io/sugar/index.html)
  - [OkHttp](http://square.github.io/okhttp/) as HTTP+SPDY Client
  - [Retrofit](http://square.github.io/retrofit/) as REST client :white_check_mark: ([a5899f0](http://git.io/vOz7E))
  - [Robolectric](https://github.com/robolectric/robolectric) and/or [Robotium](https://code.google.com/p/robotium/) for unit testing
  - [Xtends](http://futurice.com/blog/android-development-has-its-own-swift)
