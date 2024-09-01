import { IChannels } from 'app/shared/model/channels.model';

export interface IUserProfile {
  id?: number;
  username?: string | null;
  email?: string | null;
  password?: string | null;
  channels?: IChannels[] | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
