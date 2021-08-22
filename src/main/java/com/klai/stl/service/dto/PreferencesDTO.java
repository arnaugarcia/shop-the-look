package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.ImportMethod;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link com.klai.stl.domain.Preferences} entity.
 */
public class PreferencesDTO implements Serializable {

    private ImportMethod importMethod;

    private String feedUrl;

    private Integer remainingImports;

    private String lastImportBy;

    private ZonedDateTime lastImportTimestamp;

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

    public Integer getRemainingImports() {
        return remainingImports;
    }

    public void setRemainingImports(Integer remainingImports) {
        this.remainingImports = remainingImports;
    }

    public String getLastImportBy() {
        return lastImportBy;
    }

    public void setLastImportBy(String lastImportBy) {
        this.lastImportBy = lastImportBy;
    }

    public ZonedDateTime getLastImportTimestamp() {
        return lastImportTimestamp;
    }

    public void setLastImportTimestamp(ZonedDateTime lastImportTimestamp) {
        this.lastImportTimestamp = lastImportTimestamp;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesDTO{" +
            ", importMethod='" + getImportMethod() + "'" +
            ", feedUrl='" + getFeedUrl() + "'" +
            ", remainingImports=" + getRemainingImports() +
            ", lastImportBy='" + getLastImportBy() + "'" +
            ", lastImportTimestamp='" + getLastImportTimestamp() + "'" +
            "}";
    }
}
