package com.klai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Space.class);
        Space space1 = new Space();
        space1.setId(1L);
        Space space2 = new Space();
        space2.setId(space1.getId());
        assertThat(space1).isEqualTo(space2);
        space2.setId(2L);
        assertThat(space1).isNotEqualTo(space2);
        space1.setId(null);
        assertThat(space1).isNotEqualTo(space2);
    }
}
