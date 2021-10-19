package ic.doc.camera;

public class Camera implements WriteListener {

  private Boolean isOn;
  private Sensor sensor;
  private MemoryCard memoryCard;
  private Boolean isWriting;

  public Camera(Sensor sensor, MemoryCard memoryCard) {
    this.isOn = false;
    this.sensor = sensor;
    this.memoryCard = memoryCard;
    this.isWriting = false;
  }

  public void pressShutter() {
    if (isOn) {
      isWriting = true;
      memoryCard.write(sensor.readData());
    }
  }

  public void powerOn() {
    isOn = true;
    sensor.powerUp();
  }

  public void powerOff() {
    isOn = false;
    if (isWriting)
      return;
    sensor.powerDown();
  }

  @Override
  public void writeComplete() {
    isWriting = false;
  }
}
