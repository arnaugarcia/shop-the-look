package com.klai.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpaceTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpaceTemplateDTO.class);
        SpaceTemplateDTO spaceTemplateDTO1 = new SpaceTemplateDTO();
        spaceTemplateDTO1.setId(1L);
        SpaceTemplateDTO spaceTemplateDTO2 = new SpaceTemplateDTO();
        assertThat(spaceTemplateDTO1).isNotEqualTo(spaceTemplateDTO2);
        spaceTemplateDTO2.setId(spaceTemplateDTO1.getId());
        assertThat(spaceTemplateDTO1).isEqualTo(spaceTemplateDTO2);
        spaceTemplateDTO2.setId(2L);
        assertThat(spaceTemplateDTO1).isNotEqualTo(spaceTemplateDTO2);
        spaceTemplateDTO1.setId(null);
        assertThat(spaceTemplateDTO1).isNotEqualTo(spaceTemplateDTO2);
    }
}
