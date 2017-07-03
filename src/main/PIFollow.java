package main;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Stopwatch;
import lejos.util.Timer;

public class PIFollow {
	static float kp;
	static float ki;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		kp = 2f;
		ki = 0f;
		int speed0 = 150;
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
		int sLast = s.elapsed();
		float errLast = 0;
		float integral = 0;
		
		for(;;)//ever
		{
			s.reset();
			if(Button.readButtons() == Button.ID_ESCAPE) System.exit(0);
			lightNow = (float )ls.getLightValue();
			if (lightNow > min * 1.05) s.reset();
			float err = (midpoint - ((lightNow - min) / (max - min)));
			integral = integral * 0.5f + err;
			errLast = err;
			sLast = s.elapsed();
			Motor.A.setSpeed(speed0 + correction * (kp * err + ki * integral)); // norm to 0 - 1
			Motor.B.setSpeed(speed0 - correction * (kp * err + ki * integral));
			
			if (Button.readButtons() == Button.ID_ENTER)
			{
				if (buttonPressed == 0) integral = 0;
				buttonPressed = buttonPressed == 0 ? 1 : buttonPressed;
			}
			else if (Button.readButtons() == Button.ID_LEFT)
			{
				if (buttonPressed == 0) {
					ki -= ki >= 0 ? 0.1f : -0.1f;
					System.out.println("KP: " + ki);
				}
				buttonPressed = buttonPressed == 0 ? 1 : buttonPressed;
				
			}
			else if (Button.readButtons() == Button.ID_RIGHT)
			{
				if (buttonPressed == 0){
					ki += ki >= 0 ? 0.1f : -0.1f;
					System.out.println("KP: " + ki);
				}
				buttonPressed = buttonPressed == 0 ? 1 : buttonPressed;
			}
			else if (Button.readButtons() == 0)
			{
				buttonPressed = buttonPressed == 1 ? 0 : buttonPressed;
			}
			forward();
			while(s.elapsed() < 100);
		}
	}

	static void forward()
	{
		Motor.A.forward();
		Motor.B.forward();
	}

}
