public class Game implements Comparable<Game> { //Comparable interface used to compare method for sorting purposes
    private String name;

    private int id;
    private int rank;

    private double geekRating;
    private double rating;

    private int ratings;


    //If prices are null, they do not exist; if they are -1, they are "too low to display" from Amazon's side!
    private double amazonLowPrice;
    private double amazonNewPrice;

    private String amazonLowPriceLink;
    private String amazonNewPriceLink;

    public Game() {

    }

    public int compareTo(Game g) {
        return Double.compare(amazonNewPrice, g.amazonNewPrice);
    }

    public String getName() {
        return name;
    }
    public void setName(String _name) {
        name = _name;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int _rank) {
        rank = _rank;
    }

    public double getGeekRating() {
        return geekRating;
    }
    public void setGeekRating(double _geekRating) {
        geekRating = _geekRating;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double _rating) {
        rating = _rating;
    }

    public double getAmazonLowPrice() {
        return amazonLowPrice;
    }
    public void setAmazonLowPrice(double _amazonLowPrice) {
        amazonLowPrice = _amazonLowPrice;
    }

    public double getAmazonNewPrice() {
        return amazonNewPrice;
    }
    public void setAmazonNewPrice(double _amazonNewPrice) {
        amazonNewPrice = _amazonNewPrice;
    }

    public String getAmazonLowPriceLink() {
        return amazonLowPriceLink;
    }
    public void setAmazonLowPriceLink(String _amazonLowPriceLink) {
        amazonLowPriceLink = _amazonLowPriceLink;
    }

    public String getAmazonNewPriceLink() {
        return amazonNewPriceLink;
    }
    public void setAmazonNewPriceLink(String _amazonNewPriceLink) {
        amazonNewPriceLink = _amazonNewPriceLink;
    }

    public int getId() {
        return id;
    }
    public void setId(int _id) {
        id = _id;
    }

    public int getRatings() {
        return ratings;
    }
    public void setRatings(int _ratings) {
        ratings = _ratings;
    }
}
