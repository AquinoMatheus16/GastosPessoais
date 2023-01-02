select
	*
from
	public.titulo
where
	data_vencimento between to_timestamp('2022-01-01 00:00:00', 'yyyy-MM-DD hh24:MI:ss') and to_timestamp('2024-01-30 23:59:59', 'yyyy-MM-DD hh24:MI:ss')
	