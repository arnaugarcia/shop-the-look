package com.klai.stl.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coordinate.class);
        Coordinate coordinate1 = new Coordinate();
        coordinate1.setId(1L);
        Coordinate coordinate2 = new Coordinate();
        coordinate2.setId(coordinate1.getId());
        assertThat(coordinate1).isEqualTo(coordinate2);
        coordinate2.setId(2L);
        assertThat(coordinate1).isNotEqualTo(coordinate2);
        coordinate1.setId(null);
        assertThat(coordinate1).isNotEqualTo(coordinate2);
    }
}
