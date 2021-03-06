/*
 * Copyright 2017 Hewlett Packard Enterprise Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.frontend.find.idol.applications;

import com.hp.autonomy.frontend.configuration.ConfigException;
import com.hp.autonomy.frontend.configuration.ConfigurationComponentTest;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.Collections;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class IdolCustomApplicationsConfigTest extends ConfigurationComponentTest<IdolCustomApplication> {
    private final String APP_NAME = "Some app name";
    private final String EXAMPLE_URL = "http://some.url.com";
    private IdolCustomApplicationsConfig config;

    @Before
    public void setUp() {
        super.setUp();
        config = constructConfig(APP_NAME, EXAMPLE_URL);
    }

    @Override
    protected Class<IdolCustomApplication> getType() {
        return IdolCustomApplication.class;
    }

    @Override
    protected IdolCustomApplication constructComponent() {
        return IdolCustomApplication.builder()
                .applicationName(APP_NAME)
                .url(EXAMPLE_URL)
                .icon("hp-app")
                .openInNewTab(false)
                .enabled(false)
                .build();
    }

    @Override
    protected String sampleJson() throws IOException {
        return IOUtils.toString(
                getClass().getResourceAsStream("/com/hp/autonomy/frontend/find/idol/applications/customApplication.json")
        );
    }

    @Override
    protected void validateJson(final JsonContent<IdolCustomApplication> jsonContent) {
        jsonContent.assertThat()
                .hasJsonPathStringValue("$.applicationName", "Application name")
                .hasJsonPathStringValue("$.url", "http://example.url.com")
                .hasJsonPathStringValue("$.icon", "hp-monitor")
                .hasJsonPathBooleanValue("$.openInNewTab", true)
                .hasJsonPathBooleanValue("$.enabled", true);
    }

    @Override
    protected void validateParsedComponent(final ObjectContent<IdolCustomApplication> objectContent) {
        objectContent.assertThat().isEqualTo(
                IdolCustomApplication.builder()
                        .applicationName("Application name")
                        .url("http://example.url.com")
                        .icon("hp-monitor")
                        .openInNewTab(true)
                        .enabled(true)
                        .build()
        );
    }

    @Override
    protected void validateMergedComponent(final ObjectContent<IdolCustomApplication> objectContent) {
        objectContent.assertThat().isEqualTo(
                IdolCustomApplication.builder()
                        .applicationName(APP_NAME)
                        .url(EXAMPLE_URL)
                        .icon("hp-app")
                        .openInNewTab(false)
                        .enabled(false)
                        .build());
    }

    @Override
    protected void validateString(final String s) {
        assertThat(s, allOf(containsString("applicationName"), containsString("url")));
    }

    @Test
    public void testBasicValidateAcceptsAValidConfig() {
        try {
            config.basicValidate(null);
        } catch(ConfigException e) {
            fail("A valid configuration should not throw an exception.");
        }
    }

    @Test
    public void testBasicValidateRejectsEmptyAppName() {
        config = constructConfig("", EXAMPLE_URL);
        validateWithException("The application name must be a non-empty string, e.g. \"IDOL Admin\".");
    }

    @Test
    public void testBasicValidateRejectsNullAppName() {
        config = constructConfig(null, EXAMPLE_URL);
        validateWithException("The application name must be a non-empty string, e.g. \"IDOL Admin\".");
    }

    @Test
    public void testBasicValidateRejectsInvalidUrl() {
        config = constructConfig(APP_NAME, "abc");
        validateWithException("The URL provided for \"" + APP_NAME + "\" is malformed.");
    }

    @Test
    public void testBasicValidateRejectsNullUrl() {
        config = constructConfig(APP_NAME, null);
        validateWithException("The \"url\" property for \"" + APP_NAME + "\" must not be empty.");
    }

    @Test
    public void testBasicValidateWorksEvenWhenAppIsDisabled() {
        config = IdolCustomApplicationsConfig.builder()
                .application(
                        IdolCustomApplication.builder()
                                .enabled(false)
                                .build()
                )
                .build();

        validateWithException("The application name must be a non-empty string, e.g. \"IDOL Admin\".");
    }

    @Test
    public void testOverridingDefaultsWorks() {
        config = IdolCustomApplicationsConfig.builder()
                .application(
                        IdolCustomApplication.builder()
                                .applicationName(APP_NAME)
                                .url(EXAMPLE_URL)
                                .icon("hp-monitor")
                                .openInNewTab(true)
                                .enabled(false)
                                .build()
                )
                .build();

        final IdolCustomApplication app = config.getApplications().iterator().next();
        assertEquals(APP_NAME, app.getApplicationName());
        assertEquals(EXAMPLE_URL, app.getUrl());
        assertEquals("hp-monitor", app.getIcon());
        assertEquals(true, app.isOpenInNewTab());
        assertEquals(false, app.getEnabled());
    }

    @Test
    public void testEnabledDefaultsToTrue() {
        assertEquals(true, config.getApplications().iterator().next().getEnabled());
    }

    @Test
    public void testOpenInNewTabDefaultsToFalse() {
        assertEquals(false, config.getApplications().iterator().next().isOpenInNewTab());
    }

    @Test
    public void testIconDefaultsToEmptyString() {
        assertEquals("", config.getApplications().iterator().next().getIcon());
    }

    private void validateWithException(final String expected) {
        try {
            config.basicValidate(null);
            fail("An exception should have been thrown");
        } catch(ConfigException e) {
            assertThat(e.getMessage(), containsString(expected));
        }
    }

    private IdolCustomApplicationsConfig constructConfig(final String name, final String url) {
        return IdolCustomApplicationsConfig.builder()
                .applications(
                        Collections.singletonList(
                                IdolCustomApplication.builder()
                                        //Do not set optional values here to check default behaviour
                                        .applicationName(name)
                                        .url(url)
                                        .build()
                        )
                )
                .build();
    }
}
