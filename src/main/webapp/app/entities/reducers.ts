import userProfile from 'app/entities/user-profile/user-profile.reducer';
import messages from 'app/entities/messages/messages.reducer';
import channels from 'app/entities/channels/channels.reducer';
import reactions from 'app/entities/reactions/reactions.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  messages,
  channels,
  reactions,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
