package com.msst.platform.domain.file.locator.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class GoogleAuthorizationClient {
  private final JsonFactory jsonFactory;

  private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

  public GoogleAuthorizationClient(JsonFactory jsonFactory){
    this.jsonFactory = jsonFactory;
  }

  public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, ProviderConfigProperties properties) throws IOException {
    InputStream in = GoogleAuthorizationClient.class.getResourceAsStream(properties.getClientSecretDir());
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, jsonFactory, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(properties.getCredentialsFolder())))
        .setAccessType(properties.getAccessType())
        .build();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(properties.getUserId());
  }
}
