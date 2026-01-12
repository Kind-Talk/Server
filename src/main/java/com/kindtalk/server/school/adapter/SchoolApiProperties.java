package com.kindtalk.server.school.adapter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("school.api")
public record SchoolApiProperties(String url, String key) {

}
