package com.klai.stl.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GoogleFeedProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoogleFeedProduct.class);
        GoogleFeedProduct googleFeedProduct1 = new GoogleFeedProduct();
        googleFeedProduct1.setId(1L);
        GoogleFeedProduct googleFeedProduct2 = new GoogleFeedProduct();
        googleFeedProduct2.setId(googleFeedProduct1.getId());
        assertThat(googleFeedProduct1).isEqualTo(googleFeedProduct2);
        googleFeedProduct2.setId(2L);
        assertThat(googleFeedProduct1).isNotEqualTo(googleFeedProduct2);
        googleFeedProduct1.setId(null);
        assertThat(googleFeedProduct1).isNotEqualTo(googleFeedProduct2);
    }
}
