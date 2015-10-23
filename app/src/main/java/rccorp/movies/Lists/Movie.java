package rccorp.movies.Lists;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String name;
    private String posterUrl;
    private String userrating;
    private String overview;
    private String releasedate;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserrating() {
        return userrating;
    }

    public void setUserrating(String userrating) {
        this.userrating = userrating;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Movie(Parcel in){
        name=in.readString();
        overview=in.readString();
        posterUrl=in.readString();
        userrating=in.readString();
        releasedate=in.readString();
        id=in.readString();
    }

    public Movie(){

    }

    @Override
    public String toString() {
        return name + ": " + overview + ": " + posterUrl + ": " + userrating + ": " + releasedate + ": " + id;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeString(overview);
        out.writeString(posterUrl);
        out.writeString(userrating);
        out.writeString(releasedate);
        out.writeString(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
