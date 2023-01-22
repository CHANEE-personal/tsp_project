alter table tsp_notice add column `top_fixed` BOOLEAN DEFAULT FALSE;
alter table tsp_notice add index `top_fixed_index` (`top_fixed`);