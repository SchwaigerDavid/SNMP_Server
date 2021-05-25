/**
 * Sample Skeleton for 'GUI.fxml' Controller Class
 */

package controller;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.SnmpWalk;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable
{
    SnmpWalk walker =new SnmpWalk();
    private int[] currentCpu =new int[]{17,25,36,23,17,25,36,23,17,25,36,23,17,25,36,23,17,25,36,23,17,25,36,23,17,25,36,23};
    private int[] oneMinCpu =new int[]{20,21,25,24};
    private int[] fiveMinCpu =new int[]{19,22,24,23};


    @FXML
    private LineChart<?, ?> dia;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;


    @FXML // fx:id="backgroundpane"
    private AnchorPane backgroundpane; // Value injected by FXMLLoader

    @FXML // fx:id="ipField"
    private TextField ipField; // Value injected by FXMLLoader

    @FXML // fx:id="anfang"
    private Button anfang; // Value injected by FXMLLoader

    @FXML // fx:id="ausgabe"
    private TextArea ausgabe; // Value injected by FXMLLoader

    @FXML
    private Text gerätename;

    @FXML
    void onStoppButtonPressed(ActionEvent event)
    {
        System.out.println("Stop");
    }
    @FXML
    void onAbfrageButtonPressed(ActionEvent event)
    {
        try
        {
            ausgabe.setText(walker.main1(ipField.getText()));
            //funkt nicht
            //gerätename.setText(walker.getGerätename());
        }

        catch (IOException e)
        {
            showAlert("Abfrage fehlgeschlagen");
        }
    }

    public void showAlert(String error)
    {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You Fucked up");
        alert.setContentText(error);
        alert.show();
    }

    /*******************************************************************************************************************/
    /***********************************************GETTER UND SETTER***************************************************/
    /*******************************************************************************************************************/

    public void setAusgabe(String output)
    {
        ausgabe.setText(output);
    }


    public String getAusgabe()
    {
        return ausgabe.getText();
    }

    public void rotateArray(int [] array) {
        //array wird nach 1 min um 1 verschoben
        //todo nach 1 min wie TodoForNextGroup geht
        array[3]=array[2];
        array[2]=array[1];
        array[1]=array[0];
        array[0]=13; //todo gehört richtig gemacht

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //todo: versuch werte aus dem Snmp walk zu bekommen (funkt nicht)
        //walker.setCurrentCpuPercentage("0");
        //System.out.println(walker.getCurrentCpuPercentage());
        //current CPU percentage
        XYChart.Series series = new XYChart.Series();
        series.setName("current CPU percentage");
        series.getData().add(new XYChart.Data("1",currentCpu[0]));
        series.getData().add(new XYChart.Data("2",currentCpu[1]));
        series.getData().add(new XYChart.Data("3",currentCpu[2]));
        series.getData().add(new XYChart.Data("4",currentCpu[3]));
        series.getData().add(new XYChart.Data("5",currentCpu[4]));



        //"1 minute exponentially-decayed moving average of the CPU busy percentage."
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("1 minutes average");
        series2.getData().add(new XYChart.Data("1",oneMinCpu[0]));
        series2.getData().add(new XYChart.Data("2",oneMinCpu[1]));
        series2.getData().add(new XYChart.Data("3",oneMinCpu[2]));
        series2.getData().add(new XYChart.Data("4",oneMinCpu[3]));


        //"5 minute exponentially-decayed moving average of the CPU busy percentage."
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("5 minutes average");
        series3.getData().add(new XYChart.Data("1",fiveMinCpu[0]));
        series3.getData().add(new XYChart.Data("2",fiveMinCpu[1]));
        series3.getData().add(new XYChart.Data("3",fiveMinCpu[2]));
        series3.getData().add(new XYChart.Data("4",fiveMinCpu[3]));

        dia.getData().addAll(series,series2,series3);


    }

}
