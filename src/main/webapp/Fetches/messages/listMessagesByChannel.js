// For instructions on how I did this refer to userProfileID.js file
// const token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTE2Mzc4NCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MDc3Mzg0fQ.HyzP-FlvjSnOt0jmpwqJQlkD9siz_Pn7i9V6j_5ORPwJ1qOwcUsNapC9BpvnEtI1UQ6x37wLbwDcMIAkYFV0eA'
// let listMessagesByChannelID = fetch('http://localhost:8080/api/messages/channels/4', {
//     headers: {
//       Authorization:
//         `Bearer ${token}`,
//     },

// })
// .then(response => response.json())
// .then(data => displayData(data));

// function displayData(data) {
//     const container = document.getElementById('messages-container');
//     container.innerHTML = '';
//     data.forEach(message => {
//       const div = document.createElement('div');
//       const formatDisplay = ` Message: ${message.content} SentAt: ${message.sentAt}`;
//       div.innerHTML = formatDisplay;
//       container.appendChild(div);
//     });
// }

async function listMessagesByChannel(id) {
  try {
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTkwMzg3OCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1ODE3NDc4fQ.ptiFLkfktscYN7Dt2LqP9DcIuLDmQR65921aJAhys9lHKhMaoCAFn8OUNG3wY7fwANVPUBEvoGXTcsgfOl_yAA';

    const response = await fetch(`http://localhost:8080/api/messages/channels/${id}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });
    if (response.ok) {
      const data = await response.json();
      console.log(data);
      displayMessages(data); // Display the messages in a UI element
      // Handle the data as needed
    } else {
      console.error('Error:', response.statusText);
    }
  } catch (error) {
    console.error('Fetch Error:', error);
  }
}
function displayMessages(messages) {
  const container = document.getElementById('messages-container');
  container.innerHTML = ''; // Clear existing content

  if (messages.length === 0) {
    container.innerText = 'No messages found.';
    return;
  }

  const list = document.createElement('ul');
  messages.forEach(message => {
    const listItem = document.createElement('li');
    listItem.innerText = `Content: ${message.content}, Sent At: ${new Date(message.sentAt).toLocaleString()}`;
    list.appendChild(listItem);
  });

  container.appendChild(list);
}

// Call the function with a specific channel ID
listMessagesByChannel(1);
console.log('hi');
