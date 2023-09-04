package POJO; 

import java.io.Serializable;

public class VotingPoll implements Serializable 
{
    String vehicleFinalist;
    int votes;

    public VotingPoll(String vehicleFinalist, int votes) {
        this.vehicleFinalist = vehicleFinalist;
        this.votes = votes;
    }

    public String getVehicleFinalist() {
        return vehicleFinalist;
    }

    public int getVotes() {
        return votes;
    }

    public void incrementVotes() {
        votes++;
    }

    @Override
    public String toString() {
        return vehicleFinalist + ": " + votes;
    }
}