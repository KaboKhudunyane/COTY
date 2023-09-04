package ServerApp;

import POJO.VotingPoll;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerApp 
{
    ObjectOutputStream oos;
    ObjectInputStream ois;
    ServerSocket serverSocket;
    Socket socket;

    Object receivedObject;
    ArrayList<VotingPoll> votingRecords;

    public ServerApp() 
    {
        try 
        {
            serverSocket = new ServerSocket(12345, 1);
            socket = serverSocket.accept();
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Server started! ");
        } 
        catch (IOException ioe) 
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    
    public void processClient() 
    {
        votingRecords = new ArrayList<>();
        votingRecords.add(new VotingPoll("Ford Ranger", 0));
        votingRecords.add(new VotingPoll("Audi A3", 0));
        votingRecords.add(new VotingPoll("BMW X3", 0));
        votingRecords.add(new VotingPoll("Toyota Starlet", 0));
        votingRecords.add(new VotingPoll("Suzuki Swift", 0));
        
        ArrayList<VotingPoll> lstVotingResults;
        String arrayAsString;

        while (true) 
        {
            try 
            {
                receivedObject = ois.readObject();
                
                if (receivedObject instanceof String) 
                {
                    String vote = (String) receivedObject;
                    for (VotingPoll poll : votingRecords) 
                    {
                        if (poll.getVehicleFinalist().equals(vote)) 
                        {
                            poll.incrementVotes();
                            break;
                        }
                    }
                    arrayAsString = votingRecords.toString();
                    oos.writeObject(arrayAsString);
                }
                oos.flush();
            } 
            catch (IOException | ClassNotFoundException e) 
            {
                e.printStackTrace();
            }
        }
    }
    public void closeConnection() 
    {
        try 
        {
            oos.close();
            ois.close();
            socket.close();
            serverSocket.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) 
    {
        ServerApp serverApp = new ServerApp();
        serverApp.processClient();
    }
}
