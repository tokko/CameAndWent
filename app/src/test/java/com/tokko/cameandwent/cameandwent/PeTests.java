package com.tokko.cameandwent.cameandwent;

import android.content.ContentProviderClient;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.RemoteException;

import com.tokko.cameandwent.cameandwent.peaccounting.Networker;
import com.tokko.cameandwent.cameandwent.peaccounting.PeAccountingSyncAdapter;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.ActivityReadables;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.ActivityReference;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.ClientProjectReference;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.EventWritable;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.Project;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.Projects;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.UserReference;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 21, constants = BuildConfig.class, manifest = Config.NONE)
public class PeTests{

    @Test
    @Ignore
    public void getProjects_HealthCheck(){
        Projects projects = Networker.get(Projects.class, "/company/%d/project", 423);
        assertNotNull(projects);
        assertEquals(6, projects.getProjectList().size());
    }

    @Test
    @Ignore
    public void saveTimeStamp_HealthCheck(){
        UserReference userReference = new UserReference();
        //TODO: setid

        ClientProjectReference projectReference = new ClientProjectReference();
        List<Project> projectList = Networker.get(Projects.class, "/company/%d/project", 269).getProjectList();
        projectReference.setId(projectList.get(0).getId());

        ActivityReference activityReference = Networker.get(ActivityReadables.class, "/company/296/activity").getActivityReadableList().get(0)
                                                       .getId();
        EventWritable event = new EventWritable();
        event.setHours(new BigDecimal(4));
        event.setDate(new Date(System.currentTimeMillis()));
        event.setUser(userReference);
        event.setClientProject(projectReference);
        event.setActivity(activityReference);

        String queryString = "/company/269/event";
        //Networker.put(Created.class, event, queryString);
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
