package sample;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader {
    private File fileToOpen;
    private BufferedReader br;
    private List<List<String>> data;

    public FileLoader(String path) {
        this.fileToOpen = new File(path);
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileToOpen), "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cleanData();
    }

    public FileLoader(File file) {
        this.fileToOpen = file;
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileToOpen), "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cleanData();
    }



    private void cleanData(){
        data = br.lines()
                .map(line -> Arrays.asList(line.toUpperCase().replace("\"", "").split(";")))
                .collect(Collectors.toList());
    }

    public Research research(String mlfb, boolean macro, boolean generic){
        Research r = new Research(mlfb);
        data.parallelStream()
                .forEach(list -> {
                    if (list.get(4).contains(mlfb) && generic) {
                        String poma = list.get(2);
                        if (macro) {
                            if (poma.length() > 2){
                                r.addResult(poma.substring(0, 2));
                            } else {
                                r.addResult(poma);
                            }
                        } else {
                            r.addResult(poma);
                        }
                    } else if (list.get(4).startsWith(mlfb)) {
                        String poma = list.get(2);
                        if (macro) {
                            if (poma.length() > 2) {
                                r.addResult(poma.substring(0, 2));
                            } else {
                                r.addResult(poma);
                            }
                        } else {
                            r.addResult(poma);
                        }
                    }

                });
        return r;
    }

    public int size(){
        return data.size();
    }
}
