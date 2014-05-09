//AARON AND CORBIN

import lejos.nxt.*;

public class SensorTest {
	public static void main(String[] args) {
		LightSensor sensor = new LightSensor(SensorPort.S1);
		// sensor.setFloodlight(!sensor.isFloodlightOn());
		while (true) {
			System.out.print(sensor.getLightValue()+ " 0" + " 0\n" );
			System.out.print(sensor.getNormalizedLightValue()+ " 0" + " 1\n" );
			System.out.print(sensor.readNormalizedValue()+ " 0" + " 2\n" );
			System.out.print(sensor.readValue()+ " 0" + " 3\n\n\n" );

			try {
    			Thread.sleep(1000);
			} catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
			}

			// if (Button.ENTER.isDown()) {
			// 	sensor.setFloodlight(!sensor.isFloodlightOn());

			// 	while (Button.ENTER.isDown()) {
			// 		Button.waitForAnyEvent(1000000);
			// 	}
			// }
		}
	}
}