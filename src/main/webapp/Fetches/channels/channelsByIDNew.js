//For instructions on how I did this refer to userProfileID.js file
let listChannelsById = fetch('http://localhost:8080/api/channels/messages-by-id/4', {
  headers: {
    Authorization:
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTE1MTEwMiwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MDY0NzAyfQ.PM3If2kFSRPrt26rT_oR59qTo2qBGRNSO9Y1MVDx-7oEGU97gpZwd4fEKmyUf6tYUCZT47QW7LrhwjWlEfphUw',
  },
})
  .then(response => response.json())
  .then(data => displayData(data));

function displayData(data) {
  const container = document.getElementById('channel2-container');
  container.innerHTML = '';
  const div = document.createElement('div');
  const formatDisplay = `Channel Name: ${data.name}, Messages: ${data.messages}`;
  div.innerHTML = formatDisplay;
  container.appendChild(div);
}
console.log('Wzup');

//http://localhost:8080/api/channels/1/messages
