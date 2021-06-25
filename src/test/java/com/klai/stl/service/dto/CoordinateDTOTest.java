package com.klai.stl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoordinateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoordinateDTO.class);
        CoordinateDTO coordinateDTO1 = new CoordinateDTO();
        coordinateDTO1.setId(1L);
        CoordinateDTO coordinateDTO2 = new CoordinateDTO();
        assertThat(coordinateDTO1).isNotEqualTo(coordinateDTO2);
        coordinateDTO2.setId(coordinateDTO1.getId());
        assertThat(coordinateDTO1).isEqualTo(coordinateDTO2);
        coordinateDTO2.setId(2L);
        assertThat(coordinateDTO1).isNotEqualTo(coordinateDTO2);
        coordinateDTO1.setId(null);
        assertThat(coordinateDTO1).isNotEqualTo(coordinateDTO2);
    }
}
