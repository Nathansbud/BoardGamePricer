import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BoardGamePricer {
    static private ArrayList<Game> scrapeGames(int pageCount, double filterPrice) {
        ArrayList<Game> gameList = new ArrayList<>();

        for(int i = 1; i < (pageCount+1); i++) {
            try {
                Document d = Jsoup.connect("https://boardgamegeek.com/browse/boardgame/page/"+i).get();
                Elements rows = (d.getElementById("collectionitems").child(0).select("> tr"));
                for(int j = 1; j < rows.size(); j++) { //j = 1 to skip header row
                    Game g = new Game();

                    //Grab id from the image, use this for the API call
                    g.setId(Integer.parseInt(rows.get(j).child(1).child(0).attr("href").split("/")[2]));
                    //Grab information from cells in the row
                    g.setRank(Integer.parseInt(rows.get(j).child(0).child(0).attr("name")));
                    g.setName(rows.get(j).child(2).child(1).child(0).text());
                    g.setGeekRating(Double.parseDouble(rows.get(j).child(3).text()));
                    g.setRating(Double.parseDouble(rows.get(j).child(4).text()));
                    g.setRatings(Integer.parseInt(rows.get(j).child(5).text()));

                    //Grab price info from the geek API
                    String gamePriceJson = Jsoup.connect("https://api.geekdo.com/api/amazon/legacyads?a_thing_textwithprices__="+g.getId()).ignoreContentType(true).execute().body();

                    //Check to see if has price info (not perfect, some simply have empty div)
                    if((gamePriceJson.indexOf(":")+2) < (gamePriceJson.length()-2)) { //Game has price data (not true if no longer sold!)
                        Document priceData = Jsoup.parse(gamePriceJson.substring(gamePriceJson.indexOf(":")+2, gamePriceJson.length() - 3));

                        Elements priceDivs = priceData.getElementsByClass("ulprice");
                        Elements prices = priceData.getElementsByClass("positive");

                        //Make sure has price at all; some are empty
                        if(prices.size() == 0 || priceDivs.size() == 0) {
                            continue;
                        }

                        for(int k = 0; k < prices.size(); k++) {
                            String price = prices.get(k).text();
                            //Correctly format amazon link
                            String priceLink = priceDivs.get(k).attr("href").replace("&amp;", "&").replace("\\", "");

                            if(price.startsWith("Too low to display")) { //Set price to -1 if gives "too low to display"
                                if(k == 0 && prices.size() == 2) {
                                    g.setAmazonLowPrice(-1);
                                    g.setAmazonLowPriceLink(priceLink);
                                } else {
                                    g.setAmazonNewPrice(-1);
                                    g.setAmazonNewPriceLink(priceLink);
                                }
                            } else {
                                int stop = 1;

                                for(int l = 1; l < price.length(); l++) {
                                    if(!Character.isDigit(price.charAt(l)) && price.charAt(l) != '.') { //Iterate over html until no longer reading the price value
                                        stop = l;
                                        break;
                                    }
                                }

                                if(k == 0 && prices.size() == 2) {
                                    g.setAmazonLowPrice(Double.parseDouble(price.substring(1, stop)));
                                    g.setAmazonNewPriceLink(priceLink);
                                } else {
                                    g.setAmazonNewPrice(Double.parseDouble(price.substring(1, stop)));
                                    g.setAmazonNewPriceLink(priceLink);
                                }
                            }
                        }

                        if(g.getAmazonNewPrice() < filterPrice || filterPrice < 0) gameList.add(g); //If filterPrice is negative, no filter
                    }
                }
            } catch(IOException e) {
                System.out.println("Failed!");
            }
        }
        return gameList;
    }

    public static void main(String[] args) {
        ArrayList<Game> gameList = scrapeGames(5, 30);
        Collections.sort(gameList, Game::compareTo);

        for(Game g : gameList) {
            if(g.getAmazonNewPrice() != -1) {
                String shortLink = g.getAmazonNewPriceLink().split("\\?")[0];
                shortLink = "https://amzn.com/"+shortLink.substring(shortLink.lastIndexOf("/")+1); //Put links into shortlink format

                System.out.println(g.getRank() + ") "+ g.getName() + ": " + g.getAmazonNewPrice() + " [" + shortLink + "]");
            }
        }
    }
}
