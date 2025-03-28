package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "customer_key")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerKey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cid")
    private Long customerId;
    @Column(name = "key_proxy")
    private String keyProxy;
    private Instant outdated;
    private Instant issued;
    private boolean active;
    private String type ;
    private String alias;
    private int price;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerKey that = (CustomerKey) o;
        return this.id != null && that.id != null && that.id.equals(this.id) ;
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
