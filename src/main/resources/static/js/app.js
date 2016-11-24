function componentToHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function xyBriToHex(PHLightState) {
    if (PHLightState == null || PHLightState.reachable !== true) {
        return "";
    }

    var x = PHLightState.x;
    var y = PHLightState.y;
    var bri = PHLightState.brightness;
    var z = 1.0 - x - y;
    var Y = bri / 255.0; // Brightness of lamp
    var X = (Y / y) * x;
    var Z = (Y / y) * z;
    var r = X * 1.612 - Y * 0.203 - Z * 0.302;
    var g = -X * 0.509 + Y * 1.412 + Z * 0.066;
    var b = X * 0.026 - Y * 0.072 + Z * 0.962;
    r = r <= 0.0031308 ? 12.92 * r : (1.0 + 0.055) * Math.pow(r, (1.0 / 2.4)) - 0.055;
    g = g <= 0.0031308 ? 12.92 * g : (1.0 + 0.055) * Math.pow(g, (1.0 / 2.4)) - 0.055;
    b = b <= 0.0031308 ? 12.92 * b : (1.0 + 0.055) * Math.pow(b, (1.0 / 2.4)) - 0.055;
    var maxValue = Math.max(r, g, b);
    r /= maxValue;
    g /= maxValue;
    b /= maxValue;
    r = Math.round(r * 255);
    if (r < 0) {
        r = 255;
    }
    g = Math.round(g * 255);
    if (g < 0) {
        g = 255;
    }
    b = Math.round(b * 255);
    if (b < 0) {
        b = 255;
    }
    return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
}

function updateLights(data) {
    var PHLights = JSON.parse(data.body);
    document.getElementById("lights").innerHTML = String.raw`
<table class="mdl-cell mdl-cell--12-col mdl-data-table mdl-js-data-table mdl-shadow--2dp">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">Name</th>
            <th>Color</th>
            <th>Light Type</th>
        </tr>
    </thead>
    <tbody>
        ${PHLights.map(PHLight => String.raw`
            <tr class="light-reachable-${PHLight.lastKnownLightState.reachable}">
                <td class="mdl-data-table__cell--non-numeric">${PHLight.name}</td>
                <td style="background-color: ${xyBriToHex(PHLight.lastKnownLightState)};">${xyBriToHex(PHLight.lastKnownLightState)}</td>
                <td>${PHLight.lightType}</td>
            </tr>
        `).join("")}
    </tbody>
</table>
`;
}

function updateGroups(data) {
    var PHGroups = JSON.parse(data.body);
    document.getElementById("groups").innerHTML = String.raw`
<table class="mdl-cell mdl-cell--12-col mdl-data-table mdl-shadow--2dp">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">name</th>
            <th>type</th>
            <th>groupClass</th>
            <th>lightIdentifiers</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
        ${PHGroups.map(PHGroup => String.raw`
            <tr>
                <td class="mdl-data-table__cell--non-numeric">${PHGroup.name}</td>
                <td>${PHGroup.type}</td>
                <td>${PHGroup.groupClass}</td>
                <td>${PHGroup.lightIdentifiers}</td>
                <td>
                    <button class="mdl-button mdl-button--icon">
                        <i class="material-icons" data-identifier="${PHGroup.identifier}">delete</i>
                    </button>
                </td>
            </tr>
        `).join("")}
    </tbody>
</table>
`;
}

function updateScenes(data) {
    var PHScenes = JSON.parse(data.body);
    document.getElementById("scenes").innerHTML = String.raw`
<table class="mdl-cell mdl-cell--12-col mdl-data-table mdl-js-data-table mdl-shadow--2dp">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">name</th>
            <th>activeState</th>
            <th>transitionTime</th>
            <th>lightIdentifiers</th>
            <th>lightStates</th>
            <th>recycle</th>
            <th>locked</th>
            <th>appData</th>
            <th>picture</th>
            <th>lastupdated</th>
            <th>version</th>
        </tr>
    </thead>
    <tbody>
        ${PHScenes.map(PHScene => String.raw`
            <tr>
                <td class="mdl-data-table__cell--non-numeric">${PHScene.name}</td>
                <td>${PHScene.activeState}</td>
                <td>${PHScene.transitionTime}</td>
                <td>${PHScene.lightIdentifiers}</td>
                <td>${PHScene.lightStates}</td>
                <td>${PHScene.recycle}</td>
                <td>${PHScene.locked}</td>
                <td>${JSON.stringify(PHScene.appData)}</td>
                <td>${PHScene.picture}</td>
                <td>${PHScene.lastupdated}</td>
                <td>${PHScene.version}</td>
            </tr>
        `).join("")}
    </tbody>
</table>
`;
}

