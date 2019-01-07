import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Factura } from 'app/shared/model/factura.model';
import { FacturaService } from './factura.service';
import { FacturaComponent } from './factura.component';
import { FacturaDetailComponent } from './factura-detail.component';
import { FacturaUpdateComponent } from './factura-update.component';
import { FacturaDeletePopupComponent } from './factura-delete-dialog.component';
import { IFactura } from 'app/shared/model/factura.model';

@Injectable({ providedIn: 'root' })
export class FacturaResolve implements Resolve<IFactura> {
    constructor(private service: FacturaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Factura> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Factura>) => response.ok),
                map((factura: HttpResponse<Factura>) => factura.body)
            );
        }
        return of(new Factura());
    }
}

export const facturaRoute: Routes = [
    {
        path: 'factura',
        component: FacturaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'creditoApp.factura.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'factura/:id/view',
        component: FacturaDetailComponent,
        resolve: {
            factura: FacturaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.factura.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'factura/new',
        component: FacturaUpdateComponent,
        resolve: {
            factura: FacturaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.factura.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'factura/:id/edit',
        component: FacturaUpdateComponent,
        resolve: {
            factura: FacturaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.factura.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const facturaPopupRoute: Routes = [
    {
        path: 'factura/:id/delete',
        component: FacturaDeletePopupComponent,
        resolve: {
            factura: FacturaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.factura.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
