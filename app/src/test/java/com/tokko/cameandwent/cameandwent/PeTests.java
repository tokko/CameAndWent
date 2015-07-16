package com.tokko.cameandwent.cameandwent;

import com.tokko.cameandwent.cameandwent.peaccounting.classes.PayrollEvent;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.PayrollEventType;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.Projects;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

import static org.junit.Assert.assertNotNull;

public class PeTests{
    private static final String baseUrl = "http://my.accounting.pe/api/v1/";

    @Test
    public void lab(){
        PayrollEvent payrollEvent = new PayrollEvent();
        payrollEvent.setDate(new Date(System.currentTimeMillis()));
        payrollEvent.setHours(new BigDecimal(4));
        payrollEvent.setType(PayrollEventType.WORK_HOUR);
        Projects projects = get(Projects.class, "/company/%d/project", 269);
        assertNotNull(projects);


    }

    private <T> T get(Class<T> clz, String queryString, Object... args){
        String urlS = String.format(baseUrl + queryString, args);
        try{
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Token", "Yps7G9VoayR3ess");
            InputStream in = null;
            try{
                in = new BufferedInputStream(connection.getInputStream());
                Serializer serializer = new Persister();
                return serializer.read(clz, in);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(in != null)
                    in.close();
                connection.disconnect();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
