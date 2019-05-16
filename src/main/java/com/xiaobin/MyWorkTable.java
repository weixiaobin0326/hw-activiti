package com.xiaobin;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @description:
 * @author:
 * @create: 2019-05-16 11:00
 **/
public class MyWorkTable {
    @Test
    public void creatTable(){
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();
    }
}
