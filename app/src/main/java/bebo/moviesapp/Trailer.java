package bebo.moviesapp;

public class Trailer {
  private   String videoKey,name;


    public Trailer(String name, String videoKey) {
        this.name = name;
        this.videoKey = videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
