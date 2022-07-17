package backend.ma9selti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_generator")
    private Long id;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}
