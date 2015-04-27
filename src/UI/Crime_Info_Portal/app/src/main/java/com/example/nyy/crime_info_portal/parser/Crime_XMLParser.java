package com.example.nyy.crime_info_portal.parser;

import com.example.nyy.crime_info_portal.model.Crime;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nyy on 4/19/15.
 */
public class Crime_XMLParser {
    public static List<Crime> parseFeed(String content){
        try {

            boolean inDataItemTag = false;
            String currentTagName = "";
            Crime crime = null;
            List<Crime> crimes = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("a:CriminalActivityRecordDetailView")) {
                            inDataItemTag = true;
                            crime = new Crime();
                            crimes.add(crime);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("a:CriminalActivityRecordDetailView")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && crime != null) {
                            switch(currentTagName) {
                                case "a:Address":
                                    crime.set_address(parser.getText());
                                    break;
                                case "a:Age":
                                    crime.set_age(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:ChargeCode":
                                    crime.set_charge_code(parser.getText());
                                    break;
                                case "a:RecordId":
                                    crime.set_Record_id(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:CityId":
                                    crime.set_city_id(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:CityName":
                                    crime.set_city_name(parser.getText());
                                    break;
                                case "a:CriminalEventType":
                                    crime.set_type(parser.getText());
                                    break;
                                case "a:DateArrested":
                                    crime.set_date_arrested(parser.getText());
                                    break;
                                case "a:DateReported":
                                    crime.set_type(parser.getText());
                                    break;
                                case "a:StateCode":
                                    crime.set_state_code(parser.getText());
                                    break;
                                case "a:StateId":
                                    crime.set_state_id(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:StateName":
                                    crime.set_state_name(parser.getText());
                                    break;
                                case "a:FirstName":
                                    crime.set_fname(parser.getText());
                                    break;
                                case "a:LastName":
                                    crime.set_lname(parser.getText());
                                    break;
                                case "a:MiddleName":
                                    crime.set_fname(parser.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            return crimes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

