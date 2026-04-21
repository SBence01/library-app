package com.example.library.service;

import com.example.library.domain.Customer;
import com.example.library.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setId(customerId);
        customer.setName("Kiss Péter");
        customer.setEmail("kiss.peter@email.com");
        customer.setPhoneNumber("+36301234567");
    }

    @Test
    void findAll_shouldReturnAllCustomers() {
        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());
        customer2.setName("Nagy Anna");
        customer2.setEmail("nagy.anna@email.com");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));

        List<Customer> result = customerService.findAll();

        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnCustomer_whenExists() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(customerId);

        assertEquals("Kiss Péter", result.getName());
        assertEquals("kiss.peter@email.com", result.getEmail());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(customerRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.findById(randomId));
    }

    @Test
    void save_shouldReturnSavedCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.save(customer);

        assertEquals("Kiss Péter", result.getName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void update_shouldUpdateAndReturnCustomer() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Kiss Péter József");
        updatedCustomer.setEmail("kiss.peter.jozsef@email.com");
        updatedCustomer.setPhoneNumber("+36309876543");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.update(customerId, updatedCustomer);

        assertNotNull(result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(customerRepository.existsById(customerId)).thenReturn(true);

        customerService.deleteById(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void deleteById_shouldThrowException_whenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(customerRepository.existsById(randomId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> customerService.deleteById(randomId));
    }
}