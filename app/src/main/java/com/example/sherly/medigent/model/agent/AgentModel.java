package com.example.sherly.medigent.model.agent;

import java.util.ArrayList;

public class AgentModel {
    Integer count;
    String status;
    ArrayList<DataAgentModel> agents;

    public Integer getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<DataAgentModel> getAgents() {
        return agents;
    }
}
