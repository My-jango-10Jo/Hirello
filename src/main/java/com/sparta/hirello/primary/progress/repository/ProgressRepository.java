package com.sparta.hirello.primary.progress.repository;

import com.sparta.hirello.primary.progress.entity.Progress;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long>, ProgressRepositoryCustom {

    boolean existsByTitle(String title);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Progress p where p.id = :id")
    Optional<Progress> findByIdWithPessimisticLock(Long id);

}