import { Component, OnInit } from '@angular/core';
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
        if (r.remainingTime && r.remainingTime > 0) {
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
