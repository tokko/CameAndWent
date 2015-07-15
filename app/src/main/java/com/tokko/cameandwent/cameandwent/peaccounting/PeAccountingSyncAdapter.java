package com.tokko.cameandwent.cameandwent.peaccounting;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.Project;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.Projects;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider.ID;

public class PeAccountingSyncAdapter extends AbstractThreadedSyncAdapter{
    private static final String baseUrl = "http://my.accounting.pe/api/v1/";
    private final ContentResolver contentResolver;

    public PeAccountingSyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }

    public PeAccountingSyncAdapter(
            Context context, boolean autoInitialize, boolean
            allowParallelSyncs
    ){
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(
            Account account, Bundle extras, String authority,
            ContentProviderClient provider, SyncResult syncResult
    ){
        syncProjects(provider);
    }

    private void syncProjects(ContentProviderClient provider){
        try{
            //noinspection ConstantConditions
            List<Project> projects = get(Projects.class, "/company/%d/project", 269)
                    .getProjectList();
            List<Integer> tagsIds = getTagIds(provider);
            List<Project> toUpdate = Stream.of(projects).filter(p -> tagsIds.contains(p.getId()))
                                           .collect(Collectors.toList());
            List<Project> toAdd = Stream.of(projects).filter(p -> !tagsIds.contains(p.getId()))
                                        .collect(Collectors.toList());
            Stream<ContentProviderOperation> updates = Stream.of(toUpdate).map(
                    p -> ContentProviderOperation.newUpdate(CameAndWentProvider.URI_TAGS)
                                                 .withSelection("%s=?", new String[]{ID, String
                                                         .valueOf(p.getId())})
                                                 .withValue(CameAndWentProvider.TAG, p.getName()
                                                 /*TODO: update active*/
                                                 ).build());
            Stream<ContentProviderOperation> inserts = Stream.of(toAdd).map(p -> ContentProviderOperation.newInsert(CameAndWentProvider.URI_TAGS)
                    .withValue(CameAndWentProvider.TAG, p.getName())
                            //TODO: set external = true
                            //TODO: set active
                    .withValue(CameAndWentProvider.ID, p.getId()).build());
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ops.addAll(updates.collect(Collectors.toList()));
            ops.addAll(inserts.collect(Collectors.toList()));
            provider.applyBatch(ops);

        }catch(NullPointerException | RemoteException | OperationApplicationException e){
            e.printStackTrace();
        }
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

    private List<Integer> getTagIds(ContentProviderClient provider) throws RemoteException{
        Cursor tagCursor = provider.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        //TODO: handle only external tags
        List<Integer> tags = new ArrayList<>();
        for(tagCursor.moveToFirst(); !tagCursor.isAfterLast(); tagCursor.moveToNext()){
            tags.add(tagCursor.getInt(tagCursor.getColumnIndex(ID)));
        }
        tagCursor.close();
        return tags;
    }
}
