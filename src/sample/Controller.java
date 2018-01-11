package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private BooleanProperty isNotReady = new SimpleBooleanProperty(true);
    private FileChooser fileChooser;
    private FileLoader fileLoader;

    @FXML
    private BorderPane root;

    @FXML
    private TextField mlfbField;

    @FXML
    private PieChart mainChart;

    @FXML
    private BarChart<String, Number> lateralChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mlfbField.disableProperty().bind(isNotReady);

    }

    @FXML
    void chooseFile(ActionEvent event) {
        isNotReady.setValue(false);
        fileChooser = new FileChooser();
        fileLoader = new FileLoader(fileChooser.showOpenDialog(root.getScene().getWindow()));

    }

    void research(String strToSearch){
        Research microResearch = fileLoader.research(strToSearch.toUpperCase(), false);
        Research macroResearch = fileLoader.research(strToSearch.toUpperCase(), true);


        microResearch.exclude("PA0001");
        microResearch.exclude("RO0001");
        microResearch.exclude("AC0001");
        microResearch.exclude("BR0001");
        microResearch.exclude("AC5000");
        microResearch.exclude("IN0000");
        microResearch.exclude("BR0000");

        initPieChart(macroResearch);
        initBarChart(microResearch);

    }

    private void initPieChart(Research research) {

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        research.getResults()
                .forEach((s,i) -> data.add(new PieChart.Data(s, i)));
        mainChart.setData(data);
    }

    private void initBarChart(Research research) {
        xAxis.setLabel("MLFB");
        yAxis.setLabel("Quantita");

        XYChart.Series data = new XYChart.Series();
        data.setName(mlfbField.getText());

        research.getResults()
                .forEach((s,i) -> data.getData().add(new XYChart.Data(s, i)));

        if (lateralChart.getData().size() > 0) {
            lateralChart.getData().remove(0);
        }
        lateralChart.getData().add(data);
}

    @FXML
    void search(ActionEvent event) {
        research(mlfbField.getText());
    }

}

