package com.redartis.expense.repository;

import com.redartis.expense.model.BugReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    @Query("SELECT b FROM BugReport b JOIN FETCH b.user WHERE b.id = :id")
    Optional<BugReport> findById(Long id);

    @Query("SELECT b FROM BugReport b JOIN FETCH b.user ORDER BY b.id DESC")
    List<BugReport> findAllOrderedByIdDesc();
}
