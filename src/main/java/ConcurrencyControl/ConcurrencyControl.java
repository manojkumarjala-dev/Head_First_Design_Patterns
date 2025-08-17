package ConcurrencyControl;

public class ConcurrencyControl {
}

/*

critical section - mulitple resources accesing same code is
ex: seat booking with same id

let say three reqs read that seat is free
=> all three reqs have same seat allocated

handle concurency when multplie reqs handle same section or critical section

1. Use Synchronised block fot the critical section
=> only one user will go now, 1st req reads its free and goes away, now second conmes it will
 see the not free status

 synchronized works when 1 process has multiple threads.
 this will work but in case of microservies there are multple process not multiple threads, synchronized fails
 => here comes Distributed concurrency control
 - two types: 1. Optimistic Concurrency control
              2. Pessimistic Concurrency control

              before trying to understand these we need to get hold of 3 things
              1. What is the use of Transaction
                - it helps achieve integrity, meaning helps us avoid inconsistency
                Ex: debit from one account & credit in another account
                    debit occurs but credit fails => results in rollback => revert changes to the db that hapende with this txn

                If not using Transaction, in case of failure partial operations will result in inconsistent db
                => consistent: either full txn or no txn

              2. WHat is DB locking
                - ensures no other txn/query modifies the locked rows
                - two types of locks => shared lock / exclusive locks
                    shared lock => also known as read lock





 */