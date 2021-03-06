import { Moment } from 'moment';

export interface IProgrammation {
  id?: number;
  date?: Moment;
  day?: number;
  sequence?: string;
  counter?: number;
  dayFrequency?: number;
}

export class Programmation implements IProgrammation {
  constructor(
    public id?: number,
    public date?: Moment,
    public day?: number,
    public sequence?: string,
    public counter?: number,
    public dayFrequency?: number
  ) {
    this.id = id ? id : undefined;
    this.date = date ? date : undefined;
    this.day = day ? day : 1;
    this.sequence = sequence ? sequence : '3';
    this.counter = counter ? counter : undefined;
    this.dayFrequency = dayFrequency ? dayFrequency : 1;
  }
}
