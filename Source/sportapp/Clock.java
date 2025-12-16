package sportapp;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton clock used by the application to provide current date/time and to
 * schedule periodic background tasks (e.g. booking status updates).
 */
public class Clock {
  private static Clock instance;
  private LocalDateTime currentDateTime;
  // private LocalTime currentTime;
  private LocalDateTime nextHour;
  private Timer timer;

  private Clock() {
    currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"));
    // currentTime = LocalTime.now(ZoneId.of("Asia/Hong_Kong"));
    nextHour = currentDateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
    
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        EquipmentBookingControl equipmentBookingControl = new EquipmentBookingControl();
        FacilityBookingControl facilityBookingControl = new FacilityBookingControl();

        equipmentBookingControl.updateBookingStatus();
        facilityBookingControl.updateBookingStatus();
      }
    };
    
    timer = new Timer(true);
    timer.scheduleAtFixedRate(timerTask, Duration.between(currentDateTime, nextHour).toMillis(), (60 * 60 * 1000));
  }

  /**
   * Returns the singleton Clock instance.
   * @return Clock instance
   */
  public static Clock getInstance() {
    if (instance == null) { instance = new Clock(); }
    return instance;
  }

  /**
   * Returns today's date according to the application's timezone.
   * @return current date
   */
  public LocalDate getToday() {
    return this.currentDateTime.toLocalDate();
  }

  /**
   * Returns the current time truncated to seconds.
   * @return current local time
   */
  public LocalTime getNow() {
    return this.currentDateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
  }

  /**
   * Returns the current hour-of-day.
   * @return hour (0-23)
   */
  public int getHour() {
    return this.currentDateTime.getHour();
  }
}