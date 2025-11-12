package org.github.dao;

import org.github.entity.Record;
import org.github.entity.RecordStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Record> findAllRecords() {
        Query query = entityManager.createQuery("SELECT r FROM Record r ORDER BY r.id ASC");
        List<Record> records = query.getResultList();

        return records;
    }

    public void saveRecord(Record record) {
        entityManager.persist(record);
    }

    public void updateRecordStatus(int id, RecordStatus status) {
//            единственный недостаток это выполнкние не нужного select запроса
//            Record record = entityManager.find(Record.class, id);
//            record.setStatus(status);
//            entityManager.merge(record);

//            второй способо более оптимизированный
        Query query = entityManager.createQuery("UPDATE Record SET status = :status WHERE id = :id");
        query.setParameter("status", status);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public void deleteRecord(int id) {
        // Уменьшение загрузки на бд, вместо двух запросов select и delete
        Query query = entityManager.createQuery("DELETE FROM Record WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
