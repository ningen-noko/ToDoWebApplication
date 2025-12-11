package org.github.repository;

import org.github.entity.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.github.entity.Record;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    @Modifying
    @Query("UPDATE Record SET status=:status WHERE id=:id")
    void update(int id, @Param("status") RecordStatus newStatus);

    List<Record> findByStatus(RecordStatus status);
    List<Record> findByStatusAndTitleContainsOrderByIdDesc(RecordStatus status, String title);
    int countAllByStatus(RecordStatus status);
    Optional<Record> findFirstByTitleContains(String title);
}
