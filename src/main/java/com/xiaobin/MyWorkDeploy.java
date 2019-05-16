package com.xiaobin;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author:
 * @create: 2019-05-16 11:19
 **/
public class MyWorkDeploy {
    ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();

    /**流程部署*/
    @Test
    public void deployProcess() {
        Deployment deploy = pe.getRepositoryService()
                .createDeployment()
                .name("helloWorld入门程序")
                //从classpath文件加载
                .addClasspathResource("testactivity.bpmn")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**启动流程*/
    @Test
    public void startProcess() {
        pe.getRuntimeService()
                .startProcessInstanceByKey("helloworld1");
    }

    /** 查询流程*/
    @Test
    public void queryTask() {
        //节点接受人
        String assignee = "王五";

        List<Task> list = pe.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();

        list.forEach(v -> {
            System.out.println("任务Id:"+v.getId());
            System.out.println("任务名称:"+v.getName());
            System.out.println("任务创建时间:"+v.getCreateTime());
            System.out.println("任务办理人:"+v.getAssignee());
            System.out.println("流程实例Id:"+v.getProcessInstanceId());
            System.out.println("执行对象Id:"+v.getExecutionId());
            System.out.println("流程定义Id:"+v.getProcessDefinitionId());
        });
    }

    /**
     * 完成任务
     */
    @Test
    public void completeMyPersonTask(){
        String taskId = "10002";

        pe.getTaskService()
                .complete(taskId);

        System.out.println("任务已完成");
    }

    /** 删除流程 */
    @Test
    public void deleteDeployment(){
        //删除部署流程id
        String deploymnetId = "15001";

        //获取仓库服务对象
        pe.getRepositoryService()
                .deleteDeployment(deploymnetId,true);

        System.out.println("删除成功");
    }

    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void findLastVersionProcessDefinition() {

        List<ProcessDefinition> list = pe.getRepositoryService()
                //使用流程定义查询
                .createProcessDefinitionQuery()
                //正序排列所有流程定义版本
                .orderByProcessDefinitionVersion().desc()
                .list();

        //key:流程定义的key; value:流程定义对象;
        //当key值相等时，后值会覆盖前值
        HashMap<String, ProcessDefinition> map = new HashMap<>();
        if(list != null && list.size() > 0){
            list.forEach(v -> map.put(v.getKey(),v));
        }

        map.values().forEach(v -> {
            System.out.println("流程定义的ID" + v.getId());
            System.out.println("流程定义的名称"+ v.getName());
            System.out.println("流程定义的Key" + v.getKey());
            System.out.println("流程定义的版本" + v.getVersion());
            System.out.println("资源名称bpmn文件" + v.getResourceName());
            System.out.println("资源名称png文件" + v.getDiagramResourceName());
            System.out.println("部署对象ID" + v.getDeploymentId());
        });

    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefinitionByKey() {
        //流程定义的Key
        String processDefinitionKey = "helloworld1";

        //先使用流程定义的key查询流程定义，查询出所有的版本
        List<ProcessDefinition> list = pe.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        //遍历，获取每个流程定义的部署id
        if(list != null && list.size() != 0) {

            RepositoryService repositoryService = pe.getRepositoryService();
            //删除
            list.forEach(v -> repositoryService.deleteDeployment(v.getDeploymentId(),true));
        }
    }
}
