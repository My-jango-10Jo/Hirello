package com.sparta.hirello.primary.progress.repository;

import com.sparta.hirello.primary.progress.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
   Optional<Progress> findByProgressName(String columnName);
}
