import { Moment } from 'moment';

export interface IBlackList {
  id?: number;
  ipAddress?: string;
  locked?: boolean;
  unlockDate?: Moment;
  lastTry?: Moment;
  count?: number;
}

export class BlackList implements IBlackList {
  constructor(public id?: number, public ipAddress?: string, public locked?: boolean, public unlockDate?: Moment, public lastTry?: Moment, public count?: number) {
    this.locked = this.locked || false;
  }
}
