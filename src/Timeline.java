/**
 * Created by hakoneko on 2016/10/18.
 */
import twitter4j.*;

import javax.security.auth.login.Configuration;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;

class StatusTimeLine implements ActionListener {
    Twitter twitter;
    TwitterStreamFactory twitterStreamFactory;
    TwitterStream twitterStream;

    JPanel timeLinePanel;
    JPanel updatePanel;
    JTextArea updateField;
    JButton updateButton;
     JButton toatoa;
     int toaNum =0;


    public StatusTimeLine(Twitter twitter) throws TwitterException {
        this.twitter = twitter;

        //this.twitterStreamFactory = new TwitterStreamFactory(configuration);
        //this.twitterStream= twitterStreamFactory.getInstance();
        //twitterStream.addListener(new MyStatusListener());
        //twitterStream.user();




        timeLinePanel = new JPanel();
        updateField = new JTextArea();

        updatePanel = new JPanel(new FlowLayout());
        updatePanel.setSize(800, 30);
        updatePanel.setPreferredSize(new Dimension(800, 60));
        updatePanel.setMaximumSize(new Dimension(800, 60));

        updateButton = new JButton("つぶやく");
        updateButton.setSize(90, 40);
        updateButton.setPreferredSize(new Dimension(90, 40));
        updateButton.setMaximumSize(new Dimension(90, 40));
        updateButton.addActionListener(this);

        toatoa = new JButton("ボタン");
        toatoa.setSize(90, 40);
        toatoa.setPreferredSize(new Dimension(90,40));
        toatoa.setMaximumSize(new Dimension(90, 40));
        toatoa.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        toaNum++;
                        try{
                            Date tweetDate = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                            twitter.updateStatus(formatter.format(tweetDate) + " にとあとあしました（" + toaNum +"回目）");
                            JOptionPane.showMessageDialog(null, "とあとあしました");
                        }catch (TwitterException ee) {
                            ee.printStackTrace();
                            JOptionPane.showMessageDialog(null, "とあとあは許可されていません");
                        }
                        try {
                            updateTimePanel();
                        } catch (TwitterException ee) {
                            ee.printStackTrace();
                        }
                        timeLinePanel.remove(1);
                        timeLinePanel.updateUI();
                        updateField.setText("");
                    }
                }
        );

        updateField = new JTextArea();
        updateField.setSize(600, 60);
        updateField.setPreferredSize(new Dimension(600, 60));
        updateField.setMaximumSize(new Dimension(600, 60));

        updatePanel.add(updateField);
        updatePanel.add(updateButton);
        updatePanel.add(toatoa);
        timeLinePanel.add(updatePanel);
        updateTimePanel();


    }

    private void updateTimePanel() throws TwitterException {
        java.util.List<Status> statusList = twitter.getHomeTimeline();
        List<Status> statuses = null;
        Paging pages = null;
        long lastStatusId = 0;

        Double lat = null;
        Double lng = null;
        String[] urls = null;
        String[] medias = null;


        String statusArr[] = new String[(statusList.size() * 2)];
        timeLinePanel.setLayout(new BoxLayout(timeLinePanel, BoxLayout.Y_AXIS));




        for (int i = 0; i < statusList.size(); i++) {
            Date tweetDate = statusList.get(i).getCreatedAt();
            SimpleDateFormat formatter = new SimpleDateFormat("yy-MMM-dd HH:mm:ss");
            statusArr[i * 2] = formatter.format(tweetDate) + ":" + "【" + statusList.get(i).getUser().getScreenName() + "】";
            statusArr[i * 2 + 1] = "　　　　　　　   　" + statusList.get(i).getText();
        }

        JList statusJList = new JList(statusArr);
        statusJList.setFixedCellHeight(30);
        statusJList.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(statusJList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        timeLinePanel.add(scrollPane);
    }

    public JPanel getTimeLinePanel() {
        return timeLinePanel;
    }

    public void actionPerformed(ActionEvent event) {
        try {
            twitter.updateStatus(updateField.getText());
            updateTimePanel();
            timeLinePanel.remove(1);
            timeLinePanel.updateUI();
            updateField.setText("");
        } catch (TwitterException exception) {
            JOptionPane.showMessageDialog(null, "つぶやきができませんでした。");
            try {
                updateTimePanel();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            timeLinePanel.remove(1);
            timeLinePanel.updateUI();
            updateField.setText("");
            exception.printStackTrace();
        }
    }
}