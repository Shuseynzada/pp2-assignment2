package MovieManagement;
public class Movie {
    private String title;
    private String director; 
    private int releaseYear;
    private int runningTime; 

    public Movie (String Title, String director, int releaseYear, int runningTime){
        this.title = title; 
        this.director = director; 
        if(releaseYear >= 1885){
            this.releaseYear = releaseYear; 
        } 
        else {
            System.out.println("Please Enter Appropiate Release Year (between 1885 and 2023)!"); 
        } 
        if(runningTime != 0){
            this.runningTime = runningTime; 
        } 
        else {
            System.out.println("Please Enter Appropiate Running Time!");
        }
    }  

    public Movie (String Title, int releaseYear){
        this.title = title; 
        if(releaseYear >= 1885){
            this.releaseYear = releaseYear; 
        } 
        else {
            System.out.println("Please Enter Appropiate Release Year (between 1885 and 2023)!"); 
        } 
    } 

    public Movie (String Title, String director){
        this.title = title; 
        this.director = director; 
    } 

    public String getTitle() {
        return title;
    } 
    public String getDirector() {
        return director;
    } 
    public int getReleaseYear() {
        return releaseYear;
    }
    public int getRunningTime() {
        return runningTime;
    } 

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDirector(String director) {
        this.director = director;
    } 

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    } 

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    } 

    public String toString() {
        return title + "," + releaseYear + "," + director + "," + runningTime; 
    }

}
