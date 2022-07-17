package backend.ma9selti.controller;

import backend.ma9selti.exception.ResourceNotFoundException;
import backend.ma9selti.model.Customer;
import backend.ma9selti.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8087")
@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }


    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer newCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer){
        Customer oldCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Customer with id = " + id));

        oldCustomer.setPhoneNumber(customer.getPhoneNumber());
        oldCustomer.setPassword(customer.getPassword());
        oldCustomer.setUsername(customer.getUsername());

        return new ResponseEntity<>(customerRepository.save(oldCustomer), HttpStatus.OK);
    }

    @DeleteMapping("customers/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") Long id){
        customerRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("customers")
    public ResponseEntity<HttpStatus> deleteAllCustomers(){
        customerRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
