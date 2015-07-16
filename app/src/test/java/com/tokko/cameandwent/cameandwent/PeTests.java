package com.tokko.cameandwent.cameandwent;

import android.content.ContentProviderClient;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.RemoteException;

import com.tokko.cameandwent.cameandwent.peaccounting.Networker;
import com.tokko.cameandwent.cameandwent.peaccounting.PeAccountingSyncAdapter;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.PayrollEvent;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.PayrollEventType;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.Projects;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class PeTests{

    @Test
    public void getProjects_HealthCheck(){
        PayrollEvent payrollEvent = new PayrollEvent();
        payrollEvent.setDate(new Date(System.currentTimeMillis()));
        payrollEvent.setHours(new BigDecimal(4));
        payrollEvent.setType(PayrollEventType.WORK_HOUR);
        Projects projects = Networker.get(Projects.class, "/company/%d/project", 269);
        assertNotNull(projects);
        assertEquals(6, projects.getProjectList().size());
    }

    @Test
    @Ignore("How do you?? I dont even :(")
    public void sync() throws RemoteException{
        PeAccountingSyncAdapter syncAdapter = new PeAccountingSyncAdapter(RuntimeEnvironment
                                                                                  .application,
                                                                          true);
        ContentProviderClient provider = mock(ContentProviderClient.class);
        given(provider.query(any(Uri.class), any(String[].class), anyString(), any(String[].class), anyString())).willReturn(new MatrixCursor(new String[]{}));
        syncAdapter.onPerformSync(null, null, CameAndWentProvider.AUTHORITY, provider, null);
    }
}
