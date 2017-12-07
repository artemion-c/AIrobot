package pack;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class ColorSensor extends AbstractFilter{
	float[] sample;

	public ColorSensor(SampleProvider source) {
		super(source);
		sample = new float[sampleSize];
	}

	public byte color() {
		super.fetchSample(sample,  0);
		if(sample[0] == 0) {
			return 0;
		}else {
			return 1;
		}
	}

}
