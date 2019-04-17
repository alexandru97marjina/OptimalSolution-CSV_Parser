package com.marjina.csvreader.batch;

import com.marjina.csvreader.controller.LoadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StatisticsListener implements StepExecutionListener {
    Logger logger = LoggerFactory.getLogger(LoadController.class);


    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("------------------------------------------------------------------------------------");
        logger.info(stepExecution.getStartTime()+" -startTime");
        logger.info(stepExecution.getReadCount()+" of record received");
        logger.info(stepExecution.getWriteCount()+" of records successful");
        logger.warn(stepExecution.getFilterCount()+" of records failed");
        logger.info(stepExecution.getEndTime()+" -endTime");
        logger.info("------------------------------------------------------------------------------------");

        return ExitStatus.COMPLETED;
    }
}
