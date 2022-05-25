import { Component, OnInit } from '@angular/core';
import { CountdownConfig } from 'ngx-countdown';
import { Subscription, timer } from 'rxjs';
import { ElectroVanneService } from '../../core/electro-vanne/electro-vanne.service';
import { StatusRelay } from './../../core/electro-vanne/status-relay.model';

@Component({
  selector: 'jhi-electro-vanne',
  templateUrl: './electro-vanne.component.html',
})
export class ElectroVanneComponent implements OnInit {
  relays: StatusRelay[] | null;
  timers: Subscription[] = [];
  lockProlongation: boolean;
  countdown: Map<number, CountdownConfig> = new Map();

  constructor(private electroVanneService: ElectroVanneService) {
    this.relays = [];
    this.lockProlongation = false;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.lockProlongation = false;
    this.timers.forEach(sub => {
      sub.unsubscribe();
    });
    this.timers = [];
    this.electroVanneService.status().subscribe(relays => {
      console.log(relays.body);
      this.relays = [];
      this.relays = relays.body;
      this.relays?.forEach(r => {
        this.countdown.delete(r.id);
        if (r.remainingTime && r.remainingTime > 0) {
          this.countdown.set(r.id, this.createCountDownConfig(r.remainingTime));
          const t = timer(r.remainingTime + 500);
          this.timers.push(
            t.subscribe(() => {
              console.log('terminé load');
              this.load();
            })
          );
          const timerLock = timer(r.remainingTime);
          this.timers.push(
            timerLock.subscribe(() => {
              console.log('terminé lock');
              this.lockProlongation = true;
            })
          );
        }
      });
    });
  }

  createCountDownConfig(remainingTime: number): CountdownConfig {
    const config: CountdownConfig = {};
    config.leftTime = remainingTime / 1000;
    config.format = 'HH:mm:ss';
    return config;
  }

  getCountdownConfig(relayId: number): CountdownConfig {
    const config = this.countdown.get(relayId);
    if (config) {
      return config;
    }
    return {};
  }

  hasCountdownConfig(relayId: number): boolean {
    return this.countdown.has(relayId);
  }

  open(id: number): void {
    this.electroVanneService.open(id).subscribe(
      () => {
        this.load();
      },
      () => {
        this.load();
      }
    );
  }

  close(id: number): void {
    this.electroVanneService.close(id).subscribe(
      () => {
        this.load();
      },
      () => {
        this.load();
      }
    );
  }

  addTime(id: number): void {
    this.electroVanneService.addTime(id).subscribe(
      () => {
        this.load();
      },
      () => {
        this.load();
      }
    );
  }

  cancel(id: number): void {
    this.electroVanneService.cancel(id).subscribe(
      () => {
        this.load();
      },
      () => {
        this.load();
      }
    );
  }

  cancelAll(): void {
    this.electroVanneService.cancelAll().subscribe(
      () => {
        this.load();
      },
      () => {
        this.load();
      }
    );
  }
}
