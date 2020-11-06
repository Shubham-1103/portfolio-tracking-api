package com.shubham.repo;

import com.shubham.dto.TradeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepo extends JpaRepository<TradeDto, Long> {
}
