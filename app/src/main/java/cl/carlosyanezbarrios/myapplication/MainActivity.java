package cl.carlosyanezbarrios.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient cliente;
    private final MemoryPersistence pers = new MemoryPersistence();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MqttAndroidClient clientedemqtt = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.emqx.io:1883", "Carloslechuga", persistencia);
        mqtt.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message Arrived!: " + topic + ": " + new String(message.getPayload()));
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery Complete!");
            }
        });
        MqttConnectOptions opcionesmqtt = new MqttConnectOptions();
        mqtto.setCleanSession(true);
        try {
            clientemqtt.connect(opcionesmqtt, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Conexion Exitosa");
                    try {
                        System.out.println("Subscribing to Verduleria/frutas");
                        clientemqtt.subscribe("Verduleria/frutas", 0);
                        System.out.println("Te suscribiste a Verduleria/frutas");
                        System.out.println("Enviando mensaje a topico");
                        clientemqtt.publish("Verduleria/frutas", new MqttMessage("Acá estan las frutas".getBytes()));
                    }catch (MqttException ex) {
                        System.out.println(ex.toString());
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("La conexión no se establecio");
                    System.out.println("throwable: " + exception.toString());
                }
            });
        } catch (MqttException ex) {
            System.out.println(ex.toString());
        }
    }

}