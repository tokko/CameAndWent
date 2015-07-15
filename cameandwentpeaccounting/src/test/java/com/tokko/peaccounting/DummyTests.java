package com.tokko.peaccounting;

import com.tokko.cameandwentpeaccounting.classes.Projects;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class DummyTests{

    @Test
    public void testApi(){
        String baseUrl = "http://my.accounting.pe/api/v1/";
        String queryString = "/company/269/project";
        String urlS = String.format("%s%s", baseUrl, queryString);
        URL url;

        try{
            url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Token", "Yps7G9VoayR3ess");
            try{
                InputStream in = new BufferedInputStream(connection.getInputStream());
                Object o = unmarshalMessage(in);
                assertNotNull(o);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                connection.disconnect();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Object unmarshalMessage(InputStream xml) throws Exception{
        Serializer serializer = new Persister();
        Projects example = serializer.read(Projects.class, xml);
        return example;
    }
}
