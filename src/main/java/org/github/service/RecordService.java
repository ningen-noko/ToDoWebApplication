package org.github.service;

import org.github.entity.User;
import org.github.repository.RecordRepository;
import org.github.entity.Record;
import org.github.entity.RecordStatus;
import org.github.entity.dto.RecordsContainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordService {
    private final UserService userService;
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository, UserService userService) {
        this.recordRepository = recordRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public RecordsContainerDto findAllRecords(String filterMode) {
        User user = userService.getCurrentUser();
        List<Record> records = user.getRecords().stream()
                .sorted(Comparator.comparingInt(Record::getId))
                .collect(Collectors.toList());
        int numberOfDoneRecords = recordRepository.countAllByStatus(RecordStatus.DONE);
        int numberOfActiveRecords = recordRepository.countAllByStatus(RecordStatus.ACTIVE);

        if(filterMode == null || filterMode.isBlank()) {
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        }
        String filterModeUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterMode = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .toList();

        if(allowedFilterMode.contains(filterModeUpperCase)) {
            List<Record> filterRecords =  records.stream()
                    .filter(record -> record.getStatus() == RecordStatus.valueOf(filterModeUpperCase))
                    .toList();
            return new RecordsContainerDto(user.getName(), filterRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    public void saveRecord(String title) {
        if(title != null && !title.isBlank()) {
            recordRepository.save(new Record(title, userService.getCurrentUser()));
        }
    }

    public void updateRecordStatus(int id, RecordStatus newStatus) {
        recordRepository.update(id, newStatus);
    }

    public void deleteRecord(int id) {
        recordRepository.deleteById(id);
    }

}
