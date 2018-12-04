package com.rah.mcpro;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class Room {
    String roomId;
    String groundName;
    String sport;
    String dist_in_km;
    String password;
    String pub;
    String players;
    String longitude;
    String latitude;
    String admin;
    String max;
    String current;
//    List<ChatMessage> messages;
//    int roomNumber;

    public Room() {
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getSport() {

        return sport;
    }

    public String getPlayers() {
        return players;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAdmin() {
        return admin;
    }

    public String getMax() {
        return max;
    }

    public String getCurrent() {
        return current;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public Room(String roomId, String groundName, String sport, String password, String pub, String players,
                String lat, String lon, String admin, String max, String current) {
        this.latitude=lat;
        this.dist_in_km="d";
        this.longitude=lon;
        this.sport = sport;
        this.groundName = groundName;
        this.password = password;
        this.pub = pub;
        this.roomId = roomId;
        this.max = max;
        this.current = current;
        this.admin = admin;
        this.players = players;

    }
    public String getDist_in_km() {
        return dist_in_km;
    }

    public void setDist_in_km(double lat,double lon) {
        if(latitude==null){
            dist_in_km="0";
        }
        else {
            System.out.println("latitude: " + latitude + " longitude: " + longitude);
            double thislat = Double.parseDouble(latitude);
            double thislon = Double.parseDouble(longitude);
            double d = 0;
            if ((lat == thislat) && (lon == thislon)) {
                d = 0;
            } else {
                double theta = lon - thislon;
                double dist = Math.sin(Math.toRadians(lat)) * Math.sin(Math.toRadians(thislat)) + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(thislat)) * Math.cos(Math.toRadians(theta));
                dist = Math.acos(dist);
                dist = Math.toDegrees(dist);
                dist = dist * 60 * 1.1515;
                dist = dist * 1.609344;

                d = dist;
                d=Math.round(d * 100D) / 100D;
            }
            this.dist_in_km = Double.toString(d);
        }
    }

//    public Room(String roomId, String groundName, String dist_in_km, String password, String pub) {
//        this.roomId = roomId;
//        this.groundName = groundName;
//        this.dist_in_km = dist_in_km;
//        this.password = password;
////        this.pub = pub;
////        this.players = players;
////        this.longitude = longitude;
////        this.latitude = latitude;
////        this.admin = admin;
////        this.max = max;
////        this.current = current;
//
////        messages = new ArrayList<>();
////        this.roomNumber = roomNum;
//
//    }

    public String getId() {return roomId;}
    public String getGroundName() {
        return groundName;
    }

    public void setGroundName(String groundName) {
        this.groundName = groundName;
    }

//    public String getDist_in_km() {
//        return dist_in_km;
//    }

//    public void setDist_in_km(String dist_in_km) {
//        this.dist_in_km = dist_in_km;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

//    public List getMessages() {
//        return messages;
//    }
}
