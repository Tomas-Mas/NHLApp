document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
	}
}

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
	
	document.getElementById('mainFinishedTable').onclick = function(e) {
		if(e.target.className === 'gameDetailPlayerName') {
			playerClicked(e.target);
		} else if (e.target.className === 'gamePageButton') {
			gamePageButtonClicked(e.target);
		} else if (e.target.closest('tr') !== null && e.target.closest('tr').className === 'clickableTr') {
			mainTableHeaderClicked(e.target.closest('tr'));
		}
	}
	
	document.getElementById('season').removeAttribute('disabled');
}

function playerClicked(target) {
	var playerId = target.id;
	alert(playerId);
	//TODO redirect to player page
}

function gamePageButtonClicked(target) {
	var gameId = target.id;
	alert(gameId);
	//TODO redirect to game page
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