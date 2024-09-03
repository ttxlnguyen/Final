//For instructions on how I did this refer to userProfileID.js file
// let listChannelsById = fetch('http://localhost:8080/api/channels/4', {
//   headers: {
//     Authorization:
//       'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTA0ODk2MCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI0OTYyNTYwfQ.GIGkXcZ2nRCsvEdswRqmz_LfNcMaG1l_zuaTeOdOd4q0KZUVqej5W9OakQJJEp3rSc__BGIFKUgjY4IHGnyHuA',
//   },
// })
//   .then(response => response.json())
//   .then(data => displayData(data));

// function displayData(data) {
//   const container = document.getElementById('channel-container');
//   container.innerHTML = '';
//   const div = document.createElement('div');
//   const formatDisplay = `Channel Name: ${data.name}, User: ${data.userProfiles.map(user=>user.username)} Messages: ${data.messages.map(cont=>cont.content)}`;
//   div.innerHTML = formatDisplay;
//   container.appendChild(div);
// }
console.log('Wzup');

//http://localhost:8080/api/channels/1/messages

async function showChannelsByUsername(username) {
  try {
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTM5MzM4NSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MzA2OTg1fQ.zOzMDDFuGjb-Bi7XJHTY9i9XRQqQMsG6jfODOGHwsVFGWG5PkZt4rk-dmJGbOCK5HH4_8Dyj8zXxww-wrD_I8A';

    const response = await fetch(`http://localhost:8080/api/user-profiles/username/${username}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (response.ok) {
      const data = await response.json();
      console.log(data);
      displayChannelInfo(data);
    } else {
      console.log('Error:', response.statusText);
    }
  } catch (error) {
    console.error('Fetch Error:', error);
  }
}

function displayChannelInfo(data) {
  const container = document.getElementById('user-container');
  container.innerHTML = '';
  const div = document.createElement('div');
  const formatDisplay = `<p>User: ${data.username} <br> Channel Names: <br> ${data.channels.map(channelname => channelname.name).join('<br> ')} </p>`;
  div.innerHTML = formatDisplay;
  container.appendChild(div);
}

const userName = 'ForrestOfSorts';
showChannelsByUsername(userName);
