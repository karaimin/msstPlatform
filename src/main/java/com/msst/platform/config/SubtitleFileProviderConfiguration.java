package com.msst.platform.config;

import com.msst.platform.domain.file.locator.SubtitleLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubtitleFileProviderConfiguration {

    @Autowired
    private ApplicationContext context;

    @Bean("fileLocatorAlias")
    public SubtitleLocator getSubtitlesFileLocator(@Value("${subtitle.provider}") String qualifier) {
        return (SubtitleLocator) context.getBean(qualifier);
    }

}
