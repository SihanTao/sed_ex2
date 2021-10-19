package ic.doc.camera;

public class Camera {

  private Boolean isOn;
  private Sensor sensor;
  private MemoryCard memoryCard;

  public Camera(Sensor sensor, MemoryCard memoryCard) {
    this.isOn = false;
    this.sensor = sensor;
    this.memoryCard = memoryCard;
  }

  public void pressShutter() {
    if (isOn) {
      memoryCard.write(sensor.readData());
    }
  }

  public void powerOn() {
    isOn = true;
    sensor.powerUp();
  }

  public void powerOff() {
    isOn = false;
    sensor.powerDown();
  }
}
