// ALL AARON

import lejos.nxt.*;

public class SensorTest {
	public static void main(String[] args) {
		LightSensor sensor = new LightSensor(SensorPort.S1);
		while (true) {
			LCD.drawInt(sensor.getLightValue(), 0, 0);
			LCD.drawInt(sensor.getNormalizedLightValue(), 0, 1);
			LCD.drawInt(sensor.readNormalizedValue(), 0, 2);
			LCD.drawInt(sensor.readValue(), 0, 3);

			if (Button.ENTER.isDown()) {
				sensor.setFloodlight(!sensor.isFloodlightOn());

				while (Button.ENTER.isDown()) {
					Button.waitForAnyEvent(1000000);
				}
			}
		}
	}
}