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

async function showChannelsByID(id) {
  try {
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTE2Mzc4NCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MDc3Mzg0fQ.HyzP-FlvjSnOt0jmpwqJQlkD9siz_Pn7i9V6j_5ORPwJ1qOwcUsNapC9BpvnEtI1UQ6x37wLbwDcMIAkYFV0eA';

    const response = await fetch(`http://localhost:8080/api/channels/${id}`, {
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
  const container = document.getElementById('channel-container');
  container.innerHTML = '';
  const div = document.createElement('div');
  const formatDisplay = `Channel Name: ${data.name}, User: ${data.userProfiles.map(user => user.username)} Messages: ${data.messages.map(cont => cont.content)}`;
  div.innerHTML = formatDisplay;
  container.appendChild(div);
}

showChannelsByID(4);
