<img width="1416" alt="Screen Shot 2024-09-12 at 2 17 49 PM" src="https://github.com/user-attachments/assets/3880f7a7-79e3-4a70-ba7a-c0cd1f686cd9">
<img width="1414" alt="Screen Shot 2024-09-12 at 2 18 17 PM" src="https://github.com/user-attachments/assets/fd19be7c-79c1-4f8c-adc6-66bee17d3a5f">
Powered by: Timothy Nguyen, Will Chapman, Ian Gordan, Coreye Ross

Co-lab application consists of public channels as well as private channels which the user views as direct messages. We created the relationships between the channel, messages, and user data models stored in our MySql database, which our messaging and channeling functionality are dependent on. The public channels listed is essentially a fetch call to an API we created to specifically find the channels saved or created by the user. Clicking into a channels will display the messaging data sent by the users who have a relationship with that specific channel. Not only can the user view all the messages in their channels, Co-Lab also has the functionaly to post messages to channels as well. When a user submits a message, the frontend, built using React, sends a POST request to our backend, which is powered by Spring Application Server the request contains the message data, the channel ID, and user information. On the backend, we have a RESTful API that handles these requests. The business logic which is wrote in java ensures that the message is stored in the correct channel by validating the channel ID and linking the message to the appropriate user. The message is then saved in our MySQL database, this combination of React, Spring Server Application, Java & MySQL provide a seamless messaging experience for our users.

Frontend Repository: https://github.com/ttxlnguyen/FinalReact
```
---Github Repositories---
Will Chapman: https://github.com/wpc3
Ian Gordan: https://github.com/igordon215
Coreye Ross: https://github.com/coreyeross25
```
