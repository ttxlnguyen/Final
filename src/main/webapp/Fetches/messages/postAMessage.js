// async function postAMessage() {
//     try{
//         const token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTE2Mzc4NCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MDc3Mzg0fQ.HyzP-FlvjSnOt0jmpwqJQlkD9siz_Pn7i9V6j_5ORPwJ1qOwcUsNapC9BpvnEtI1UQ6x37wLbwDcMIAkYFV0eA';

//         const response = await fetch('http://localhost:8080/api/messages', {
//         method: 'POST',
//         headers: {
//             Authorization: `Bearer ${token}`,
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(response),
//     }
//     .then(response => response.json())
// .then(response => console.log(JSON.stringify(response)))

// );

// }catch (error){
//     console.error('Fetch Error:', error);
// }
// }

// postAMessage();

async function postAMessage(messageData) {
  try {
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTMxNjY0MywiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MjMwMjQzfQ.DV6T7oEpTNrJGIJ5LqyotC1Dk8bh_fxBdbESM8ocRNZD8nALrvQIq30GKjJOlV04jRLgw3VnKfFWaAyXI5Mb0g';

    const response = await fetch('http://localhost:8080/api/messages', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(messageData),
    });

    if (response.ok) {
      const data = await response.json();
      document.getElementById('response-container').innerText = JSON.stringify(data, null, 2);
      console.log(data);
    } else {
      console.error('Error:', response.statusText);
      document.getElementById('response-container').innerText = `Error: ${response.statusText}`;
    }
  } catch (error) {
    console.error('Fetch Error:', error);
    document.getElementById('response-container').innerText = `Fetch Error: ${error.message}`;
  }
}

document.getElementById('message-form').addEventListener('submit', function (event) {
  event.preventDefault();

  const content = document.getElementById('content').value;
  const messageData = {
    content,
    sentAt: new Date().toISOString(),
  };
  postAMessage(messageData);
  console.log(messageData);
});

console.log('hi');
