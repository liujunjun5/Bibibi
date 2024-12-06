package com.Bibibi.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;

    @Value("${admin.account}")
    private String adminAccount;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${showFFmegLog}")
    private Boolean showFFmpegLog;

    @Value("${es.host.port}")
    private String esHostPort;

    @Value("${es.index.video.name}")
    private String esIndexVideoName;

    public String getEsHostPort() {
        return esHostPort;
    }

    public String getEsIndexVideoName() {
        return esIndexVideoName;
    }

    public Boolean getShowFFmpegLog() {
        return showFFmpegLog;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public String getAdminPassword() {
        return adminPassword;
    }


}