package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Staff(String staff) {
        try {
            JSONObject s = new JSONObject(staff);

            this.id = s.getInt(PassionAttendance.KEY_ID);
            this.name = s.getString(PassionAttendance.KEY_NAME);
            this.organization = s.getString(PassionAttendance.KEY_ORGANIZATION);
            this.imageUrl = s.getString(PassionAttendance.KEY_IMAGE_URL);
            this.contactNumber = s.getString(PassionAttendance.KEY_CONTACT_NUMBER);
            this.extras = PassionAttendance.getStringMapFromString(
                    s.getString(PassionAttendance.KEY_EXTRAS)
            );
            this.preferences = PassionAttendance.getStringMapFromString(
                    s.getString(PassionAttendance.KEY_PREFERENCES)
            );
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast String to Staff");
        }


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
        return this.extras.get(key);
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
}
