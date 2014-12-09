/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.service;

import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.constant.Enum;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.jsonperser.BloodGroupJson;
import com.bluepandora.therap.donatelife.jsonperser.BloodRequestJson;
import com.bluepandora.therap.donatelife.jsonperser.DistrictJson;
import com.bluepandora.therap.donatelife.jsonperser.DonationRecordJson;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonperser.DonatorMobileNumberJson;
import com.bluepandora.therap.donatelife.jsonperser.HospitalJson;
import com.bluepandora.therap.donatelife.jsonperser.RequestNameAdderJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.jsonperser.UserProfileJson;
import com.bluepandora.therap.donatelife.validation.DataValidation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class DataService extends DbUser {

    public static void getBloodGroupList(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String query = GetQuery.getBloodGroupListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = BloodGroupJson.getJsonBloodGroup(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();

    }

    public static void getHospitalList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String query = GetQuery.getHospitalListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = HospitalJson.getHospitalJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getDistrictList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String query = GetQuery.getDistrictListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = DistrictJson.getDistrictJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getBloodRequestList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        deleteBloodRequestBefore(Enum.MAX_DAY, dbService);
        String query = GetQuery.getBloodRequestListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = BloodRequestJson.getBloodRequestJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getUserProfile(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);

        if (request.getParameter("mobileNumber") != null && request.getParameter("keyWord") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String keyWord = request.getParameter("keyWord");

            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidKeyWord(keyWord)) {
                String hashKey = DataValidation.encryptTheKeyWord(keyWord);
                String query = GetQuery.getUserProfileQuery(mobileNumber, hashKey);
                //  Debug.debugLog("UserProfile: ", query);
                dbService.databaseOpen();
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = UserProfileJson.getUserProfileJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
                SendJsonData.sendJsonData(request, response, jsonObject);
                dbService.databaseClose();

            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
                SendJsonData.sendJsonData(request, response, jsonObject);
            }

        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void getUserDonationRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        JSONObject jsonObject = null;
        if (request.getParameter("mobileNumber") != null) {
            String mobileNumber = request.getParameter("mobileNumber");

            if (DataValidation.isValidMobileNumber(mobileNumber)) {
                dbService.databaseOpen();
                String query = GetQuery.getDonationRecordQuery(mobileNumber);
                ResultSet result = dbService.getResultSet(query);
                jsonObject = DonationRecordJson.getDonationRecordJson(result);
                dbService.databaseClose();
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);

        }
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void getDonatorMobileNumber(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("mobileNumber") != null
                && request.getParameter("keyWord") != null
                && request.getParameter("groupId") != null
                && request.getParameter("hospitalId") != null) {

            String mobileNumber = request.getParameter("mobileNumber");
            String keyWord = request.getParameter("keyWord");
            String groupId = request.getParameter("groupId");
            String hospitalId = request.getParameter("hospitalId");

            if (DataValidation.isValidMobileNumber(mobileNumber)
                    && DataValidation.isValidKeyWord(keyWord)
                    && DataValidation.isValidString(groupId)
                    && DataValidation.isValidString(hospitalId)) {

                String hashKey = DataValidation.encryptTheKeyWord(keyWord);

                boolean validUser = CheckService.isValidUser(mobileNumber, hashKey,dbService);

                if (validUser) {
                    dbService.databaseOpen();
                    String query = GetQuery.findBestDonatorQuery(groupId, hospitalId);
                    ResultSet result = dbService.getResultSet(query);
                    jsonObject = DonatorMobileNumberJson.getDonatorMobileNumberJson(result);
                    dbService.databaseClose();
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_USER);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
        }

        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
    }
    
    public static void deleteBloodRequestBefore(int day, DatabaseService dbService) {
        String query = GetQuery.deleteBloodRequestBeforeQuery(day);
        boolean done = dbService.queryExcute(query);
        if (done) {
            Debug.debugLog("BLOOD REQUEST BEFORE " + day + " DAYS IS DELETED");
        } else {
            Debug.debugLog("BLOOD REQUEST DELETION OCCURS ERROR!");
        }
    }

    public static void unknownHit(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_UNKNOWN_HIT);
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

}
