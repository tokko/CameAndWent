package com.tokko.cameandwent.cameandwent.peaccounting;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Networker{
    private static final String baseUrl = "http://my.accounting.pe/api/v1/";

    public static <T, T1> T put(Class<T> returnClass, T1 putObject, String queryString, Object... args){
        return null;
    }

    public static <T> T get(Class<T> clz, String queryString, Object... args){
        HttpURLConnection connection = getConnection(queryString, args);
        return readResponse(clz, connection);
    }

    private static HttpURLConnection getConnection(String queryString, Object... args){
        String urlS = String.format(baseUrl + queryString, args);
        try{
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Token", "Yps7G9VoayR3ess");
            return connection;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T readResponse(Class<T> clz, HttpURLConnection connection){
        InputStream in;
        try{
            in = new BufferedInputStream(connection.getInputStream());
            Serializer serializer = new Persister();
            return serializer.read(clz, in);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return null;
    }

}
