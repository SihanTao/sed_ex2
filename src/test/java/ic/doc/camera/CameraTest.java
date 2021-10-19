package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class CameraTest {

  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    Camera camera = new Camera(sensor, memoryCard);

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

    Camera camera = new Camera(sensor, memoryCard);

    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerDown();
          }
        });

    camera.powerOff();
  }

  @Test
  public void pressingTheShutterWhenPowerOffDoesNothing() {

      Camera camera = new Camera(sensor, memoryCard);

      context.checking(new Expectations() {{
          exactly(1).of(sensor).powerDown();
      }});

      camera.powerOff();
      camera.pressShutter();
  }

    @Test
    public void pressingTheShutterWhenPowerOnCopiesData() {
        Camera camera = new Camera(sensor, memoryCard);

        byte[] data = "Data from Camera Sensor".getBytes(StandardCharsets.UTF_8);

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);
        }});

        camera.powerOn();
        camera.pressShutter();
    }
}
