import dataview.models.GlobalSchedule;
import dataview.planners.WorkflowPlanner;
import dataview.planners.WorkflowPlanner_Naive2;
import dataview.workflowexecutors.WorkflowExecutor;
import dataview.workflowexecutors.WorkflowExecutor_Local;

import java.io.File;
import java.io.IOException;

public class NBodyTest {
    public static void main(String[] args) throws IOException {
        NBodyWorkflow w = new NBodyWorkflow();
        w.design();
        WorkflowPlanner wp = new WorkflowPlanner_Naive2(w);
        GlobalSchedule gsch = wp.plan();
        System.out.println(gsch.getSpecification());
        String fileLocation = System.getProperty("user.dir") + File.separator + "WebContent" +File.separator;
        try {
            WorkflowExecutor we = new WorkflowExecutor_Local(fileLocation+"workflowTaskDir"+ File.separator, fileLocation + "workflowLibDir"+ File.separator , gsch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
