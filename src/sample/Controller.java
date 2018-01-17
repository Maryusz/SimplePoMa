package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.net.URL;
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
    private Label labelNumber;

    @FXML
    private RadioButton familyResearch;

    @FXML
    private RadioButton genericResearch;

    @FXML
    private AreaChart<?, ?> lateralChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private PieChart mainChart;

    @FXML
    private TableView<?> statTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mlfbField.disableProperty().bind(isNotReady);
        ToggleGroup toggleGroup = new ToggleGroup();
        familyResearch.setToggleGroup(toggleGroup);
        genericResearch.setToggleGroup(toggleGroup);
        genericResearch.setSelected(true);

    }

    @FXML
    void chooseFile(ActionEvent event) {
        isNotReady.setValue(false);
        fileChooser = new FileChooser();
        fileLoader = new FileLoader(fileChooser.showOpenDialog(root.getScene().getWindow()));

    }

    void research(String strToSearch){
        Research microResearch = fileLoader.research(strToSearch.toUpperCase(), false, genericResearch.isSelected());
        Research macroResearch = fileLoader.research(strToSearch.toUpperCase(), true, genericResearch.isSelected());

        labelNumber.textProperty().setValue(
                String.format("Numero di elementi trovati: %d (%f %% / 100)", microResearch.getNumberOfPiecesFounded(),
                        (microResearch.getNumberOfPiecesFounded() * 100.0) / fileLoader.size()));

        microResearch.exclude("PA");
        microResearch.exclude("RO");
        microResearch.exclude("AC");
        microResearch.exclude("BR");
        microResearch.exclude("AC5");
        microResearch.exclude("IN");
        microResearch.exclude("BR");
        microResearch.exclude("PT");
        //microResearch.exclude("");



        initPieChart(macroResearch);
        initBarChart(microResearch);

    }

    private void initPieChart(Research research) {

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        research.getResults()
                .forEach((s,i) -> data.add(new PieChart.Data(String.format("%s = %f %%", s, i * 100.0 / research.getNumberOfPiecesFounded()), i)));
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

private void initTable(Research research) {

}

    @FXML
    void search(ActionEvent event) {
        research(mlfbField.getText());
    }

}

