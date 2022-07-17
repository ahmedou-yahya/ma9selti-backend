package backend.ma9selti.repository;

import backend.ma9selti.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUsername(String username);
}
