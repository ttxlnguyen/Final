import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IMessages } from 'app/shared/model/messages.model';

export interface IReactions {
  id?: number;
  reaction?: string | null;
  user?: IUserProfile | null;
  messages?: IMessages | null;
}

export const defaultValue: Readonly<IReactions> = {};
