export interface IRelay {
  id?: number;
  zone?: string;
}

export class Relay implements IRelay {
  constructor(public id?: number, public zone?: string) {}
}
