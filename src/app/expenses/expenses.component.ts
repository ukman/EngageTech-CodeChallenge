import { Component, OnInit } from '@angular/core';
import { Expense } from './expense';
import { ExpenseService } from '../expense.service';
import { VatService } from '../vat.service';

@Component({
  selector: 'app-expenses',
  templateUrl: './expenses.component.html',
  styleUrls: ['./expenses.component.less']
})
export class ExpensesComponent implements OnInit {

  newExpense: Expense = {
  };

  expenses: Expense[];

  vat: number;

  constructor(private expenseService: ExpenseService,
    private vatService: VatService,
  ) { }

  ngOnInit(): void {
    this.loadExpenses();
    this.loadVat();
  }

  private loadExpenses(): void {
    this.expenseService.getExpenses().subscribe((expenses) => {
      this.expenses = expenses;
    });
  }

  saveExpense(): void {
    this.expenseService.createExpense(this.newExpense).subscribe(() => {
      this.clearExpense();
      this.loadExpenses();
    });
  }

  clearExpense(): void {
      this.newExpense = {
      };
  }


  private loadVat(): void {
    this.vatService.getVat("uk").subscribe((vat) => {
      this.vat = vat;
    });
  }
}
