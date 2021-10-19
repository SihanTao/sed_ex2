package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class CameraTest {

  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  Camera camera = new Camera(sensor, memoryCard);
  byte[] data = "Data from Camera Sensor".getBytes(StandardCharsets.UTF_8);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
          }
        });

    camera.powerOn();
  }

  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).powerDown();
          }
        });

    camera.powerOn();
    camera.powerOff();
  }

  @Test
  public void pressingTheShutterWhenPowerOffDoesNothing() {
    context.checking(
        new Expectations() {
          {
            // do nothing
          }
        });

    camera.pressShutter();
  }

  @Test
  public void pressingTheShutterWhenPowerOnCopiesData() {

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);
          }
        });

    camera.powerOn();
    camera.pressShutter();
  }

  @Test
  public void switchingCameraOffDoesNotPowerDownSensorWhenWritingData() {
    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);
            never(sensor).powerDown();
          }
        });

    camera.powerOn();
    camera.pressShutter();
    camera.powerOff();
  }

  @Test
  public void writingDataCompletedSwitchingOffCameraPowersDownSensor() {
    byte[] data = "Data from Camera Sensor".getBytes(StandardCharsets.UTF_8);

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);

            exactly(1).of(sensor).powerDown();
          }
        });

    camera.powerOn();
    camera.pressShutter();
    camera.writeComplete();
    camera.powerOff();
  }

  @Test
  public void onceWritingDataCompletedSwitchingOffCameraPowersDownSensor() {
    byte[] data = "Data from Camera Sensor".getBytes(StandardCharsets.UTF_8);

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);
            // isWriting, power off do nothing
            // until write complete
            // exactly(1).of(camera).writeComplete();
            // never(sensor).powerDown();
            // exactly(1).of(camera).writeComplete();
            exactly(1).of(sensor).powerDown();
          }
        });

    camera.powerOn();
    camera.pressShutter();
    camera.powerOff();
    camera.writeComplete();
  }
}
