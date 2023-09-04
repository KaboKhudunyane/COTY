package ClientApp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;

public class ClientApp implements ActionListener 
{
    JFrame frame;
    JPanel headingPanel, centerPanel, infoPanel, textPanel, buttonPanel;
    JLabel headingLabel, finalistLabel;
    JComboBox<String> comboBox;
    JTextArea textArea;
    JButton voteButton, retrieveButton, exitButton;
    
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Socket socket;
    
    public ClientApp() 
    {
        frame = new JFrame("Car Of The Year Voting App");
        
        headingPanel = new JPanel();
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2,1));
        infoPanel = new JPanel();
        textPanel = new JPanel();
        buttonPanel = new JPanel();

        headingLabel = new JLabel("Vote for your Car Of The Year");

        finalistLabel = new JLabel("Finalists: ");
        comboBox = new JComboBox<>(new String[]{"Ford Ranger", "Audi A3", "BMW X3", "Toyota Starlet", "Suzuki Swift"});

        textArea = new JTextArea(100, 100);
        textArea.setEditable(false);

        voteButton = new JButton("Vote");
        retrieveButton = new JButton("Retrieve");
        exitButton = new JButton("Exit");
        voteButton.addActionListener(this);
        retrieveButton.addActionListener(this);
        exitButton.addActionListener(this);
    }
    public void setGUI() 
    {
        headingPanel.add(headingLabel);

        infoPanel.add(finalistLabel);
        infoPanel.add(comboBox);

        textPanel.add(textArea);
        
        centerPanel.add(infoPanel);
        centerPanel.add(textPanel);

        buttonPanel.add(voteButton);
        buttonPanel.add(retrieveButton);
        buttonPanel.add(exitButton);

        frame.add(headingPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void connectToServer() 
    {
        try 
        {
            socket = new Socket("localhost", 12345);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server");
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    public void castVote(String vote) 
    {
        try 
        {
            oos.writeObject(vote);
            oos.flush();
            System.out.println("Vote casted " + vote );
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
    public void retrieveVotingRecords() 
    {
        try 
        {
            oos.writeObject("RETRIEVE");
            oos.flush();

            Object result = ois.readObject();
            if (result instanceof String) 
            {
                String records = (String) result;
                updateTextArea("Voting Records:\n" + records);
            }
            System.out.println("records obtained");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    public void updateTextArea(String text) 
    {
        textArea.setText(text);
        System.out.println("Text area updated");
    }
    public void closeConnection() 
    {
        try 
        {
            oos.writeObject("Exit");
            oos.close();
            ois.close();
            socket.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource().equals(voteButton)) 
        {
            String selectedVote = (String) comboBox.getSelectedItem();
            castVote(selectedVote);
        }
        if (e.getSource().equals(retrieveButton)) 
        {
            retrieveVotingRecords();
        }

        if (e.getSource().equals(exitButton)) 
        {
            closeConnection();
            System.exit(0);
        }
    }
    public static void main(String[] args) 
    {
        ClientApp clientApp = new ClientApp();
        clientApp.setGUI();
        clientApp.connectToServer();
    }
}
