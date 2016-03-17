package com.passion.attendance.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.passion.attendance.PassionAttendance;

import java.util.HashMap;

/**
 * Created by Aayush on 3/8/2016.
 */
public class Staff {

    Integer id;
    String name;
    String organization;
    String imageUrl;
    String contactNumber;

    HashMap<String, String> extras;
    HashMap<String, String> preferences;

    /**
     * @param id
     * @param name
     * @param organization
     * @param imageUrl
     * @param contactNumber
     */
    public Staff(Integer id, String name, String organization, String imageUrl, String contactNumber) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.imageUrl = imageUrl;
        this.contactNumber = contactNumber;

        this.extras = new HashMap<>();
        this.preferences = new HashMap<>();
    }

    /**
     * @param id
     * @param name
     * @param organization
     * @param imageUrl
     * @param contactNumber
     * @param extras
     * @param preferences
     */
    public Staff(Integer id, String name, String organization, String imageUrl, String contactNumber, HashMap<String, String> extras, HashMap<String, String> preferences) {

        this.id = id;
        this.name = name;
        this.organization = organization;
        this.imageUrl = imageUrl;
        this.contactNumber = contactNumber;
        this.extras = extras;
        this.preferences = preferences;
    }

    /**
     * @param id
     * @param name
     * @param organization
     * @param imageUrl
     * @param contactNumber
     * @param extras
     */
    public Staff(Integer id, String name, String organization, String imageUrl, String contactNumber, HashMap<String, String> extras) {

        this.id = id;
        this.name = name;
        this.organization = organization;
        this.imageUrl = imageUrl;
        this.contactNumber = contactNumber;
        this.extras = extras;

        this.preferences = new HashMap<>();
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void addExtra(String key, String value){
        this.extras.put(key, value);
    }

    public void removeExtra(String key, String value){
        this.extras.remove(key);
    }

    public String getExtra(String key) {
        return (String) this.extras.get(key);
    }

    public HashMap<String, String> getExtras() {
        return extras;
    }

    public void setExtras(HashMap<String, String> extras) {
        this.extras = extras;
    }

    public HashMap<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(HashMap<String, String> preferences) {
        this.preferences = preferences;
    }

    public void setPreference(String key, String value){
        this.preferences.put(key, value);
    }

    public String getPreference(String key){
        return this.preferences.get(key);
    }

    public void saveStaff(Context context, Staff staff){
        SharedPreferences sp = context.getSharedPreferences(PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE);

        sp.edit()
                .putInt(PassionAttendance.KEY_ID, staff.getId())
                .putString(PassionAttendance.KEY_NAME, staff.getName())
                .putString(PassionAttendance.KEY_ORGANIZATION, staff.getOrganization())
                .putString(PassionAttendance.KEY_IMAGE_URL, staff.getImageUrl())
                .putString(PassionAttendance.KEY_CONTACT_NUMBER, staff.getContactNumber())
                .putString(PassionAttendance.KEY_EXTRAS, PassionAttendance.getStringFromMap(staff.getExtras()))
                .putString(PassionAttendance.KEY_PREFERENCES, PassionAttendance.getStringFromMap(staff.getPreferences()))
                .apply();
    }

    public Staff retrieveStaff(Context context){
        SharedPreferences sp = context.getSharedPreferences(PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE);

        Integer id = sp.getInt(PassionAttendance.KEY_ID, -1);
        String name = sp.getString(PassionAttendance.KEY_NAME, "");
        String organization = sp.getString(PassionAttendance.KEY_ORGANIZATION, "");
        String image = sp.getString(PassionAttendance.KEY_IMAGE_URL, "");
        String contact = sp.getString(PassionAttendance.KEY_CONTACT_NUMBER, "");

        HashMap<String, String> extras = PassionAttendance.getMapFromString(sp.getString(PassionAttendance.KEY_EXTRAS,
                ""));
        HashMap<String, String> preferences = PassionAttendance.getMapFromString(sp.getString(PassionAttendance.KEY_PREFERENCES,
                ""));

        return new Staff(id, name, organization, image, contact, extras, preferences);
    }
}
