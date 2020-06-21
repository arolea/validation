package com.rolea.learning.validation;

import com.rolea.learning.validation.domain.Category;
import com.rolea.learning.validation.domain.Product;
import com.rolea.learning.validation.service.ProductAdditionalData;
import com.rolea.learning.validation.service.ProductValidationService;
import com.rolea.learning.validation.service.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.CollectionUtils.isEmpty;

@SpringBootTest
class ValidationApplicationTests {

	@Autowired
	private ProductValidationService productValidationService;

	@Test
	void testStatefulValidation() {
		List<Product> products = getProductList();
		ProductAdditionalData additionalData = getProductAdditionalData();

		Map<Product, ValidationResult> validationResults = productValidationService
				.validateProductsStateful(products, additionalData);

		validateResults(products, validationResults);
	}

	@Test
	void testStatelessValidation() {
		List<Product> products = getProductList();
		ProductAdditionalData additionalData = getProductAdditionalData();

		Map<Product, ValidationResult> validationResults = productValidationService
				.validateProductsStateless(products, additionalData);

		validateResults(products, validationResults);
	}

	private void validateResults(List<Product> products, Map<Product, ValidationResult> validationResults) {
		assertThat(validationResults.size()).isEqualTo(3);

		ValidationResult result = validationResults.get(products.get(0));
		assertThat(result.isValid()).isTrue();

		result = validationResults.get(products.get(1));
		assertThat(result.isValid()).isFalse();
		assertThat(result.getValidationWarnings().size()).isEqualTo(1);
		assertThat(result.getValidationErrors().size()).isEqualTo(2);

		result = validationResults.get(products.get(2));
		assertThat(result.isValid()).isFalse();
		assertThat(result.getValidationWarnings().size()).isEqualTo(1);
		assertThat(result.getValidationErrors().size()).isEqualTo(3);

		printValidationResults(validationResults);
	}

	private void printValidationResults(Map<Product, ValidationResult> validationResults) {
		if(!isEmpty(validationResults)){
			validationResults.forEach((product, result) -> {
				System.out.println("Validation result for product with id " + product.getId());

				System.out.println("\tProduct valid: " + result.isValid());
				if(!result.getValidationWarnings().isEmpty()){
					System.out.println("\tValidation warnings:");
					result.getValidationWarnings().forEach(warning ->
							System.out.println("\t\t- " + warning.getMessage()));
				}
				if(!result.getValidationErrors().isEmpty()){
					System.out.println("\tValidation errors:");
					result.getValidationErrors().forEach(error ->
							System.out.println("\t\t- " + error.getMessage()));
				}

				System.out.println();
			});
		}
	}

	private List<Product> getProductList(){
		return Arrays.asList(
				Product.builder()
						.id(1L)
						.name("First Product")
						.categories(Arrays.asList(
								Category.builder().name("Category 1").build(),
								Category.builder().name("Category 3").build()
						))
						.price(10D)
						.build(),
				Product.builder()
						.id(2L)
						.price(-1D)
						.categories(Arrays.asList(
								Category.builder().name("Category 1").build(),
								Category.builder().name("Category 2").build()
						))
						.build(),
				Product.builder()
						.id(3L)
						.price(null)
						.categories(null)
						.build()
		);
	}

	private ProductAdditionalData getProductAdditionalData(){
		Map<Long, Long> productStockMap = new HashMap<>();

		productStockMap.put(1L, 10L);
		productStockMap.put(2L, 1L);
		productStockMap.put(3L, null);

		return ProductAdditionalData.builder()
				.productStockData(productStockMap)
				.build();
	}

}
