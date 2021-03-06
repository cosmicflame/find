package com.hp.autonomy.frontend.find.idol.dashboards.widgets;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.hp.autonomy.frontend.configuration.SimpleComponent;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Data
@Builder
@JsonDeserialize(builder = TimeLastRefreshedWidgetSettings.TimeLastRefreshedWidgetSettingsBuilder.class)
@EqualsAndHashCode(callSuper = false)
public class TimeLastRefreshedWidgetSettings extends SimpleComponent<TimeLastRefreshedWidgetSettings> implements WidgetSettings<TimeLastRefreshedWidgetSettings> {
    private final Map<String, Object> widgetSettings;
    private final String dateFormat;
    private final String timeZone;

    @Override
    @JsonAnyGetter
    public Map<String, Object> getWidgetSettings() {
        return Collections.unmodifiableMap(widgetSettings);
    }

    @SuppressWarnings({"WeakerAccess", "FieldMayBeFinal", "unused"})
    @JsonPOJOBuilder(withPrefix = "")
    public static class TimeLastRefreshedWidgetSettingsBuilder {
        private Map<String, Object> widgetSettings = new HashMap<>();
        private String dateFormat;
        private String timeZone;

        @SuppressWarnings("unused")
        @JsonAnySetter
        public TimeLastRefreshedWidgetSettingsBuilder widgetSetting(final String key, final Object value) {
            widgetSettings.put(key, value);
            return this;
        }
    }
}
