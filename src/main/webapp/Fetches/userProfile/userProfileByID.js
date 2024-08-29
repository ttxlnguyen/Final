let listUserProfileByID = fetch('http://localhost:8080/api/user-profiles/1', {
  headers: {
    Authorization:
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNDk4NDk0MiwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI0ODk4NTQyfQ.xffQj9bQh9rFQOJU8wrxauBNYNDyGHESffSVUUduYg4tcv84_P1NyFsKt0BvCUBmOQJrghsKp0vS2aTvpBtmAQ',
  },
})
  .then(response => response.json())
  .then(data => displayData(data));

function displayData(data) {
  const container = document.getElementById('profile-container');
  container.innerHTML = '';
  const div = document.createElement('div');
  const formatDisplay = `Welcome ${data.username}! Your Channels: ${data.channels.map(channel => channel.name).join(', ')}`;
  div.innerHTML = formatDisplay;
  container.appendChild(div);
}
console.log('Hi');
