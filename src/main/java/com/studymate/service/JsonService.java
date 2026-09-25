package com.studymate.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studymate.model.AssignmentTask;
import com.studymate.model.Task;

import java.io.File;
import java.util.List;

public class JsonService {
    private final ObjectMapper mapper;

    public JsonService() {
        mapper = new ObjectMapper();
    }

    // EXPORT
    public void exportTasks(List<Task> tasks, String filePath)
            throws Exception {

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filePath), tasks);
    }

    // IMPORT
    public List<AssignmentTask> importTasks(String filePath)
            throws Exception {

        return mapper.readValue(
                new File(filePath),
                new TypeReference<List<AssignmentTask>>() {}
        );
    }
}
