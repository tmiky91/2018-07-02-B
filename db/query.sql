select f1.ORIGIN_AIRPORT_ID as id1, f1.DESTINATION_AIRPORT_ID as id2, avg(f1.ELAPSED_TIME) as media
from flights as f1
where f1.ORIGIN_AIRPORT_ID != f1.DESTINATION_AIRPORT_ID
group by id1, id2
having count(*)>=50