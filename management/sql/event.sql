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

delimiter $$
create event mysql.daily_deviceStatus_event
on schedule
every 1 day
STARTS '2018-03-28 00:00:00'
do
BEGIN
insert into airdevice.air_device_day (
select deviceStatus.device_id as chip_id,
avg(deviceStatus.pm25) as pm25,
date(now()) as date,
0 as block_flag,
now() as create_time
from airdevice.deviceStatus
where deviceStatus.time > date_sub(now(), interval 24 hour)
and deviceStatus.power = 1
group by deviceStatus.device_id
);
END $$