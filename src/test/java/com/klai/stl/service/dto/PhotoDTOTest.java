package com.klai.stl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhotoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhotoDTO.class);
        PhotoDTO photoDTO1 = new PhotoDTO();
        photoDTO1.setReference("1L");
        PhotoDTO photoDTO2 = new PhotoDTO();
        assertThat(photoDTO1).isNotEqualTo(photoDTO2);
        photoDTO2.setReference(photoDTO1.getReference());
        assertThat(photoDTO1).isEqualTo(photoDTO2);
        photoDTO2.setReference("2L");
        assertThat(photoDTO1).isNotEqualTo(photoDTO2);
        photoDTO1.setReference(null);
        assertThat(photoDTO1).isNotEqualTo(photoDTO2);
    }
}
