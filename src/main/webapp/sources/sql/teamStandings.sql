select myTeams.team, t.name, t.abbreviation, t.t_id as id, divisionConference.conference as conference, divisionConference.division as division, 
        gamesPlayed, goalsFor, goalsAgainst, points, regulationWins, regulationLoses, overtimeWins, overtimeLoses, shootoutWins, shootoutLoses
from (
    select season, gameType, team, teamId, count(g_id) as gamesPlayed, sum(goalsFor) as goalsFor, sum(goalsAgainst) as goalsAgainst, sum(points) as points,
            sum(case when periods <= 3 and goalsFor > goalsAgainst then 1 else 0 end) as regulationWins,
            sum(case when periods <= 3 and goalsFor < goalsAgainst then 1 else 0 end) as regulationLoses,
            sum(case when periods = 4 and goalsFor > goalsAgainst then 1 else 0 end) as overtimeWins,
            sum(case when periods = 4 and goalsFor < goalsAgainst then 1 else 0 end) as overtimeLoses,
            sum(case when periods = 5 and goalsFor > goalsAgainst then 1 else 0 end) as shootoutWins,
            sum(case when periods = 5 and goalsFor < goalsAgainst then 1 else 0 end) as shootoutLoses
    from (
        select *
        from (
            select g.season, g.g_id, g.gameType, homeT.t_id as homeTeam, awayT.t_id as awayTeam, myGames.periods, g.homeScore, g.awayScore,
                    case 
                        when homeScore > awayScore then 2
                        when homeScore < awayScore and periods <=3 then 0 else 1
                    end as homePoints,
                    case 
                        when awayScore > homeScore then 2
                        when awayScore < homeScore and periods <=3 then 0 else 1
                    end as awayPoints
            from (
                    select g.g_id, max(gEv.periodNumber) as periods
                    from games g inner join gameEvents gev on g.g_id = gev.gameId 
                    group by g.season, g.g_id
                ) myGames 
                inner join Games g on g.g_id = myGames.g_id
                inner join Teams homeT on homeT.t_id = g.homeTeamId
                inner join Teams awayT on awayT.t_id = g.awayTeamId
            where g.gameType = 'R' and g.season = ?
            order by g.g_id
            )
        unpivot (
            (teamId, goalsFor, goalsAgainst, points)
            for team
            in (
                (homeTeam, homeScore, awayScore, homePoints) as 'home',
                (awayTeam, awayScore, homeScore, awayPoints) as 'away'
            )
        )
    )
    group by season, gameType, team, teamId
) myTeams inner join Teams t on t.t_id = myTeams.teamId
    inner join (
        select t.t_id as teamId, conf.name as conference, div.name as division
        from teams t inner join division_teams dt on t.t_id = dt.team_id
            inner join divisions div on div.d_id = dt.division_id
            inner join conference_teams ct on t.t_id = ct.team_id
            inner join conferences conf on conf.c_id = ct.conference_id
        where dt.season = ? and ct.season = ?
    ) divisionConference on t.t_id = divisionConference.teamId
order by name
        