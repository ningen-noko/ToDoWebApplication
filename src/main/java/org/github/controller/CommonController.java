package org.github.controller;

import org.github.entity.RecordStatus;
import org.github.entity.dto.RecordsContainerDto;
import org.github.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommonController {
    private final RecordService recordService;

    @Autowired
    public CommonController(RecordService recordService) {
        this.recordService = recordService;
    }

    // Model нужна для передачи данных в ui
    @RequestMapping("/home")
    public String getMainPage(Model model, @RequestParam(name = "filter", required = false) String filterMode) {
        RecordsContainerDto container = recordService.findAllRecords(filterMode);

        model.addAttribute("numberOfDoneRecords", container.getNumberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.getNumberOfActiveRecords());
        model.addAttribute("records", container.getRecords());
        return "main-page";
    }

    @RequestMapping("/")
    public String redirectToHomePage() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/add-record", method = RequestMethod.POST)
    public String addRecord(@RequestParam String title) {
        recordService.saveRecord(title);
        return "redirect:/home";
    }

    @RequestMapping(value = "/make-record-done", method = RequestMethod.POST)
    public String makeRecordDone(@RequestParam int id,
                                 @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.updateRecordStatus(id, RecordStatus.DONE);
        return "redirect:/home" + (filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }

    @RequestMapping(value = "/delete-record", method = RequestMethod.POST)
    public String deleteRecord(@RequestParam int id,
                               @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.deleteRecord(id);
        return "redirect:/home" + (filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }
}
// QueryParameters -- Вместо @RequestParam, когда get параметров много, делают прослойку
/*class QueryParameters {
    private int id;
    private String filter;

    public QueryParameters(int id, String filter) {
        this.id = id;
        this.filter = filter;
    }

    public int getId() {
        return id;
    }

    public String getFilter() {
        return filter;
    }
}*/
