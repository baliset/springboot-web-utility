package com.baliset.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javax.servlet.http.HttpServletRequest;


public class RequestBasedSerializationUtil {

    public static ObjectMapper acceptableMapper(String mimetype)
    {
        switch(mimetype)
        {
            case "application/xml":  return new ObjectMapper(new XmlFactory());
            case "text/xml":         return new ObjectMapper(new XmlFactory());
            case "text/x-yaml":      return new ObjectMapper(new YAMLFactory());
            case "application/json": return new ObjectMapper(new JsonFactory());
            default:                 return new ObjectMapper(new JsonFactory());
        }
    }


    public static String serializeInAcceptedFormat(HttpServletRequest req, Object o)
    {
        String result = "serialization: failure";
        String desiredResultMimeType = req.getHeader("accept");

        try {
            ObjectMapper mapper = acceptableMapper(desiredResultMimeType);
            result =  mapper.writer().writeValueAsString(o);  //instead of writerWithView(View).blah
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}