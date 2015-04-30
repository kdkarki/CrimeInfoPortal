package com.example.nyy.crimereport.parsers;

import com.example.nyy.crimereport.model.Address;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by nyy on 4/14/15.
 */
public class Address_XMLParser {
    public static Address parseFeed(String content){
        try {

            boolean inDataItemTag = false;
            String currentTagName = "";
            Address address = null;
            //List<Address> addresses = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("a:UserAddressDetailView")) {
                            inDataItemTag = true;
                            address = new Address();
                            //addresses.add(address);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("a:UserAddressDetailView")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && address != null) {
                            switch(currentTagName) {
                                case "a:Address1":
                                    address.set_address1(parser.getText());
                                    break;
                                case "a:Address2":
                                    address.set_address2(parser.getText());
                                    break;
                                case "a:AddressID":
                                    address.set_id(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:CityId":
                                    address.set_city_id(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:CityName":
                                    address.set_city_name(parser.getText());
                                    break;
                                case "a:IsPreferred":
                                    address.set_is_preferred(Boolean.parseBoolean(parser.getText()));
                                    break;
                                case "a:StateCode":
                                    address.set_state_code(parser.getText());
                                    break;
                                case "a:StateId":
                                    address.set_state_id(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:StateName":
                                    address.set_state_name(parser.getText());
                                    break;
                                case "a:Zipcode":
                                    address.set_zipcode(Integer.parseInt(parser.getText()));
                                    break;
                                case "a:Username":
                                    address.set_usr_name(parser.getText());
                                    break;
                                case "a:UserId":
                                    address.set_usr_id(Integer.parseInt(parser.getText()));
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
