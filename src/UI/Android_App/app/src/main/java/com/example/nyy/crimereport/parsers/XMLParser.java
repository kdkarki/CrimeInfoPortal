package com.example.nyy.crimereport.parsers;

import com.example.nyy.crimereport.model.City;
import com.example.nyy.crimereport.model.State;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nyy on 4/2/15.
 */
public class XMLParser {
    public static List<State> parseFeed(String content){
        try {

            boolean inDataItemTag = false;
            String currentTagName = "";
            State state = null;
            List<State> states = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("a:State")) {
                            inDataItemTag = true;
                            state = new State();
                            states.add(state);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("a:State")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && state != null) {
                            switch (currentTagName) {
                                case "a:StateCode":
                                    state.setCode(parser.getText());
                                    break;
                                case "a:StateID":
                                    state.setId(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:StateName":
                                    state.setName(parser.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            return states;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<City> parseFeed_city(String content){
        try {

            boolean inDataItemTag = false;
            String currentTagName = "";
            City city = null;
            List<City> cities = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("a:CityDetailView")) {
                            inDataItemTag = true;
                            city = new City();
                            cities.add(city);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("a:CityDetailView")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && city != null) {
                            switch (currentTagName) {
                                case "a:CityId":
                                    city.setId(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:Name":
                                    city.setName(parser.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            return cities;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
