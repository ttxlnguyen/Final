async function createAChannel(channelData, username) {
  try {
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTQ3OTAzMCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MzkyNjMwfQ.CSMxBTrzwiGCroE95Cyh5Yn6MkbM0uoPT5fg7Fu8Djq7bnkabZHHjV58HlvekaiXLsIx0UKNWmZU_bbqqbtupA';

    const response = await fetch(`http://localhost:8080/api/channels/username/${username}`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(channelData),
    });

    if (response.ok) {
      const data = await response.json();
      document.getElementById('channelp-container').innerText = JSON.stringify(data, null, 2);
      console.log(data);
    } else {
      console.error('Error:', response.statusText);
      document.getElementById('channelp-container').innerText = `Error: ${response.statusText}`;
    }
  } catch (error) {
    console.error('Fetch Error:', error);
    document.getElementById('channelp-container').innerText = `Fetch Error: ${error.message}`;
  }
}

document.getElementById('channel-form').addEventListener('submit', function (event) {
  event.preventDefault();

  const content = document.getElementById('content').value;
  const username = 'wpc3';
  const channelData = {
    name: content,
    description: 'A new channel created by ' + username,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    createdBy: username,
    updatedBy: username,
    messageCount: 0,
    unreadCount: 0,
    messages: [],
  };

  createAChannel(channelData, username);
  console.log(channelData);
});
