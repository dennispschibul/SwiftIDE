package com.example.swiftide;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SwiftController implements Initializable {

    private static final double EPSILON = 0.0000005;

    @FXML
    private CodeArea inputWindow;

    @FXML
    private TextArea outputWindow;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text exitCode;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // adds the Line numbers
        inputWindow.setParagraphGraphicFactory(LineNumberFactory.get(inputWindow));
        outputWindow.setWrapText(true);

    }

    /*
    Button event for run button
     */
    public void compile(ActionEvent a) {
        final Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // getting Path of file
                String currentDir = System.getProperty("user.dir");
                String filePath = currentDir + "/myFile.swift";
                filePath = filePath.replaceAll("\s", "\\\\ ");
                // 1/10
                updateProgress(0.0, 1.0);
                try {
                    // creates file at filePath
                    SwiftCompile.createFile(filePath);
                    updateProgress(0.2, 1.0);
                    // 2/10
                    // writes what is in inputWindow to file
                    boolean isWritten = SwiftCompile.writeFile(inputWindow.getText(), filePath);
                    //3/10
                    updateProgress(0.3, 1.0);
                    if (!isWritten) {
                        // 10/10
                        updateProgress(1.0, 1.0);

                        outputWindow.setText("ERROR: Writing to File");
                    } else {
                        // 5/10
                        updateProgress(0.7, 1.0);
                        // executes and receives compiler response
                        String[] s = SwiftCompile.compileFile(filePath);
                        // 10/10
                        updateProgress(1.0, 1.0);
                        if (s[1] == null) {
                            outputWindow.setText(s[0]);
                        } else {
                            String err = "Error at (" + s[1] + ") : " + s[0];
                            outputWindow.setText(err);
                        }
                        if (s[2] != null) {
                            exitCode.setText("Process finished with exit code " + s[2]);
                        } else {
                            exitCode.setText("");
                        }
                    }
                } catch (IOException e) {
                    // 10/10
                    updateProgress(1.0, 1.0);
                    outputWindow.setText(e.getMessage());
                    e.printStackTrace();
                }
                // to make the progress display for longer
                Thread.sleep(1000);
                updateProgress(0, 1.0);
                return null;
            }
        };
        // binding to progressbar to task to get updated bar
        progressBar.progressProperty().bind(task.progressProperty());
        // changing the color if progress is 100%
        progressBar.progressProperty().addListener(observable -> {
            if (progressBar.getProgress() >= 1 - EPSILON) {
                progressBar.setStyle("-fx-accent: #8CFFBA;");
            } else {
                progressBar.setStyle("-fx-accent: #D8DEE9;");
            }
        });


        final Thread thread = new Thread(task, "task-thread");
        thread.setDaemon(true);
        thread.start();
    }

    // sets text style every time any key is pressed
    public void textInput(KeyEvent e) {
        inputWindow.setStyleSpans(0, SwiftKeywords.computeHighlighting(inputWindow.getText()));
    }





}