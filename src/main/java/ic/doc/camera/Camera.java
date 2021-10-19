package ic.doc.camera;

public class Camera implements WriteListener {

  private Boolean isOn;
  private final Sensor sensor;
  private final MemoryCard memoryCard;
  private Boolean isWriting;
  private Boolean willOff;
  private static final Boolean DEFAULT_POWER_STATE = false;

  public Camera(Sensor sensor, MemoryCard memoryCard) {
    this.isOn = DEFAULT_POWER_STATE;
    this.sensor = sensor;
    this.memoryCard = memoryCard;
    this.isWriting = false;
    this.willOff = false;
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
    if (isWriting) {
      willOff = true;
      return;
    }
    sensor.powerDown();
  }

  @Override
  public void writeComplete() {

    isWriting = false;
    if (willOff) {
      sensor.powerDown();
    }
  }
}
