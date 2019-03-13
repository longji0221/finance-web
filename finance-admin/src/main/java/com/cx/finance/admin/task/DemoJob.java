package com.cx.finance.admin.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 *@定时任务模板
 */

@Component
public class DemoJob {
    Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Scheduled(cron = "0 0 0 * * ?")
    public void laonDueJob(){
        try{
        } catch (Exception e){
            logger.error("DemoJob  error, case=",e);
        }
    }
}
