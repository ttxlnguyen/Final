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
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTU5MDc0MSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1NTA0MzQxfQ.3ee1ZrxdwawkDcsaYCjRbQI4ZjsU-GgkSv6niLP0Afq3fH0I4R4luIyuJkP3XFx6H6FD8ZZxCVUGA4i9coJkHQ';

    const response = await fetch(`http://localhost:8080/api/channels/user-profile/${username}`, {
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
  // const div = document.createElement('div');
  // const formatDisplay = `<p>User: ${data.username} <br> Channel Names: <br> ${data.channels.map(channelname => channelname.name).join('<br> ')} <br> Messages: <br>${data.messages.map(message => message.content).join('<br> ')}</p>`;
  // const channelDisplays = data.channels.map(channel => {
  //   const channelName = channel.name;
  //   const messages = channel.messages.map(message => message.content).join('<br> ');
  //   return `<strong>Channel: ${channelName}</strong><br> Messages:<br> ${messages}`;
  // }).join('<br><br>');

  // const formatDisplay = `<p>${channelDisplays}</p>`;
  // div.innerHTML = formatDisplay;
  // container.appendChild(div);

  // Generate HTML for each channel
  const channelDisplays = data
    .map(channel => {
      const channelName = channel.name;
      const messages = channel.messages.map(message => message.content).join('<br> ') || 'No messages';
      return `<strong>Channel: ${channelName}</strong><br> Messages:<br> ${messages}`;
    })
    .join('<br><br>');

  container.innerHTML = `<p>${channelDisplays}</p>`;
}

const userName = 'ForrestOfSorts';
showChannelsByUsername(userName);
