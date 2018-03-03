var SERVER_URL = $(location).attr('origin');
var CURRENT_GAME;

$(document).ready(function() {
    updateGames();
    updateUser();
    runGameUpdatePuller();
});

function runGameUpdatePuller() {
    var t =setInterval(function(){tryReloadGame()}, 5000);
}

function tryReloadGame() {
    updateGames();
    if(CURRENT_GAME === undefined){
        return;
    }
    getGame(CURRENT_GAME.id);
}

function logout() {
    window.location.href = SERVER_URL + "/logout"
}
function updateUser() {
    $.get("user", function (name) {
        $('#user_name').text(name)
    });
}

function updateGames() {

    $.get("games", function (data) {
        $('#all_games > tbody').children().remove();
        data.forEach(function (game) {
            var firstColumn = "";
            if(game.user2 == null && !($('#user_name').text() === game.user1)){
                firstColumn = "<button onclick=\"joinToGame('" + game.id + "')\">Join</button>";
            }else if (game.user2 != null){
                firstColumn = "<button onclick=\"getGame('" + game.id + "')\">Play</button>";
            }
            var tableRow = "<tr>" +
                           "<td align='center'>" + firstColumn + "</td>" +
                           "<td align='center'>" + game.status + "</td>" +
                           "<td align='center'>" + game.user1 + "</td>" +
                           "<td align='center'>" + game.user2 + "</td>" +
                           "</tr>";
            $('#all_games > tbody:last-child').append(tableRow);
        })
    });
}

function joinToGame(gameId) {
    $.get("game/" + gameId + "/join", function(game){
        CURRENT_GAME = game;
        drowGame(game);
        updateGames();
    });
}

function createGame() {
    $.get("create", function(newGame){
        CURRENT_GAME = newGame;
        updateGames();
        drowGame(newGame);
    })
}

function getGame(gameId) {
    $.get("game/" + gameId, function (game) {
        CURRENT_GAME = game;
        drowGame(game)
    })
}

function drowGame(game) {
    var gameBlock = $("#game_block");
    gameBlock.removeAttr("hidden");
    var gameState = game.state;
    var isCurrentUserOwner = $('#user_name').text() === game.owner;
    var currentUserSymbol = isCurrentUserOwner ? 'X' : 'O';
    var secondPlayer = isCurrentUserOwner ? game.player : game.owner;
    $("#game_name").text("You("+ currentUserSymbol +") play against " + secondPlayer);
    $("#game_status").text(game.status);
    for (i=0; i<=2; i++){
        for (j=0; j<=2; j++){
            var cellId = "#cell" + i + j;
            var cellValue = gameState[i][j];
            $(cellId).text(cellValue == null ? "" : cellValue);
        }
    }
}

function onFieldClick(x, y){
    var canPlay = $('#user_name').text() !== CURRENT_GAME.prevPlayer
        && CURRENT_GAME.status === "IN_PROGRESS";
    if(!canPlay){
        return;
    }
    var gameId = CURRENT_GAME.id;
    $.get("game/update/" + gameId + "?x=" + x + "&&y=" + y, function (game) {
        CURRENT_GAME = game;
        drowGame(game)
    });
}