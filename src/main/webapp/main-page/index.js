document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
	}
}

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
	
	document.getElementById('mainFinishedTableRegulation').onclick = function(e) {
		if(e.target.className === 'gameDetailPlayerName') {
			playerClicked(e.target);
		} else if (e.target.className === 'gamePageButton') {
			gamePageButtonClicked(e.target);
		} else if (e.target.closest('tr') !== null && e.target.closest('tr').className === 'clickableTr') {
			mainTableHeaderClicked(e.target.closest('tr'));
		} else if (e.target.closest('table') !== null && e.target.closest('table').className === 'resultsHeader') {
			mainTableHeaderClicked(e.target.closest('table').closest('tr'));
		}
	}
	
	document.getElementById('regulationStats').onclick = function(e) {
		if(e.target.className === 'teamName') {
			teamClicked(e.target);
		}
	}
	
	document.getElementById('season').removeAttribute('disabled');
}

function playerClicked(target) {
	var playerId = target.id;
	window.location.href = 'player.jsp?id=' + playerId;
}

function teamClicked(target) {
	var teamId = target.id;
	alert(teamId);
	//window.location.href = 'team.jsp?id=' + teamId;
}

function gamePageButtonClicked(target) {
	var gameId = target.id;
	window.location.href = 'game.jsp?id=' + gameId;
}

function mainTableHeaderClicked(trEl) {
	var rowId = getIdFromElement(trEl, true, 'headerRow');
	var subRow = document.getElementById('dataRow' + rowId);
	if(subRow.style.display === 'none') {
		subRow.style.display = 'block';
	} else {
		subRow.style.display = 'none';
	}
}

function getIdFromElement(element, prefix, idPrefix) {
	if(prefix) 
		return element.id.replace(idPrefix, '');
	else
		return element.id;
}