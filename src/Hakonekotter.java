import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;


public class Hakonekotter extends JFrame {
    public Hakonekotter() throws IOException, TwitterException {
        super("Hakonekotter");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        Twitter twitter = TwitterFactory.getSingleton();

        //Oauth
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        RequestToken requestToken = twitter.getOAuthRequestToken();
        String uriString = requestToken.getAuthorizationURL();

        Desktop desktop = Desktop.getDesktop();
        try{
            URI uri = new URI( uriString );
            desktop.browse( uri );
        }catch( Exception e ){
            e.printStackTrace();
        }

        String pin = br.readLine();

        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        twitter.setOAuthAccessToken(accessToken);

        //TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        add(new StatusTimeLine(twitter).getTimeLinePanel());
    }


    public static void main(String[] args) throws MalformedURLException, TwitterException {
        Hakonekotter application = null;
        try {
            application = new Hakonekotter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        application.setVisible(true);

    }
}