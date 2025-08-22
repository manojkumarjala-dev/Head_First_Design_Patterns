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

              2. What is DB locking
                - ensures no other txn/query modifies the locked rows
                - two types of locks => shared lock / exclusive locks
                    shared lock    => also known as read lock, any number of txn can
                                      take shared lock on same rows(Only read, no updates for others)
                    exclusive lock => also known as write lock, Only one txn holds the exclusive lock and no other txn is given
                                      shared or exclusive lock.(No read, no write for others).

              3. What are Isolation level present?
                - Isolation says even though there are so many txn happening, each txn feels only they are working
                - Table
                                        Dirty_Read_Possible  Non-Repeatable_read_possible   Phantom_REad_Possible
                    Read Uncommited             Y                           Y                         Y
                    Read Commited               N                           Y                         Y
                    Repeatable Read             N                           N                         Y
                    Serializable                N                           N                         N

                Dirty Read: A transaction reads data that another transaction has written but not yet committed.
                Non-Repeatable Read: A transaction reads the same row twice and gets different values because another transaction updated it in between.
                Phantom Read: A transaction re-executes a query and sees new rows inserted (or deleted) by another transaction.


 */