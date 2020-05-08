import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

import java.util.List;

public class Workflow_Average_Columns extends Workflow {
    public final String MATRIX_SOURCE_FILE = "INPUT_Matrix.txt";
    public final String DESTINATION_FILE_PREFIX = "RESULT_average_column_";
    private List<String> srcFilenames;

    public Workflow_Average_Columns() {
        // create the workflow object
        super("Workflow_Average_Columns", "Averages each column of a matrix.");
        // there will be 1 input to this workflow, which is the matrix whose columns shall be averaged
        wins = new DATAVIEW_BigFile[1];
        wins[0] = new DATAVIEW_BigFile(MATRIX_SOURCE_FILE);

        // there will be 1 output from this workflow, which is a file listing all of the column averages
        wouts = new DATAVIEW_BigFile[Task_Matrix_To_Columns.numOfColumns];
        for (int i = 0; i < Task_Matrix_To_Columns.numOfColumns; i++) {
            wouts[i] = new DATAVIEW_BigFile(DESTINATION_FILE_PREFIX + (i+1) + ".txt");
        }
    }

    public void design() {
        // step 1: create and add tasks
        Task toColumns = addTask("Task_Matrix_To_Columns");
        // number of columns must be updated in Task_Matrix_To_Columns for different sized matrices
        Task[] averageColumns = addTasks("Task_Average_Column", Task_Matrix_To_Columns.numOfColumns);

        // step 2: add edges between the tasks created in step 1
        // feed matrix to Task_Matrix_To_Columns from win[0] to the toColumns task
        addEdge(0, toColumns);
        // output column vectors from Task_Matrix_To_Columns to each instance of Task_Average_Column
        for (int i = 0; i < averageColumns.length; i++) {
            addEdge(toColumns, i, averageColumns[i], 0);
        }
        // connect instances of Task_Average_Column to output files
        for (int i = 0; i < Task_Matrix_To_Columns.numOfColumns; i++) {
            addEdge(averageColumns[i], i);
        }
    }
}
