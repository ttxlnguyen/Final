console.log('Hi');
async function userProfileID(id) {
  try {
    //putting bearer token into a container
    const token =
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNTE2Mzc4NCwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI1MDc3Mzg0fQ.HyzP-FlvjSnOt0jmpwqJQlkD9siz_Pn7i9V6j_5ORPwJ1qOwcUsNapC9BpvnEtI1UQ6x37wLbwDcMIAkYFV0eA';

    const response = await fetch(`http://localhost:8080/api/user-profiles/${id}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      }, //setting up the api fetch above
    });
    if (response.ok) {
      const data = await response.json();
      console.log(data);
      displayUserProfile(data);
    } else {
      console.log('Error:', response.statusText);
    } //this will display the fetch in json
  } catch {
    console.error('Fetch Error:', error);
  }
}

//helper function to display the data. its called on line 17
function displayUserProfile(data) {
  const container = document.getElementById('profile-container'); //the id that we will call in the html/react front end setup
  container.innerHTML = ''; // Emptying the body of the display and we'll later fill it with the data we want
  const div = document.createElement('div'); //creating the div element in the html
  const formatDisplay = `Welcome ${data.username}! Your Channels: ${data.channels.map(channel => channel.name).join(', ')}`; //formatting what we want to display
  div.innerHTML = formatDisplay; //adding it to the html string
  container.appendChild(div); //This means the newly created div, now filled with the formatted content, will be displayed inside the profile-container
}

//Calling function which will display the json on html
userProfileID(1);
