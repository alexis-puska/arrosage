import { Moment } from 'moment';

export interface IStatusRelay {
  id: number;
  zone: string;
  status: string;
  remainingTime: number;
  estimatedStopHours: Date;
  estimatedStartHours: Date;
}

export class StatusRelay implements IStatusRelay {
  constructor(
    public id: number,
    public zone: string,
    public status: string,
    public remainingTime: number,
    public estimatedStopHours: Date,
    public estimatedStartHours: Date
  ) {}
}
