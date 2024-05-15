export interface AccountDetails {
  accountId: string;
  balance: number;
  currentPage: number;
  pageSize: number;
  accountOperationDTOS: AccountOperationDTO[];
  totalPages: number;
}

export interface AccountOperationDTO {
  id: number;
  operationDate: Date;
  amount: number;
  operationType: string;
  description: string;
}
