/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.jsonbuilder;

import com.bluepandora.therap.donatelife.debug.Debug;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 * Get the Blood Group From Database and Make it a JSONObject
 */
public class BloodGroupJson {

    private static final String BLOOD_GROUP = "bloodGroup";
    private static final String GROUP_ID = "groupId";
    private static final String GROUP_NAME = "groupName";
    private static final String GROUP_BNAME = "groupBName";
    private static final String DONE = "done";

    public static JSONObject getJsonBloodGroup(ResultSet result) throws JSONException {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try {

            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(GROUP_ID, result.getString("group_id"));
                jsonObject.put(GROUP_NAME, result.getString("group_name"));
                jsonObject.put(GROUP_BNAME, result.getString("group_bname"));
                jsonArray.put(jsonObject);
            }
            
            jsonObject = new JSONObject();
            jsonObject.put(BLOOD_GROUP, jsonArray);
            jsonObject.put(DONE, 1);
            
        } catch (SQLException error) {
            Debug.debugLog("BLOOD_GROUP RESULT SET: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(DONE, 0);
            
        }
        return jsonObject;
    }

}
