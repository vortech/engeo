create index geo_index on geo (geo asc);
create index eng_index on eng (eng asc);
create index geo_id_index on geo (geo_id asc);
create index eng_id_index on eng (eng_id asc);
create index geo_eng_geo_id on geo_eng (geo_id asc);
create index gen_eng_eng_id on geo_eng (eng_id asc);
create index types_id on types (id asc);
