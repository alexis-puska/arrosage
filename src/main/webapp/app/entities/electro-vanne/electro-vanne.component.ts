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

  constructor(private electroVanneService: ElectroVanneService) {
    this.relays = [];
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
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
              this.load();
            })
          );
        }
      });
    });
  }

  open(relay: number): void {
    this.electroVanneService.open(relay).subscribe(
      () => {
        this.load();
      },
      () => {
        this.load();
      }
    );
  }

  close(relay: number): void {
    this.electroVanneService.close(relay).subscribe(
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
