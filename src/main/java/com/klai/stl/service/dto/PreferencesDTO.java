package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.ImportMethod;
import java.io.Serializable;

/**
 * A DTO for the {@link com.klai.stl.domain.Preferences} entity.
 */
public class PreferencesDTO implements Serializable {

    private ImportMethod importMethod;

    private String feedUrl;

    public ImportMethod getImportMethod() {
        return importMethod;
    }

    public void setImportMethod(ImportMethod importMethod) {
        this.importMethod = importMethod;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesDTO{" +
            ", importMethod='" + getImportMethod() + "'" +
            ", feedUrl='" + getFeedUrl() + "'" +
            "}";
    }
}
