/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package quotes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class App {


    public static void main(String[] args) throws IOException {

        String filePath = System.getProperty("user.dir");

        if(args.length == 0 || args[0].equals("local-random")) {
            System.out.println(randomQuote(filePath));
        } else if(args[0].equals("internet-random")) {
            HttpURLConnection con = urlConnector();
            String[] swQuote = UrlReaderParser(con);
            if(swQuote.length == 0){
                System.out.println("The server did not respond.");
                System.out.println(randomQuote(filePath));
            } else { System.out.println(swQuote[0] + " - Ron Swanson");
            swansonQuoteWriter(swQuote); }
        } else if(args[0].equals("local-search")) {
            System.out.println(quoteByAuthor(filePath, args[1]));
            System.out.println(quoteByWord(filePath, args[1]));
        } else {
            System.out.println("Invalid search. Please enter \"local-random\", \"internet-random\", or \"local-search\"");
        }
    }

    static Gson gson = null;

    public String getGreeting() {
        return "Hello World!";
    }

    private static void swansonQuoteWriter(String[] swQuote) throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        Quotes newQuote = new Quotes("Ron Swanson",swQuote[0]);
        String filePath = System.getProperty("user.dir") + "\\src" +
                "\\main" +
                "\\resources\\recentquotes.json";
        File quotesFile = new File(filePath);
        Reader reader = Files.newBufferedReader(Paths.get(filePath));
        Quotes[] quotesArray = gson.fromJson(reader, Quotes[].class);
        Quotes[] newQuotesArray = new Quotes[quotesArray.length + 1];
        for(int i = 0; i < quotesArray.length; i++) {
            newQuotesArray [i] = quotesArray[i];
        }
        newQuotesArray[quotesArray.length] = newQuote;
        try(FileWriter quoteFileWriter = new FileWriter(quotesFile, Charset.forName("UTF8"))){
            gson.toJson(newQuotesArray, quoteFileWriter);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String[] UrlReaderParser(HttpURLConnection con) throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        InputStreamReader swQuoteInputStreamReader = new InputStreamReader(con.getInputStream());
        String swQuote = null;
        try (BufferedReader reader = new BufferedReader(swQuoteInputStreamReader)) {
            swQuote = reader.readLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String[] quote = gson.fromJson(swQuote, String[].class);
        return quote;
    }

    public static HttpURLConnection urlConnector() throws MalformedURLException, IOException {
        URL url = new URL("http://ron-swanson-quotes.herokuapp.com/v2/quotes");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    public static String randomQuote(String filePath) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filePath + "\\src" +
                "\\main" +
                "\\resources\\recentquotes.json"));
        Gson gson = new Gson();
        Quotes[] quotes = gson.fromJson(reader, Quotes[].class);
        Random rando = new Random();
        int index = rando.nextInt(quotes.length);
        Quotes q = quotes[index];
        String authorText = "Author: " + q.getAuthor() + " Quote: " + q.getText();

        return authorText;
    }

    public static String quoteByAuthor(String filePath, String author) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filePath + "\\src" +
                "\\main" +
                "\\resources\\recentquotes.json"));
        Gson gson = new Gson();
        Quotes[] quotes = gson.fromJson(reader, Quotes[].class);
        for(Quotes q : quotes){
            if (q.getAuthor().contains(author)){
                return "Author: " + q.getAuthor() + " Quote: " + q.getText();
            }
        }
        return "Author not found.";
    }
    public static String quoteByWord(String filePath, String word) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filePath + "\\src" +
                "\\main" +
                "\\resources\\recentquotes.json"));
        Gson gson = new Gson();
        Quotes[] quotes = gson.fromJson(reader, Quotes[].class);
        for(Quotes q : quotes){
            if (q.getText().contains(word)){
                return "Author: " + q.getAuthor() + " Quote: " + q.getText();
            }
        }
        return "Word not found.";
    }

}
