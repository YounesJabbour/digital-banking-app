export interface Operation {
    sourceAccountId: string;
    destinationAccountId: string;
    operationType: string;
    amount: number;
    description: string;
  }