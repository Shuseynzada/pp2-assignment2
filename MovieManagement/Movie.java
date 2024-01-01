package MovieManagement;

public class Movie {
    private int id;
    private String title;
    private String director;
    private int releaseYear;
    private int runningTime;

    public Movie(int id, String Title, String director, int releaseYear, int runningTime)
            throws InvalidRunningTimeException, InvalidReleaseYearException {
        this.id = id;
        this.title = Title;
        this.director = director;
        if (releaseYear < 1885 || releaseYear > 2023) {
            throw new InvalidReleaseYearException("Invalid release year: " + releaseYear);
        } else {
            this.releaseYear = releaseYear;
        }

        if (runningTime <= 0) {
            throw new InvalidRunningTimeException("Invalid running time: " + runningTime);
        } else {
            this.runningTime = runningTime;
        }
    }

    public Movie(String Title, String director, int releaseYear, int runningTime)
            throws InvalidRunningTimeException, InvalidReleaseYearException {
        this.title = Title;
        this.director = director;
        if (releaseYear < 1885 || releaseYear > 2023) {
            throw new InvalidReleaseYearException("Invalid release year: " + releaseYear);
        } else {
            this.releaseYear = releaseYear;
        }

        if (runningTime <= 0) {
            throw new InvalidRunningTimeException("Invalid running time: " + runningTime);
        } else {
            this.runningTime = runningTime;
        }
    }

    public Movie(String Title, int releaseYear) throws InvalidReleaseYearException {
        this.title = Title;
        if (releaseYear < 1885 || releaseYear > 2024) {
            throw new InvalidReleaseYearException("Invalid release year: " + releaseYear);
        } else {
            this.releaseYear = releaseYear;
        }
    }

    public Movie(String Title, String director) {
        this.title = Title;
        this.director = director;
    }

    // Getters
    public int getId() {
        return id;
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

    // Setters
    public void setId(int id) {
        this.id = id;
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
        return id + "," + title + "," + director + "," + releaseYear + "," + runningTime;
    }
}
