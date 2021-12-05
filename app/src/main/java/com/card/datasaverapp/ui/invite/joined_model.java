package com.card.datasaverapp.ui.invite;

public class joined_model {

    private String joinedById, joinedFromId;

    public joined_model() {
    }

    public joined_model(String joinedById, String joinedFromId) {
        this.joinedById = joinedById;
        this.joinedFromId = joinedFromId;
    }

    public String getJoinedById() {
        return joinedById;
    }

    public void setJoinedById(String joinedById) {
        this.joinedById = joinedById;
    }

    public String getJoinedFromId() {
        return joinedFromId;
    }

    public void setJoinedFromId(String joinedFromId) {
        this.joinedFromId = joinedFromId;
    }
}
