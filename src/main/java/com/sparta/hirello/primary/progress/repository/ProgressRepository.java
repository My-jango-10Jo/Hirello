package com.sparta.hirello.primary.progress.repository;

import com.sparta.hirello.primary.progress.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long>, ProgressRepositoryCustom {

    boolean existsByTitle(String title);

}