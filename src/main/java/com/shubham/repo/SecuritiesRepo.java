package com.shubham.repo;

import com.shubham.dto.SecurityDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecuritiesRepo extends JpaRepository<SecurityDto, String> {
}
