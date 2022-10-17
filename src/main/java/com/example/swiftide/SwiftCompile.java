package com.example.swiftide;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Swift compile handles creating and writing to a file
 * handles compilation of set file
 */
public class SwiftCompile {

    public static void createFile(String filePath) throws IOException {
        try {
            File myObj = new File(filePath);
            if (!myObj.createNewFile()) {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static boolean writeFile(String text, String filePath) {
        try(FileWriter writer = new FileWriter(filePath)) {
            writer.write(text);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    // Returns the position of an error to display it in front of error message
    private static String getErrorPos(String string) {
        Pattern pattern = Pattern.compile("\\d+:\\d+");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /*
     runs script using /usr/bin/env swift
     returns string array with [output, error line pos, exit code]
     */
    public static String[] compileFile(String filePath) {
        String[] out = new String[3];
        String[] arg = new String[] {"/usr/bin/env", "bash", "-c", "swift " + filePath, "", ""};
        StringBuilder respond = new StringBuilder();
        try {
            Process proc = new ProcessBuilder(arg).start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String s;
            while ( (s = stdInput.readLine()) != null) {
                respond.append(s);
                respond.append('\n');
            }
            while ((s = stdError.readLine()) != null) {
                String errPos = getErrorPos(s);
                if (errPos != null) {
                    out[1] = errPos;
                }
                respond.append(s);
                respond.append('\n');
            }
            if (out[1] != null) {
                proc.destroy();
                if (proc.exitValue() != 0) {
                    out[2] = String.valueOf(proc.exitValue());
                }
            }
        } catch (IOException e) {
            respond.append(e.getMessage());
            e.printStackTrace();
        }
        out[0] = respond.toString();
        return out;
    }
}
