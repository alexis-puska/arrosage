import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlackList } from 'app/shared/model/black-list.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BlackListService } from './black-list.service';
import { BlackListDeleteDialogComponent } from './black-list-delete-dialog.component';

@Component({
  selector: 'jhi-black-list',
  templateUrl: './black-list.component.html',
})
export class BlackListComponent implements OnInit, OnDestroy {
  blackLists: IBlackList[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected blackListService: BlackListService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.blackLists = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.blackListService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IBlackList[]>) => this.paginateBlackLists(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.blackLists = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBlackLists();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBlackList): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBlackLists(): void {
    this.eventSubscriber = this.eventManager.subscribe('blackListListModification', () => this.reset());
  }

  delete(blackList: IBlackList): void {
    const modalRef = this.modalService.open(BlackListDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.blackList = blackList;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBlackLists(data: IBlackList[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.blackLists.push(data[i]);
      }
    }
  }
}
