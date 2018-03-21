delimiter |

CREATE EVENT city_aqi_event
    ON SCHEDULE
      EVERY 7  DAY
    DO
      BEGIN
        insert into airdevice.city_aqi_full select * from city_aqi where city_aqi.time < date_sub(now(), interval 7 day);
        delete from airdevice.city_aqi where city_aqi.time < date_sub(now(), interval 7 day);
      END |

delimiter ;


delimiter |

CREATE EVENT device_status_event
    ON SCHEDULE
      EVERY 7  DAY
    DO
      BEGIN
        insert into airdevice.deviceStatus_full select * from deviceStatus where deviceStatus.time < date_sub(now(), interval 7 day);
        delete from airdevice.deviceStatus where deviceStatus.time < date_sub(now(), interval 7 day);
      END |

delimiter ;