// DAO (Data Access Object) -- этот класс будет работать с данными
// Обычно данные берут с бд, в нашем случае данные будут хранить в этом классе

package org.github.dao;

import org.github.entity.Record;
import org.github.entity.RecordStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class RecordDao {
    private final List<Record> records = new ArrayList<>(
            Arrays.asList(
                    new Record("Take a shower", RecordStatus.ACTIVE),
                    new Record("Buy flowers", RecordStatus.DONE),
                    new Record("Go to the gym", RecordStatus.ACTIVE)
            )
    );

    // Не возвращаем ссылку на оргинальный обьект
    public List<Record> findAllRecords() {
        return new ArrayList<>(records);
    }

    public void saveRecord(Record record) {
        records.add(record);
    }

    public void updateRecordStatus(int id, RecordStatus status) {
        for (Record record : records) {
            if(record.getId() == id) {
                record.setStatus(status);
                break;
            }
        }
    }

    public void deleteRecord(int id) {
        // removeIf удаляет все записи с аргументом title
        records.removeIf(item -> item.getId() == id);
    }
}
