/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.servlet;

import com.bluepandora.therap.donatelife.constant.Request;
import com.bluepandora.therap.donatelife.constant.Enum;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.service.BloodRequestService;
import com.bluepandora.therap.donatelife.service.DataService;
import com.bluepandora.therap.donatelife.service.DonationService;
import com.bluepandora.therap.donatelife.service.FeedBackService;
import com.bluepandora.therap.donatelife.service.GcmProfileService;
import com.bluepandora.therap.donatelife.service.UserRegistrationService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
@WebServlet(name = "LifeService", urlPatterns = {"/LifeService"})
public class LifeService extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        Debug.debugLog("SERVICE SERVLET HITTED FROM : " + request.getRemoteAddr());
        String requestName = request.getParameter("requestName");

        try {
            if (requestName != null) {

                if (requestName.equals(Request.BLOOD_GROUP_LIST)) {
                    DataService.getBloodGroupList(request, response);
                } else if (requestName.equals(Request.HOSPITAL_LIST)) {
                    DataService.getHospitalList(request, response);
                } else if (requestName.equals(Request.DISTRICT_LIST)) {
                    DataService.getDistrictList(request, response);
                } else if (requestName.equals(Request.GET_BLOOD_REQUEST)) {
                    Debug.debugURL(request, response);
                    DataService.getBloodRequestList(request, response);
                } else if (requestName.equals(Request.USER_INFO)) {
                    DataService.getUserProfile(request, response);
                } else if (requestName.equals(Request.GET_DONATION_RECORD)) {
                    DataService.getUserDonationRecord(request, response);
                } else if (requestName.equals(Request.REGISTER)) {
                    UserRegistrationService.registerUser(request, response);
                } else if (requestName.equals(Request.FEEDBACK)) {
                    FeedBackService.addFeedback(request, response);
                } else if (requestName.equals(Request.GCM_UPDATE)) {
                    GcmProfileService.updateGCMKey(request, response);
                } else if (requestName.equals(Request.IS_REGISTER)) {
                    UserRegistrationService.userRegistrationCheck(request, response);
                } else if (requestName.equals(Request.ADD_BLOOD_REQUEST)) {
                    BloodRequestService.addBloodRequest(request, response);
                } else if (requestName.equals(Request.REMOVE_TRACKER)) {
                    BloodRequestService.removePersonBloodRequestTracker(request, response);
                } else if (requestName.equals(Request.ADD_DONATION_RECORD)) {
                    DonationService.addDonationRecord(request, response);
                } else if (requestName.equals(Request.REMOVE_DONATION_RECORD)) {
                    DonationService.removeDonationRecord(request, response);
                } else if (requestName.equals(Request.DELETE_BLOOD_REQUET_IN_KEY)) {
                    BloodRequestService.removeBloodRequest(request, response);
                } else if (requestName.equals(Request.UPDATE_USER_INFO)) {
                    UserRegistrationService.updateUserPersonalInfo(request, response);
                } else if (requestName.equals(Request.FIND_DONATOR_MOBILE_NUMBER)) {
                    DataService.getDonatorMobileNumber(request, response);
                } else {
                    Debug.debugLog("REQUEST NAME NOT MATCHED!");
                    Debug.debugURL(request, response);
                    DataService.unknownHit(request, response);
                }
            } else {
                Debug.debugLog("NO REQUEST NAME FOUND!");
                Debug.debugURL(request, response);
                DataService.unknownHit(request, response);
            }
        } catch (Exception error) {
            Debug.debugURL(request, response);
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, "PROBLEM ARISE WITH YOUR REQUEST IN SERVLET!");
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(LifeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(LifeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
