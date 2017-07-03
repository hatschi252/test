package main;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class WallFollower {
	static float kp;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		kp = 0.5f;
		int speed0 = 200;
		float correction = 2f * speed0;
		Motor.A.setSpeed(speed0);
		Motor.B.setSpeed(speed0);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		LightSensor ls = new LightSensor(SensorPort.S4);
		ls.setFloodlight(true);
		System.out.println("Set on white and press Button");
		Button.waitForAnyPress();
		int max = us.getDistance();
		int white = ls.getLightValue(); // here max value
		System.out.println(white);
		System.out.println(ls.getLightValue());
		System.out.println("Set on line and press Button");
		Button.waitForAnyPress();
		int min = us.getDistance();
		int black = ls.getLightValue(); // here min value
		System.out.println(black);
		System.out.println("Press Button to run");
		Button.waitForAnyPress();
		forward();
		float midpoint = 0.5f; //
		float lightNow;
		float distNow;
		for (;;)// ever
		{
			lightNow = (float) ls.getLightValue();
			distNow = (float) us.getDistance();
			distNow = distNow > max ? max : distNow;
			distNow = distNow < min ? min : distNow;
			if (true) {
				float err = midpoint - ((distNow - min) / (max - min));
				System.out.println(err);
				Motor.A.setSpeed(speed0 - kp * (err * correction)); // norm to 0
																	// - 1
				Motor.B.setSpeed(speed0 + kp * (err * correction));
			}
			else
			{
				Motor.A.setSpeed(speed0 * 2);
				Motor.B.setSpeed(0);
			}
			forward();
		}
	}

	static void forward() {
		Motor.A.forward();
		Motor.B.forward();
	}
}
