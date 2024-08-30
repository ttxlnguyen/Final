import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IChannels {
  id?: number;
  name?: string | null;
  userProfiles?: IUserProfile[] | null;
}

export const defaultValue: Readonly<IChannels> = {};
