//This fetch function grabs the user profile by id api and will display it on the front end..
let listUserProfileByID = fetch('http://localhost:8080/api/user-profiles/1', {
  //The url for the api endpoint
  headers: {
    //This is the token that allows us to fetch the api and display it to a different origin
    Authorization:
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNDk4NDk0MiwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI0ODk4NTQyfQ.xffQj9bQh9rFQOJU8wrxauBNYNDyGHESffSVUUduYg4tcv84_P1NyFsKt0BvCUBmOQJrghsKp0vS2aTvpBtmAQ',
  },
})
  .then(response => response.json()) //This is the respone object turned into a json object
  .then(data => displayData(data)); //This is the display data function below which we'll display the data in the exact format we want it to

//Helper function to actually display our api call
function displayData(data) {
  const container = document.getElementById('profile-container'); //the id that we will call in the html/react front end setup
  container.innerHTML = ''; // Emptying the body of the display and we'll later fill it with the data we want
  const div = document.createElement('div'); //creating the div element in the html
  const formatDisplay = `Welcome ${data.username}! Your Channels: ${data.channels.map(channel => channel.name).join(', ')}`; //formatting what we want to display
  div.innerHTML = formatDisplay; //adding it to the html string
  container.appendChild(div); //This means the newly created div, now filled with the formatted content, will be displayed inside the profile-container
}

console.log('Hi');
