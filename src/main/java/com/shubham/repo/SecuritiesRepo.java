package com.shubham.repo;

import com.shubham.dto.SecurityDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecuritiesRepo extends JpaRepository<SecurityDto, String> {
}
