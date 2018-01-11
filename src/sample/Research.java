package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Research {
    //Rappresenta il risultato della ricerca
    private StringProperty mlfbPart;

    // Map che contiene Poma: quantita di risultati
    private ConcurrentMap<String, Integer> results;

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
        results = new ConcurrentHashMap<>();
    }

    public synchronized void addResult(String poma){
        if (results.keySet().contains(poma)) {
            results.compute(poma, (s, integer) -> integer += 1);
        } else {
            results.put(poma, 1);
        }
    }

    public void exclude(String poma) {
        results.remove(poma);
    }

    public int getNumberOfPiecesFounded(){
        return results.values().stream().reduce(0, (n, nn) -> n + nn);
    }

    public ConcurrentMap<String, Integer> getResults() {
        return results;
    }

    public void printResearch() {
        System.out.println("Ricerca per: " + getMlfbPart());
        results.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }
}
