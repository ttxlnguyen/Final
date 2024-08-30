import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IChannels } from 'app/shared/model/channels.model';

export interface IMessages {
  id?: number;
  content?: string | null;
  sentAt?: dayjs.Dayjs | null;
  editedAt?: dayjs.Dayjs | null;
  isDeleted?: boolean | null;
  userProfile?: IUserProfile | null;
  channels?: IChannels | null;
}

export const defaultValue: Readonly<IMessages> = {
  isDeleted: false,
};
