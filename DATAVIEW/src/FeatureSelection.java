import dataview.models.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureSelection extends Task {
    public FeatureSelection() {
        super("FeatureSelection", "Filters columns based on the column index or column name.");

        ins = new InputPort[2];
        ins[0] = new InputPort("Data Set", Port.DATAVIEW_Table, "The data set we wish to use to filter the columns");
        ins[1] = new InputPort("Drop Columns", Port.DATAVIEW_String, "The list of column names/indices to drop");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Filtered Data Set", Port.DATAVIEW_MathMatrix, "The original data set, without the specified columns");
    }

    @Override
    public void run() {
        // read the data set and figure out which columns need to be dropped
        DATAVIEW_Table dataSet = (DATAVIEW_Table) ins[0].read();
        // string contains the list of column indices XOR string names given to columns; whitespace is trimmed from it
        String dropString = ((String) ins[1].read()).replaceAll("\\s+", "");
        String[] dropArr = dropString.split(",");
        if (!dropString.equals("")) {
            if (dropArr.length > dataSet.getNumOfColumns()) {
                throw new IllegalArgumentException("Number of columns specified to be dropped (" + dropArr.length +
                        ") exceeds the number of columns in the data set (" + dataSet.getNumOfColumns() + ")");
            }

            if (dropArr.length == dataSet.getNumOfColumns()) {
                throw new IllegalArgumentException("Number of columns specified to be dropped (" + dropArr.length +
                        ") equals the number of columns in the data set (" + dataSet.getNumOfColumns() + "); must leave at least 1 column remaining");
            }

            if (isIntList(dropArr)) { // no named columns, just column indices
                Integer[] dropIndices = makeIntArray(dropArr);
                // decrement indices; user inputs column numbers with 1 as the base index, dropColumns is 0 indexed
                for (int i = 0; i < dropIndices.length; i++) dropIndices[i]--;
                dataSet.dropColumns(dropIndices);
            } else { // we have named columns
                // ensure that the data set does have a valid header row
                String[] headerRow = null;
                if (hasHeaderRow(dataSet)) {
                    headerRow = dataSet.getRow(0);
                } else {
                    throw new IllegalArgumentException("Provided data set does not contain a named header row.");
                }

                // find the indices of the columns which need to be dropped
                ArrayList<Integer> dropIndices = new ArrayList<>();
                for (int i = 0; i < dropArr.length; i++) {
                    int colIndex = indexOf(dropArr[i], headerRow);
                    if (colIndex < 0) throw new IllegalArgumentException("Column named \"" + dropArr[i] + "\" could not be found in the data set.");
                    dropIndices.add(colIndex);
                }
                dataSet.dropColumns(objectArrToIntegerArr(dropIndices.toArray()));
            }
        }

        outs[0].write(dataSet);
    }

    private boolean hasHeaderRow(DATAVIEW_Table dataSet) {
        String[] rowArr = dataSet.getRow(0);
        StringBuilder header = new StringBuilder();
        header.append(rowArr[0]);
        for (int i = 1; i < rowArr.length; i++) {
            header.append("," + rowArr[i]);
        }
        Pattern p = Pattern.compile("([0-9]+\\.[0-9]+,)*([0-9]+\\.[0-9]+)\n?");  // row of comma separated doubles
        Matcher m = p.matcher(header.toString());
        return !m.matches();  // if we have a row of floats, we don't have a header row
    }

    private Integer[] objectArrToIntegerArr(Object[] arr) {
        Integer[] newArr = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = (Integer) arr[i];
        }
        return newArr;
    }

    /**
     * Used to check if a String[] contains all integers (as strings).
     * @param arr Array of Strings which we will check.
     * @return true if all strings in the array are ints; false otherwise
     */
    private boolean isIntList(String[] arr) {
        Pattern integerRE = Pattern.compile("[0-9]+");
        for (String str : arr) {
            Matcher integerMatcher = integerRE.matcher(str);
            if (!integerMatcher.matches()) return false;  // string is not an integer
        }
        return true;
    }

    private Integer[] makeIntArray(String[] arr) {
        Integer[] newArr = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = Integer.parseInt(arr[i]);
        }
        return newArr;
    }

    private int indexOf(String key, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(key)) return i;
        }
        return -1;
    }
}
