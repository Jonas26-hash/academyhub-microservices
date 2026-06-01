package com.chavez.repository;

import com.chavez.entity.SagaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaLogRepository extends JpaRepository<SagaLog, Long> {
}
