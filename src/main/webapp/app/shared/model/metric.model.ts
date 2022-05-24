import { Moment } from 'moment';

export interface IMetric {
  zone?: number;
  blackList?: number;
  programmation?: number;
}

export class Metric implements IMetric {
  constructor(public zone?: number, public blackList?: number, public programmation?: number) {}
}
