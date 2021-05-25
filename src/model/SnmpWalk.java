package model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class SnmpWalk extends Application
{
    //Bindings für die ausgabe in der GUI
   private StringProperty gerätename;
   private StringProperty currentCpuPercentage;
   private StringProperty oneMinCpuPercentage;
   private StringProperty fiveMinCpuPercentage;

    public String getGerätename() {
        return gerätename.get();
    }

    public StringProperty gerätenameProperty() {
        return gerätename;
    }

    public void setGerätename(String gerätename) {
        this.gerätename.set(gerätename);
    }

    public String getCurrentCpuPercentage() {
        return currentCpuPercentage.get();
    }

    public StringProperty currentCpuPercentageProperty() {
        return currentCpuPercentage;
    }

    public void setCurrentCpuPercentage(String currentCpuPercentage) {
        this.currentCpuPercentage.set(currentCpuPercentage);
    }

    public String getOneMinCpuPercentage() {
        return oneMinCpuPercentage.get();
    }

    public StringProperty oneMinCpuPercentageProperty() {
        return oneMinCpuPercentage;
    }

    public void setOneMinCpuPercentage(String oneMinCpuPercentage) {
        this.oneMinCpuPercentage.set(oneMinCpuPercentage);
    }

    public String getFiveMinCpuPercentage() {
        return fiveMinCpuPercentage.get();
    }

    public StringProperty fiveMinCpuPercentageProperty() {
        return fiveMinCpuPercentage;
    }

    public void setFiveMinCpuPercentage(String fiveMinCpuPercentage) {
        this.fiveMinCpuPercentage.set(fiveMinCpuPercentage);
    }


    public String main1(String ip) throws IOException
    {
        // Fehler: Oids auslesen noch nicht gemacht
        //todo wireshark
        //todo snmpj doku anschauen


        // StringBuilder
        StringBuilder resultSB = new StringBuilder();

        // Rückgabe String
        String resultString = null;

        // Zu ueberwachende Oids
        List<String> oids = new LinkedList<>();

        CommunityTarget target = new CommunityTarget();

        // SNMP Community setzen (standardmäßig public)
        target.setCommunity(new OctetString("public"));

        // IP-Adresse des SNMP-Agents festlegen
        target.setAddress(GenericAddress.parse("udp:" + ip + "/161")); // supply your own IP and port

        // Es wird zweimal versucht, die Information zu holen
        target.setRetries(2);

        // TimeOut festlegen
        target.setTimeout(1500);

        // SNMP-Version festlegen
        target.setVersion(SnmpConstants.version2c);

        //todo next group OIDs nochmal checken ob richtig
        //Oids, die ausgelesen werden sollen festlegen
        oids.add(".1.3.6.1.4.1.9.2.1.3"); // Gerätename
        oids.add(".1.3.6.1.2.1.1.1"); // Systembeschreibung
        oids.add(".1.3.6.1.2.1.1.3"); // UpTime
        oids.add(".1.3.6.1.2.1.1.7"); // Services
        oids.add(".1.3.6.1.2.1.25.2.2"); // RAM-Groesse
        oids.add(".1.3.6.1.4.1.9.2.1.56");//CPU busy percentage(current)
        oids.add(".1.3.6.1.4.1.9.2.1.57"); //CPU busy percentage(1min)
        oids.add(".1.3.6.1.4.1.9.2.1.58");//CPU busy percentage(5min)




        String oid;
        String typ;

        // Dauerschleife
        // while (true)
        // {
        for (int i = 0; i < oids.size(); i++)
        {
            oid = oids.get(i);

            Map<String, String> result = doWalk(oid, target); // ifTable, mib-2 interfaces

            for (Map.Entry<String, String> entry : result.entrySet())
            {
                typ = entry.getKey();

                // Es wird anhand der OID erkannt, welche Daten geliefert werden
                switch (typ)
                {
                    case ".1.3.6.1.4.1.9.2.1.3":
                        System.out.println("Gerätename:");
                        resultSB.append("Gerätename:").append("\n");
                        gerätename.set(entry.getValue());
                        break;

                    case ".1.3.6.1.2.1.1.1.0":
                        System.out.println("Systembeschreibung:");
                        resultSB.append("Systembeschreibung:").append("\n");
                        break;

                    case ".1.3.6.1.2.1.1.3.0":
                        System.out.println("UpTime:");
                        resultSB.append("UpTime:").append("\n");
                        break;

                    case ".1.3.6.1.2.1.1.7.0":
                        System.out.println("Services:");
                        resultSB.append("Services:").append("\n");
                        break;

                    case ".1.3.6.1.2.1.25.2.2.0":
                        System.out.println("RAM Größe:");
                        resultSB.append("RAM Größe:").append("\n");
                        break;
                    case ".1.3.6.1.4.1.9.2.1.58":
                        System.out.println("CPU busy percentage(5min):");
                        resultSB.append("CPU busy percentage(5min):").append("\n");
                        currentCpuPercentage.set(entry.getValue());
                        break;
                    case ".1.3.6.1.4.1.9.2.1.57":
                        System.out.println("CPU busy percentage(1min):");
                        resultSB.append("CPU busy percentage(5min):").append("\n");
                        oneMinCpuPercentage.set(entry.getValue());
                        break;
                    case ".1.3.6.1.4.1.9.2.1.56":
                        System.out.println("CPU busy percentage(current):");
                        resultSB.append("CPU busy percentage(current):").append("\n");
                        fiveMinCpuPercentage.set(entry.getValue());
                        break;

                    // Weitere Oids die gemacht werden können --> müssen weiter oben auch geaddet werden
                    //.1.3.6.1.4.1.9.2.1.58 "5 minute exponentially-decayed moving average of the CPU busy percentage."
                    //.1.3.6.1.4.1.9.2.1.57 "1 minute exponentially-decayed moving average of the CPU busy percentage."
                    //.1.3.6.1.4.1.9.2.1.56 "CPU busy percentage in the last 5 second period. Not the last 5 realtime seconds but the last 5 second period in the scheduler."
                    //.1.3.6.1.4.1.9.2.4.6 "Total bytes of lost IP packets."
                    //.1.3.6.1.4.1.9.2.4.5 "Lost IP packets due to memory limitations."
                }

                // Ausgabe der Daten
                System.out.println(entry.getValue());
                resultSB.append(entry.getValue()).append("\n");
            }

            // Leerzeile nach den Einträgen
            System.out.println("");
            resultSB.append("\n");

            resultString = resultSB.toString();
        }
        // }
        return resultString;
    }

    // Datenabfrage
    public static Map<String, String> doWalk(String tableOid, Target target) throws IOException
    {
        Map<String, String> result = new TreeMap<>();
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(tableOid));

        if (events == null || events.size() == 0)
        {
            System.out.println("Error: Unable to read table...");
            return result;
        }

        for (TreeEvent event : events)
        {
            if (event == null)
            {
                continue;
            }

            if (event.isError())
            {
                System.out.println("Error: table OID [" + tableOid + "] " + event.getErrorMessage());
                continue;
            }

            VariableBinding[] varBindings = event.getVariableBindings();

            if (varBindings == null || varBindings.length == 0)
            {
                continue;
            }

            for (VariableBinding varBinding : varBindings)
            {
                if (varBinding == null)
                {
                    continue;
                }

                result.put("." + varBinding.getOid().toString(), varBinding.getVariable().toString());
            }

        }

        // Kommunikation beenden
        snmp.close();

        return result;
    }

    public static void main(String[] args) throws Exception
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/GUI.fxml"));
        primaryStage.setTitle("SNMP Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
