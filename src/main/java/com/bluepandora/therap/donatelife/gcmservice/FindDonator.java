/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.gcmservice;

import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class FindDonator {

    /**
     * This method for finding GCM-ID from the Donate Life's database matched
     * with the Blood Group and District
     *
     * @param groupId
     * @param hospitalId
     */
    private static final int VALID_GCM_SIZE_GREATER = 8;

    public static List findDonator(String groupId, String hospitalId, String mobileNumber, DatabaseService dbService) {

        String query = GetQuery.getGcmIdOfDonatorQuery(groupId, hospitalId, mobileNumber);
        Debug.debugLog("FIND DONATOR: ", query);
        ResultSet result = dbService.getResultSet(query);
        List donatorList = new ArrayList<Donator>();
        String gcmId = null;

        try {
            while (result.next()) {
                gcmId = result.getString("gcm_id");
                mobileNumber = result.getString("mobile_number");

                if (gcmId.length() > VALID_GCM_SIZE_GREATER) {
                    donatorList.add(new Donator(mobileNumber, gcmId));
                }

            }
        } catch (SQLException error) {
            Debug.debugLog("FINDING DONATOR SQL EXCEPTION!");
        }
        return donatorList;

    }

    /**
     *
     * @param groupId
     * @param hospitalId
     * @param mobileNumber
     * @return This function will return the List of GCM ID without Requester 
     * mobileNumber's GCM ID;
     */
    public static List findDonatorGCMId(String groupId, String hospitalId, DatabaseService dbService) {
        String query = GetQuery.findBestDonatorQuery(groupId, hospitalId);
        Debug.debugLog("FIND DONATOR: ", query);
        ResultSet result = dbService.getResultSet(query);
        List donatorList = new ArrayList<String>();
        Debug.debugLog("DONATOR GCM ID FINDING");

        try {
            while (result.next()) {
                String mobileNumber = result.getString("mobile_number");
                String gcmId = result.getString("gcm_id");

                if (gcmId.length() > VALID_GCM_SIZE_GREATER) {
                    Debug.debugLog("Mobile: ", mobileNumber, "GCM Id: ", gcmId);
                    donatorList.add(gcmId);
                }
            }
        } catch (SQLException error) {
            Debug.debugLog("FINDING DONATOR'S GCM SQL EXCEPTION!");
        }
        return donatorList;
    }

    /**
     *
     * @param mobileNumber
     * @return This function will return the List of GCM ID of this mobile
     * Number
     */
    public static List findDonatorGCMId(String mobileNumber, DatabaseService dbService) {
        String query = GetQuery.getGcmIdOfDonatorQuery(mobileNumber);
        Debug.debugLog("FIND DONATOR: ", query);
        ResultSet result = dbService.getResultSet(query);
        List donatorList = new ArrayList<String>();
        Debug.debugLog("REQUESTER GCM ID FINDING");

        try {
            while (result.next()) {
                mobileNumber = result.getString("mobile_number");
                String gcmId = result.getString("gcm_id");

                if (gcmId.length() > VALID_GCM_SIZE_GREATER) {
                    Debug.debugLog("Mobile: ", mobileNumber, "GCM Id: ", gcmId);
                    donatorList.add(gcmId);
                }
            }
        } catch (SQLException error) {
            Debug.debugLog("FINDING DONATOR'S GCM SQL EXCEPTION!");
        }
        return donatorList;
    }

}
