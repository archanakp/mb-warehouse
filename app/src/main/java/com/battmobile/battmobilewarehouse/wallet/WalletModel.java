package com.battmobile.battmobilewarehouse.wallet;

public class WalletModel {
    String id, agent_name, tranferred_to_agent, amount, date, transferred_to, user_id;

    public String getId() {
        return id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public String getTranferred_to_agent() {
        return tranferred_to_agent;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getTransferred_to() {
        return transferred_to;
    }

    public String getUser_id() {
        return user_id;
    }

    public WalletModel(String id, String user_id, String amount, String transferred_to, String date,
                       String agent_name, String tranferred_to_agent) {
        this.id = id;
        this.agent_name = agent_name;
        this.tranferred_to_agent = tranferred_to_agent;
        this.amount = amount;
        this.date = date;
        this.transferred_to = transferred_to;
        this.user_id = user_id;
    }
}
