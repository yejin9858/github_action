package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    public Optional<Address> findByGuAndDong(String gu, String dong);
}
