import { IncreaseStockComponent } from './components/increase-stock/increase-stock.component';
import { ReduceStockComponent } from './components/reduce-stock/reduce-stock.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './views/home/home.component';

import { PageNotFoundComponent } from './views/page-not-found/page-not-found.component';
import { MatrixDetailComponent } from './views/matrix-detail/matrix-detail.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomeComponent,
    pathMatch: 'full',
  },
  {
    path: 'increase/:id',
    component: IncreaseStockComponent,
    pathMatch: 'full',
  },
  {
    path: 'reduce/:id',
    component: ReduceStockComponent,
    pathMatch: 'full',
  },
  {
    path: 'details/:id',
    component: MatrixDetailComponent,
    pathMatch: 'full',
  },
  {
    path: '404',
    component: PageNotFoundComponent,
    pathMatch: 'full',
  },
  { path: '**',
   component: PageNotFoundComponent,
   pathMatch: 'full'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
