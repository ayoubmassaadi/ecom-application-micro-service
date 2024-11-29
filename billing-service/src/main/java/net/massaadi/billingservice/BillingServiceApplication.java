package net.massaadi.billingservice;

import net.massaadi.billingservice.entities.Bill;
import net.massaadi.billingservice.entities.ProductItem;
import net.massaadi.billingservice.feign.CustomerRestClient;
import net.massaadi.billingservice.feign.ProductRestClient;
import net.massaadi.billingservice.model.Customer;
import net.massaadi.billingservice.model.Product;
import net.massaadi.billingservice.repository.BillRepository;
import net.massaadi.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner comaLineRunner(BillRepository billRepository, ProductItemRepository productItemRepository
									, CustomerRestClient customerRestClient
									, ProductRestClient productRestClient){
		Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
		Collection<Product> products = productRestClient.getAllProducts().getContent();

		return args -> {
			customers.forEach(customer -> {
				Bill bill = Bill.builder()
						.billingDate(new Date())
						.customer(customer)
						.customerId(customer.getId())
						.build();
				billRepository.save(bill);

				products.forEach(product -> {
					ProductItem productItem = ProductItem.builder()
							.bill(bill)
							.productId(product.getId())
							.quantity(1+new Random().nextInt(10))
							.unitPrice(product.getPrice())
							.build();
					productItemRepository.save(productItem);
				});
			});
		};
	}
}