function updateSchedules(data) {
    var PHSchedules = JSON.parse(data.body);
    document.getElementById("schedules").innerHTML = String.raw`
<table class="mdl-cell mdl-cell--12-col mdl-data-table mdl-js-data-table mdl-shadow--2dp">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">name</th>
            <th>date</th>
            <th>lightIdentifier</th>
            <th>groupIdentifier</th>
            <th>sceneIdentifier</th>
            <th>lightState</th>
            <th>recurringTimerInterval</th>
            <th>created</th>
            <th>startTime</th>
            <th>hasLocalTime</th>
            <th>autodelete</th>
            <th>status</th>
            <th>timer</th>
            <th>randomTime</th>
            <th>recurringDays</th>
        </tr>
    </thead>
    <tbody>
        ${PHSchedules.map(PHSchedule => String.raw`
            <tr>
                <td class="mdl-data-table__cell--non-numeric">${PHSchedule.name}</td>
                <td>${PHSchedule.date}</td>
                <td>${PHSchedule.lightIdentifier}</td>
                <td>${PHSchedule.groupIdentifier}</td>
                <td>${PHSchedule.sceneIdentifier}</td>
                <td style="background-color: ${xyBriToHex(PHSchedule.lightState)};">${xyBriToHex(PHSchedule.lightState)}</td>
                <td>${PHSchedule.recurringTimerInterval}</td>
                <td>${PHSchedule.created}</td>
                <td>${PHSchedule.startTime}</td>
                <td>${PHSchedule.hasLocalTime}</td>
                <td>${PHSchedule.autodelete}</td>
                <td>${PHSchedule.status}</td>
                <td>${PHSchedule.timer}</td>
                <td>${PHSchedule.randomTime}</td>
                <td>${PHSchedule.recurringDays}</td>
            </tr>
        `).join("")}
    </tbody>
</table>
`;
}

function updateSensors(data) {
    var PHSensors = JSON.parse(data.body);
    document.getElementById("sensors").innerHTML = String.raw`
<table class="mdl-cell mdl-cell--12-col mdl-data-table mdl-js-data-table mdl-shadow--2dp">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">Name</th>
            <th>State</th>
            <th>Configuration</th>
        </tr>
    </thead>
    <tbody>
        ${PHSensors.map(PHSensor => String.raw`
            <tr>
                <td class="mdl-data-table__cell--non-numeric">${PHSensor.name}</td>
                <td>${JSON.stringify(PHSensor.state)}</td>
                <td>${JSON.stringify(PHSensor.configuration)}</td>
            </tr>
        `).join("")}
    </tbody>
</table>
`;
}

function updateRules(data) {
    var PHRules = JSON.parse(data.body);
    document.getElementById("rules").innerHTML = String.raw`
<table class="mdl-cell mdl-cell--12-col mdl-data-table mdl-js-data-table mdl-shadow--2dp">
    <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">Name</th>
            <th>lastTriggered</th>
            <th>timesTriggered</th>
            <th>creationTime</th>
            <th>status</th>
            <th>conditions</th>
            <th>actions</th>
        </tr>
    </thead>
    <tbody>
        ${PHRules.map(PHRule => String.raw`
            <tr>
                <td class="mdl-data-table__cell--non-numeric">${PHRule.name}</td>
                <td>${PHRule.lastTriggered}</td>
                <td>${PHRule.timesTriggered}</td>
                <td>${PHRule.creationTime}</td>
                <td>${PHRule.status}</td>
                <td>${JSON.stringify(PHRule.conditions)}</td>
                <td>${JSON.stringify(PHRule.actions)}</td>
            </tr>
        `).join("")}
    </tbody>
</table>
`;
}

var socket = new WebSocket('ws://localhost:8080/hue-controller/websocket');
//var socket = new SockJS('/hue-controller');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    //stompClient.subscribe('/topic/bridge-configuration', updateGroups);
    stompClient.subscribe('/topic/lights', updateLights);
    stompClient.subscribe('/topic/groups', updateGroups);
    stompClient.subscribe('/topic/scenes', updateScenes);
    stompClient.subscribe('/topic/schedules', updateSchedules);
    stompClient.subscribe('/topic/sensors', updateSensors);
    stompClient.subscribe('/topic/rules', updateRules);
    //stompClient.subscribe('/app/topic/bridge-configuration', updateGroups);
    stompClient.subscribe('/app/topic/lights', updateLights);
    stompClient.subscribe('/app/topic/groups', updateGroups);
    stompClient.subscribe('/app/topic/scenes', updateScenes);
    stompClient.subscribe('/app/topic/schedules', updateSchedules);
    stompClient.subscribe('/app/topic/sensors', updateSensors);
    stompClient.subscribe('/app/topic/rules', updateRules);
});

window.addEventListener("DOMContentLoaded", function () {
    document.getElementById("enable").onclick = function () {
        stompClient.send("/app/light/enable", {});
    };
    document.getElementById("disable").onclick = function () {
        stompClient.send("/app/light/disable", {});
    };
    document.getElementById("groups").onclick = function(event) {
        console.log(event.target.dataset);
        stompClient.send("/app/group/delete", {}, JSON.stringify(event.target.dataset));
    };
}, false);

