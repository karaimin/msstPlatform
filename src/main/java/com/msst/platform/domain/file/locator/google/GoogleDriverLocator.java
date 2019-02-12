package com.msst.platform.domain.file.locator.google;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.file.locator.SubtitleContent;
import com.msst.platform.domain.file.locator.SubtitleLocator;
import com.msst.platform.domain.file.locator.google.query.AttributeQuery;
import com.msst.platform.domain.file.locator.google.query.GoogleDriveFileQuery;
import com.msst.platform.domain.file.locator.google.query.NameQueryFactory;
import com.msst.platform.domain.file.locator.google.query.ParentAttributeQueryFactory;
import com.msst.platform.web.rest.MovieResource;
import com.msst.platform.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service("google")
public class GoogleDriverLocator implements SubtitleLocator {
  private static final String ROOT_DIRECTORY_NAME = "Subtitles";
  private static final String ERROR_MESSAGE = "Something went wrong when fetching subtitles form server";

  private final Logger log = LoggerFactory.getLogger(GoogleDriverLocator.class);

  private String rootDirectoryId;

  @Autowired
  private DriveServiceProvider driveServiceProvider;

  @Override
  public Subtitle locate(String fileName) {
    return null;
  }

  @Override
  public List<SubtitleContent> getAllSubtitles() {
   try {
     Drive.Files.List request = createGetAllSubtitlesRequest();

     return getSubtitles(request);
   } catch (GoogleDriveLocatorException | IOException | GeneralSecurityException e) {
     throw new InternalServerErrorException(ERROR_MESSAGE);
   }
  }

  @Override
  public List<SubtitleContent> getSubtitlesByMovieName(String movieName) {
    try {
      Drive.Files.List request = createGetAllSubtitlesRequest();

      AttributeQuery attributeNameQuery = new NameQueryFactory(normalizeMovieNameForSearching(movieName)).contains();
      AttributeQuery IdInParentsQuery = new ParentAttributeQueryFactory(getRootDirectoryId()).in();
      GoogleDriveFileQuery generalQuery = new GoogleDriveFileQuery(IdInParentsQuery).and(attributeNameQuery);

      request.setQ(generalQuery.toString());

      return getSubtitles(request);

    } catch (GoogleDriveLocatorException | IOException | GeneralSecurityException e) {
      log.debug("Error getting subtitles", e);
      throw new InternalServerErrorException(ERROR_MESSAGE);
    }
  }

  @Override
  public SubtitleContent getSubtitleInfoById(String id) {
    try {
      File subtitleFile = driveServiceProvider.getDriveService().files().get(id).execute();
      return convertSubtitle(subtitleFile).build();

    } catch (GoogleJsonResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
        throw new InternalServerErrorException(id);
      }
      throw new InternalServerErrorException(ERROR_MESSAGE);
    } catch (IOException | GeneralSecurityException e) {
      throw new InternalServerErrorException(ERROR_MESSAGE);
    }
  }

  @Override
  public SubtitleContent downloadSubtitleFile(String id) {

    try {
      File subtitleFile = driveServiceProvider.getDriveService().files().get(id).execute();
      return convertSubtitle(subtitleFile).setFileContent(downloadFile(subtitleFile)).build();

    } catch (IOException | GeneralSecurityException e) {
      throw new InternalServerErrorException(ERROR_MESSAGE);
    }
  }

  @Override
  public boolean uploadFile(java.io.File file, String name) {
    try {
      String directoryId = getRootDirectoryId();

      File fileMetadata = new File();
      fileMetadata.setName(String.format("%s.srt", name));
      fileMetadata.setParents(Collections.singletonList(directoryId));
      FileContent mediaContent = new FileContent("application/octet-stream", file);

      File some = driveServiceProvider.getDriveService().files().create(fileMetadata, mediaContent).setFields("id, parents").execute();
      return true;
    } catch (IOException | GeneralSecurityException | GoogleDriveLocatorException e) {
      log.debug("Error uploading file", e);
      return false;
    }
  }

  private List<SubtitleContent> getSubtitles(Drive.Files.List request) throws IOException {
    List<SubtitleContent> subtitles = new ArrayList<>();
    do {
      FileList result = request.execute();

      for(File file : result.getFiles()){
        subtitles.add(convertSubtitle(file).build());
      }
      request.setPageToken(result.getNextPageToken());

    } while (request.getPageToken() != null && request.getPageToken().length() > 0);
    return subtitles;
  }


  private String getRootDirectoryId() throws IOException, GoogleDriveLocatorException, GeneralSecurityException {
    if(rootDirectoryId != null){
      return rootDirectoryId;
    }

    AttributeQuery attributeQuery = new NameQueryFactory(ROOT_DIRECTORY_NAME).equal();
    GoogleDriveFileQuery query = new GoogleDriveFileQuery(attributeQuery);

    FileList fileList = driveServiceProvider.getDriveService().files()
        .list()
        .setQ(query.toString())
        .execute();

    if(fileList.isEmpty()){
      throw new GoogleDriveLocatorException("Root Directory Could not be retrieved");
    }

    rootDirectoryId = fileList.getFiles().get(0).getId();
    return rootDirectoryId;
  }

  private Drive.Files.List createGetAllSubtitlesRequest() throws IOException, GoogleDriveLocatorException, GeneralSecurityException {
    String pageToken = null;

    AttributeQuery attributeQuery = new ParentAttributeQueryFactory(getRootDirectoryId()).in();
    GoogleDriveFileQuery query = new GoogleDriveFileQuery(attributeQuery);

    // @formatter:ff
    return driveServiceProvider.getDriveService().files().list()
        .setQ(query.toString())
        .setFields("nextPageToken, files(id, name)")
        .setPageToken(pageToken);

    // @formatter:on
  }

  private SubtitleContent.Builder convertSubtitle(File file)  {
    return new SubtitleContent.Builder().setProviderId(file.getId()).setSubtitleName(file.getName());
  }



  private String downloadFile(File file) throws IOException, GeneralSecurityException {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
      driveServiceProvider.getDriveService().files().get(file.getId()).executeMediaAndDownloadTo(out);

      return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
  }

  private String normalizeMovieNameForSearching(String movieName){
    // @formatter:off
    return new StringBuilder()
        .append(FILE_NAME_DELIMITER)
        .append(movieName)
        .append(FILE_NAME_DELIMITER)
        .toString();
    // @formatter:on
  }

}
