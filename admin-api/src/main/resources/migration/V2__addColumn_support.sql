alter table tsp_support add column create_time datetime(6) DEFAULT NULL;
alter table tsp_support add column creator int(11) DEFAULT NULL;
alter table tsp_support add column update_time datetime(6) DEFAULT NULL;
alter table tsp_support add column updater int(11) DEFAULT NULL;