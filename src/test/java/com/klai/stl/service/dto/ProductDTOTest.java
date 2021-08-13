package com.klai.stl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.stl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setReference("REFERENCE1");
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setReference(productDTO1.getReference());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setReference("REFERENCE2");
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setReference(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }
}
