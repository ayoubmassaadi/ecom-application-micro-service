package net.massaadi.billingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import net.massaadi.billingservice.model.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date billingDate;
    private Long customerId;
    @OneToMany(mappedBy = "bill")
    private List<ProductItem> productsItems = new ArrayList<>();
    @Transient private Customer customer;
}
