package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;

public class Research {
    //Rappresenta il risultato della ricerca
    private StringProperty mlfbPart;

    // Map che contiene Poma: quantita di risultati
    private SortedMap<String, Integer> results;


    public String getMlfbPart() {
        return mlfbPart.get();
    }

    public StringProperty mlfbPartProperty() {
        return mlfbPart;
    }

    public void setMlfbPart(String mlfbPart) {
        this.mlfbPart.set(mlfbPart);
    }

    public Research(String mlfbPart) {
        this.mlfbPart  = new SimpleStringProperty(this, "mlfbPart", mlfbPart);
        results = new TreeMap<>();

    }

    public synchronized void addResult(String poma){
        if (results.keySet().contains(poma)) {
            results.compute(poma, (s, integer) -> integer += 1);
        } else {
            results.put(poma, 1);
        }
    }

    public void exclude(String poma) {

        Iterator<Map.Entry<String, Integer>> it = results.entrySet().iterator();

        while (it.hasNext()) {
            String key = it.next().getKey();

            if (key.startsWith(poma) || key.equals("")) {
                it.remove();
            }
        }
    }


    public int getNumberOfPiecesFounded(){
        return results.values().stream().reduce(0, (n, nn) -> n + nn);
    }

    public SortedMap<String, Integer> getResults() {
        return results;

    }

    public void printResearch() {
        System.out.println("Ricerca per: " + getMlfbPart());
        results.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }
}
