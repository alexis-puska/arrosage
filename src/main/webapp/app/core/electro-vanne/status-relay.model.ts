import { Moment } from 'moment';

export interface IStatusRelay {
  relay: number;
  zone: string;
  on: boolean;
  remainingTime: number;
  estimatedHours: Date;
}

export class StatusRelay implements IStatusRelay {
  constructor(public relay: number, public zone: string, public on: boolean, public remainingTime: number, public estimatedHours: Date) {}
}
