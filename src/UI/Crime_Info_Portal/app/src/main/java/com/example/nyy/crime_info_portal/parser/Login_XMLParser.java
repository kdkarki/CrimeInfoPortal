package com.example.nyy.crime_info_portal.parser;

import com.example.nyy.crime_info_portal.model.Auth;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by nyy on 4/19/15.
 */
public class Login_XMLParser {
    public static Auth parseFeed(String content){
        try {

            boolean inDataItemTag = false;
            String currentTagName = "";
            Auth auth = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("AuthenticateUserResponse")) {
                            inDataItemTag = true;
                            auth = new Auth();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("AuthenticateUserResponse")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && auth != null) {
                            if (currentTagName.equals("AuthenticateUserResult")) {
                                auth.set_result(Boolean.parseBoolean(parser.getText()));
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            return auth;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
