# MovieList
This app retrieves movies by consuming a web service provided by The Movie Database API.
The app will give you a choice on how you would like the movies to be listed by: most popular, 
highest rated or favorite movies. 
Favorite movies is a list of movies that the user selected as a favorite from either the most 
popular or highest rated list.  The favorites is stored in a relational SQLite database located
on the android device.
If no network is available it will automatically list the favorites. 
In order to run the app you must Replace the API_MOVIE_KEY with your key in the MovieConst.java 
class which is located in com.dzartek.movielist.datamodel. 
public static final String API_MOVIE_KEY = "Your API Key here"

Libraries used: 
Retrofit,
Picasso, 
SQLiteDatabase, 
SQLiteOpenHelper
com.google.youtube.player

I have the following dependencies in my gradle file:

    compile fileTree(dir: 'libs', include: ['*.jar']), testCompile 'junit:junit:4.12'
    
    compile 'com.android.support:appcompat-v7:23.3.0'
    
    compile 'com.android.support:design:23.3.0'
    
    compile 'com.google.code.gson:gson:2.6.2'
    
    compile 'com.squareup.retrofit2:retrofit:2.0.1'
    
    compile 'com.squareup.retrofit2:converter-gson:2.0.1'
    
    compile 'com.squareup.okhttp3:okhttp:3.2.0'
    
    compile 'org.glassfish:javax.annotation:10.0-b28'
    
    compile 'com.squareup.picasso:picasso:2.5.2'
    
    compile 'com.android.support:recyclerview-v7:23.3.0'
    
    compile files('libs/YouTubeAndroidPlayerApi.jar'
    
* I had to copy the com.google.youtube.player.jar file into my libs library.
