package com.mobile.fuhome.app;

/**
 * Created by Administrator on 2014/9/27.
 */
import android.util.Xml;

import com.mobile.fuhome.app.bean.UpdataInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

public class UpdataInfoParser {
    public static UpdataInfo getUpdataInfo(InputStream is) throws Exception{
        XmlPullParser  parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        int type = parser.getEventType();
        UpdataInfo info = new UpdataInfo();
        while(type != XmlPullParser.END_DOCUMENT ){
            switch (type) {
                case XmlPullParser.START_TAG:
                    if("version".equals(parser.getName())){
                        info.setVersion(parser.nextText());
                    }else if ("url".equals(parser.getName())){
                        info.setUrl(parser.nextText());
                    }else if ("description".equals(parser.getName())){
                        info.setDescription(parser.nextText());
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }
}