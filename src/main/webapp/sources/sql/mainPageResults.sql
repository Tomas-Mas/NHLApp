select myevents.*, p.p_id, p.firstName, p.lastName, ep.role, t.name as actorTeamName
from (
    select mygames.*, e.name, ge.ge_id, ge.gameEventId, ge.periodNumber, score.homePeriodScore homePeriodScore, score.awayPeriodScore awayPeriodScore, 
        ge.periodTime, e.strength, e.emptyNet, e.secondaryType, e.penaltySeverity, e.penaltyMinutes
    from (
        (select g.g_id, g.gameType, g.season, g.gameDate as sortableDate,
                to_char(to_timestamp_tz(to_char(g.gameDate, 'yyyy-mm-dd hh24:mi:ss"Z"'), 'yyyy-mm-dd hh24:mi:ss TZH;TZM') at time zone 'Europe/Prague', 'dd.mm.yy hh24:mi') as gameDate,
                awayTeam.t_id as awayTeamId, awayTeam.name as awayTeamName, g.awayScore, homeTeam.t_id as homeTeamId, homeTeam.name as homeTeamName, g.homeScore, v.name as venueName, s.name as statusName
        from Games g inner join GameStatus s on s.code = g.gameStatus 
            inner join Teams awayTeam on awayTeam.t_id = g.awayTeamId 
            inner join Teams homeTeam on homeTeam.t_id = g.homeTeamId
            inner join Venues v on v.v_id = g.venueId
        where g.season = ? and gameType = 'R' or gameType = 'P'
        order by g.gamedate desc
        fetch first 20 rows only)
    ) mygames
    left join GameEvents ge on ge.gameId = mygames.g_id
    left join Events e on e.e_id = ge.eventId
    left join ( 
        select periodScore.g_id, periodScore.periodNumber, count(case periodScore.scoringTeam when periodScore.homeTeam then 1 end) as homePeriodScore, 
                count(case periodScore.scoringTeam when periodScore.awayTeam then 1 end) as awayPeriodScore
        from (
            select g.g_id, ge.periodNumber, homeTeam.name as homeTeam, awayTeam.name as awayTeam, ge.ge_id, e.name, ep.role, r.t_id, scoringTeam.name as scoringTeam
            from games g inner join gameEvents ge on g.g_id = ge.gameId
                inner join Events e on e.e_id = ge.eventId
                inner join Teams homeTeam on homeTeam.t_id = g.homeTeamId
                inner join Teams awayTeam on awayTeam.t_id = g.awayTeamId
                inner join EventPlayers ep on ge.ge_id = ep.event_id
                inner join Rosters r on r.r_id = ep.roster_id
                inner join Teams scoringTeam on scoringTeam.t_id = r.t_id
            where e.name = 'Goal' and ep.role='Scorer'
            order by g.g_id, ge.periodNumber
        ) periodScore
        group by periodScore.g_id, periodScore.periodNumber
        order by periodScore.g_id, periodScore.periodNumber
    ) score on score.g_id = mygames.g_id and score.periodNumber = ge.periodNumber
    where e.name = 'Goal' or e.name = 'Penalty' or e.name is null
) myevents
    left join EventPlayers ep on ep.event_id = myevents.ge_id
    left join Rosters r on r.r_id = ep.roster_id
    left join Players p on p.p_id = r.p_id
    left join Teams t on t.t_id = r.t_id
order by myevents.sortableDate desc, myevents.g_id desc, myevents.gameEventId, ep.role
