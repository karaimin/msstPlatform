package com.msst.platform.domain.file.locator.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class DriveServiceProvider {

  private Drive driveService;

  @Autowired
  private ProviderConfigProperties configProperties;

  public Drive getDriveService() throws GeneralSecurityException, IOException {
    if(driveService == null){
      connect();
    }

    return driveService;
  }

  public void connect() throws GeneralSecurityException, IOException {
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    GoogleAuthorizationClient authClient = new GoogleAuthorizationClient(jsonFactory);

    NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    Credential credentials = authClient.getCredentials(httpTransport, configProperties);

    this.driveService = new Drive.Builder(httpTransport, jsonFactory, credentials).setApplicationName("movie-sub-server").build();
  }
}
