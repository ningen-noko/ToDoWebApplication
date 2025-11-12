package org.github.service;

import org.github.dao.RecordDao;
import org.github.entity.Record;
import org.github.entity.RecordStatus;
import org.github.entity.dto.RecordsContainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordService {
    private final RecordDao recordDao;

    @Autowired
    public RecordService(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    @Transactional(readOnly = true)
    public RecordsContainerDto findAllRecords(String filterMode) {
        List<Record> records = recordDao.findAllRecords();
        int numberOfDoneRecords = (int)records.stream().filter(record -> record.getStatus() == RecordStatus.DONE).count();
        int numberOfActiveRecords = (int)records.stream().filter(record -> record.getStatus() == RecordStatus.ACTIVE).count();

        if(filterMode == null || filterMode.isBlank()) {
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }
        String filterModeUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterMode = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .toList();

        if(allowedFilterMode.contains(filterModeUpperCase)) {
            List<Record> filterRecords =  records.stream()
                    .filter(record -> record.getStatus() == RecordStatus.valueOf(filterModeUpperCase))
                    .toList();
            return new RecordsContainerDto(filterRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    public void saveRecord(String title) {
        // isBlank пустая ли строка
        if(title != null && !title.isBlank()) {
            recordDao.saveRecord(new Record(title));
        }
    }

    public void updateRecordStatus(int id, RecordStatus status) {
        recordDao.updateRecordStatus(id, status);
    }

    public void deleteRecord(int id) {
        recordDao.deleteRecord(id);
    }

}
