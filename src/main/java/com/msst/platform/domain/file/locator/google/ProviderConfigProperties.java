package com.msst.platform.domain.file.locator.google;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:fileProvider/google.properties")
@ConfigurationProperties(prefix = "com.google.config")
public class ProviderConfigProperties {
  private String credentialsFolder;

  private String clientSecretDir;

  private String userId;

  private String accessType;

  public String getCredentialsFolder() {
    return credentialsFolder;
  }

  public void setCredentialsFolder(String credentialsFolder) {
    this.credentialsFolder = credentialsFolder;
  }

  public String getClientSecretDir() {
    return clientSecretDir;
  }

  public void setClientSecretDir(String clientSecretDir) {
    this.clientSecretDir = clientSecretDir;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAccessType() {
    return accessType;
  }

  public void setAccessType(String accessType) {
    this.accessType = accessType;
  }
}
