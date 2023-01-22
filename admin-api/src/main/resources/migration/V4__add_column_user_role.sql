alter table tsp_admin add column role varchar(255) DEFAULT NULL;

update tsp_admin set role = 'ADMIN';