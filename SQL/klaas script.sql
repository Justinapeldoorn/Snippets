
/////////////////// Patronenen met activiteiten die fout gaan\\\\\\\\\\\\\\\\\
select s.code as Patroon, act.drglptnaam, act.activiteitsoort_code as ACTIVITEIT_SOORT, TO_CHAR(act.plantijd, 'HH24:MI:ss') as PLANTIJD, TO_CHAR(act.berekendeplantijd, 'HH24:MI:ss') BEREKENDE_TIJD from bewegingsactiviteit act
inner join BEWEGING bew on act.beweging_id = bew.id
inner join dipelementref der on der.TECHNISCHESLEUTEL_ID=bew.id
inner join BEWEGINGSLEUTEL s on s.id = der.LOGISCHESLEUTEL_ID
inner join planversie pv on pv.DRGLINFRAPLAN_ID=der.DRGLINFRAPLAN_ID
where pv.logischeplanversie_id = 5863290499 and MOD(TO_CHAR(act.plantijd, 'ss'), 6)!=0
order by s.code;

/////////////////// aantal activiteitsregels per patroon\\\\\\\\\\\\\\\\\
select s.code as Patroon, count(act.id) as AANTAL_FOUTE_ACTIVITEITEN from bewegingsactiviteit act
inner join BEWEGING bew on act.beweging_id = bew.id
inner join dipelementref der on der.TECHNISCHESLEUTEL_ID=bew.id
inner join BEWEGINGSLEUTEL s on s.id = der.LOGISCHESLEUTEL_ID
inner join planversie pv on pv.DRGLINFRAPLAN_ID=der.DRGLINFRAPLAN_ID
where pv.logischeplanversie_id = 5863290499 and MOD(TO_CHAR(act.plantijd, 'ss'), 6)!=0
group by s.code;

/////////////////// count van aantal foute patronen en aantal foute activiteiten \\\\\\\\\\\\\\\\\
select count( distinct s.code) as PATRONEN , count(act.id) as ACTIVITEITEN from bewegingsactiviteit act
inner join BEWEGING bew on act.beweging_id = bew.id
inner join dipelementref der on der.TECHNISCHESLEUTEL_ID=bew.id
inner join BEWEGINGSLEUTEL s on s.id = der.LOGISCHESLEUTEL_ID
inner join planversie pv on pv.DRGLINFRAPLAN_ID=der.DRGLINFRAPLAN_ID
where pv.logischeplanversie_id = 5863290499 and MOD(TO_CHAR(act.plantijd, 'ss'), 6)!=0;

/////////////////// count van alle patronen en alle activiteiten \\\\\\\\\\\\\\\\\
select count(distinct s.code) as PATRONEN, count(act.id) as ACTIVITEITEN from bewegingsactiviteit act
inner join BEWEGING bew on act.beweging_id = bew.id
inner join dipelementref der on der.TECHNISCHESLEUTEL_ID=bew.id
inner join BEWEGINGSLEUTEL s on s.id = der.LOGISCHESLEUTEL_ID
inner join planversie pv on pv.DRGLINFRAPLAN_ID=der.DRGLINFRAPLAN_ID
where pv.logischeplanversie_id = 5863290499
order by s.code;