package com.klai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpaceTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpaceTemplate.class);
        SpaceTemplate spaceTemplate1 = new SpaceTemplate();
        spaceTemplate1.setId(1L);
        SpaceTemplate spaceTemplate2 = new SpaceTemplate();
        spaceTemplate2.setId(spaceTemplate1.getId());
        assertThat(spaceTemplate1).isEqualTo(spaceTemplate2);
        spaceTemplate2.setId(2L);
        assertThat(spaceTemplate1).isNotEqualTo(spaceTemplate2);
        spaceTemplate1.setId(null);
        assertThat(spaceTemplate1).isNotEqualTo(spaceTemplate2);
    }
}
