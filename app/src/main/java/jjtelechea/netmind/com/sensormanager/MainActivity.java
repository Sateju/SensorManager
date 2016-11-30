package jjtelechea.netmind.com.sensormanager;

import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG_MAIN_ACTIVITY = "In-MainActivity";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private RelativeLayout mRelativeLayout;
    private long lastSensorUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutMain);

        this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> myDeviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        
        String mSensorString = "";
        for (Sensor aSensor: myDeviceSensors) {
            mSensorString += aSensor.getName() + "\n";
        }

        Toast.makeText(this,mSensorString,Toast.LENGTH_LONG).show();
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.lastSensorUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long eventTime = sensorEvent.timestamp;
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            float[] accSensorValues = sensorEvent.values; //Getting values for all three axis
            float x = accSensorValues[0]; float y = accSensorValues[1]; float z = accSensorValues[2];
            //Getting the acceleration module power 2 over G
            float accTimesG = (x*x + y*y + z*z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            if (accTimesG > 5.0 && (eventTime - this.lastSensorUpdate) > 1000000000)
            {
                this.lastSensorUpdate =  eventTime;

                Log.i(MainActivity.TAG_MAIN_ACTIVITY, "Device was shuffled");
                Toast.makeText(this, "Device was shuffled", Toast.LENGTH_SHORT).show();

                if (this.mRelativeLayout.getBackground() instanceof ColorDrawable)
                {
                    if (((ColorDrawable) this.mRelativeLayout.getBackground()).getColor() == ContextCompat.getColor(this, R.color.lightBlue))
                        this.mRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightRed));
                    else
                        this.mRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this,this.mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this,this.mAccelerometer);
    }
}
