package ge.taxistgela.bean;

/**
 * Created by Alex on 5/25/2015.
 */
public class Review {
    private int userID;
    private int driverID;
    private boolean orientationFlag;
    private double timeRating;
    private double comfortRating;
    private String text;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public boolean isOrientationFlag() {
        return orientationFlag;
    }

    public void setOrientationFlag(boolean orientationFlag) {
        this.orientationFlag = orientationFlag;
    }

    public double getTimeRating() {
        return timeRating;
    }

    public void setTimeRating(double timeRating) {
        this.timeRating = timeRating;
    }

    public double getComfortRating() {
        return comfortRating;
    }

    public void setComfortRating(double comfortRating) {
        this.comfortRating = comfortRating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
