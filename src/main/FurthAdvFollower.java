package main;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Stopwatch;

public class FurthAdvFollower {
	static float kp;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		kp = 0.6f;
		int speed0 = 250;
		float correction = 2f * speed0;
		Motor.A.setSpeed(speed0);
		Motor.B.setSpeed(speed0);
		LightSensor ls = new LightSensor(SensorPort.S4);
		ls.setFloodlight(true);
		System.out.println("Set on white and press Button");
		Button.waitForAnyPress();
		int max = ls.getLightValue(); // white matt
		System.out.println(max);
		System.out.println(ls.getLightValue());
		System.out.println("Set on line and press Button");
		Button.waitForAnyPress();
		int min = ls.getLightValue(); // black line
		System.out.println(min);
		System.out.println("Press Button to run");
		Button.waitForAnyPress();
		System.out.flush();
		forward();
		float midpoint = 0.5f; // 
		float lightNow;
		int buttonPressed = 0;
		int firstCorner = 0;
		Stopwatch s = new Stopwatch();
		int count = 0;
		for(;;)//ever
		{
			s.reset();
			if(Button.readButtons() == Button.ID_ESCAPE) System.exit(0);
			lightNow = (float )ls.getLightValue();
			if (lightNow > min * 1.05) s.reset();
			float err = midpoint - ((lightNow - min) / (max - min));
			Motor.A.setSpeed(speed0 + kp * (err * correction)); // norm to 0 - 1
			Motor.B.setSpeed(speed0 - kp * (err * correction));
//			if (firstCorner == 0 && s.elapsed() > 500)
//			{
//				firstCorner++;
//				kp *= -1f;
//				Motor.B.setSpeed(speed0);
//				Motor.A.setSpeed(0);
//				forward();
//				while(ls.getLightValue() < max * 0.9);
//				while(ls.getLightValue() > min * 1.1);
//				
//			}
			
			if (Button.readButtons() == Button.ID_ENTER)
			{
				if (buttonPressed == 0) kp *= -1f;
				buttonPressed = buttonPressed == 0 ? 1 : buttonPressed;
			}
			else if (Button.readButtons() == Button.ID_LEFT)
			{
				if (buttonPressed == 0) {
					kp -= kp >= 0 ? 0.1f : -0.1f;
					System.out.println("KP: " + kp);
				}
				buttonPressed = buttonPressed == 0 ? 1 : buttonPressed;
				
			}
			else if (Button.readButtons() == Button.ID_RIGHT)
			{
				if (buttonPressed == 0){
					kp += kp >= 0 ? 0.1f : -0.1f;
					System.out.println("KP: " + kp);
				}
				buttonPressed = buttonPressed == 0 ? 1 : buttonPressed;
			}
			else if (Button.readButtons() == 0)
			{
				buttonPressed = buttonPressed == 1 ? 0 : buttonPressed;
			}
			forward();
			++count;
			while(s.elapsed() < 10);
		}
	}

	static void forward()
	{
		Motor.A.forward();
		Motor.B.forward();
	}
}
