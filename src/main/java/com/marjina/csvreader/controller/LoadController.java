package com.marjina.csvreader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load")
public class LoadController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job_toDB;

    @Autowired
    Job job_BadData;

    Logger logger = LoggerFactory.getLogger(LoadController.class);

    @GetMapping
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        logger.debug("Checking file for bad data");
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters1 = new JobParameters(maps);
        JobExecution jobExecution_BadData=jobLauncher.run(job_BadData,parameters1);
        logger.info("Reading bad data to a file status="+jobExecution_BadData.getStatus());

        logger.debug("Reading records to DataBase");
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters2 = new JobParameters(maps);
        JobExecution jobExecution_toDB = jobLauncher.run(job_toDB, parameters2);
        logger.info("Finished data to DataBase status= "+jobExecution_toDB.getStatus());

        return jobExecution_toDB.getStatus();
    }
}
