package com.shubham.repo;

import com.shubham.dto.PortfolioDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepo extends JpaRepository<PortfolioDto, String> {
}
