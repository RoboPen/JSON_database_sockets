package client;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = "-t", description = "Type of the request")
    private String type;

    @Parameter(names = "-k", description = "Key")
    private String key;

    @Parameter(names = "-v", description = "Value to save in the database (only in case of a 'set' request")
    private String value;

    @Parameter(names = "-in", description = "Input file")
    private String inputFile;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
}