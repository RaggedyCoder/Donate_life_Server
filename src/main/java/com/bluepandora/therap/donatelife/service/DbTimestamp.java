/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Biswajit Debnath
 */
public class DbTimestamp {

    public static String getTimeStamp() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp.toString();
    }
}