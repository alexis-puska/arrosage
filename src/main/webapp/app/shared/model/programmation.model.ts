import { Moment } from 'moment';

export interface IProgrammation {
  id?: number;
  date?: Moment;
  day?: number;
  sequence?: string;
  counter?: number;
}

export class Programmation implements IProgrammation {
  constructor(public id?: number, public date?: Moment, public day?: number, public sequence?: string, public counter?: number) {}
}
