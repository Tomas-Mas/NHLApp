select g.g_id as gameId, to_char(to_timestamp_tz(to_char(g.gameDate, 'yyyy-mm-dd hh24:mi:ss"Z"'), 'yyyy-mm-dd hh24:mi:ss TZH;TZM') at time zone 'Europe/Prague', 'dd.mm.yy hh24:mi') as gameDate, 
        g.gameType, homeT.t_id as homeTeamId, homeT.name as homeTeamName, homeT.abbreviation as homeAbbreviation, 
        awayT.t_id as awayTeamId, awayT.name as awayTeamName, awayT.abbreviation as awayAbbreviation, myGames.periods, g.homeScore, g.awayScore
from (
    select g.g_id, max(gEv.periodNumber) as periods
    from games g inner join gameEvents gev on g.g_id = gev.gameId 
    group by g.season, g.g_id
    ) myGames 
    inner join Games g on g.g_id = myGames.g_id
    inner join Teams homeT on homeT.t_id = g.homeTeamId
    inner join Teams awayT on awayT.t_id = g.awayTeamId
where g.gameType = 'P' and g.season = ?
order by g.gameDate
--fetch first 84 rows only --testing purpose