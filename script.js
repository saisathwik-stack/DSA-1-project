let users = [];
let activities = [];

/* REGISTER USER */
function registerUser(){

let id = document.getElementById("userId").value;
let name = document.getElementById("userName").value;
let goal = parseInt(document.getElementById("goalSteps").value);

users.push({
id:id,
name:name,
goalSteps:goal
});

alert("User Registered");
}

/* SHOW USERS */
function showUsers(){

let list = document.getElementById("userList");
list.innerHTML="";

users.forEach(u=>{
let li=document.createElement("li");
li.textContent=`${u.id} - ${u.name} (Goal: ${u.goalSteps})`;
list.appendChild(li);
});
}

/* ADD ACTIVITY */
function addActivity(){

let userId=document.getElementById("activityUserId").value;
let type=document.getElementById("activityType").value;
let steps=parseInt(document.getElementById("steps").value);
let calories=parseInt(document.getElementById("calories").value);
let duration=parseInt(document.getElementById("duration").value);

activities.push({
userId,
type,
steps,
calories,
duration
});

alert("Activity Added");
}

/* SHOW ACTIVITIES */
function showActivities(){

let list=document.getElementById("activityList");
list.innerHTML="";

activities.forEach(a=>{
let li=document.createElement("li");
li.textContent=`${a.userId} | ${a.type} | Steps:${a.steps} | Calories:${a.calories}`;
list.appendChild(li);
});
}

/* LEADERBOARD */
function showLeaderboard(){

let board=document.getElementById("leaderboard");
board.innerHTML="";

let sorted=[...activities].sort((a,b)=>b.calories-a.calories);

sorted.slice(0,5).forEach((a,index)=>{

let li=document.createElement("li");
li.textContent=`Rank ${index+1}: ${a.userId} - ${a.calories} calories`;

board.appendChild(li);

});
}