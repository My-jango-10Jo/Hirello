package com.sparta.hirello.primary.column.repository;

import com.sparta.hirello.primary.column.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Columns, Long> {
   Optional<Columns> findByColumnName(String columnName);
}
