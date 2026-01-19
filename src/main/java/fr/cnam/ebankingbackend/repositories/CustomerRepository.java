package fr.cnam.ebankingbackend.repositories;

import fr.cnam.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    //List<Customer> findByNameContainingIgnoreCase(String kw);

    @Query("select c from Customer c where c.name like :keyword")
    List<Customer> searchCustomerByKW(@Param("keyword")String keyword);
}
