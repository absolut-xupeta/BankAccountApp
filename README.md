### Bank Account App
Simple bank-like app for study purposes.

### TODO
Implementation guideline:
- [ ] Base class *Account*

- *Account* properties:
- accountNumber: Int, password: String, ownerName: String, balance: Currency, creationDate: Date
- Currency type will have: (amount: Long, currency: String) (Example: "BRL")
Currency amount will be in cents.

- *Account* methods:
- deposit(), withdraw()

All proprerties are gonna be saved inside a .csv file inside the assets folder like:

AccountNumber: 0001
Password: Pa$$20
OwnerName: John Doe
Balance: 00,00
Currency: BRL
CreationDate: 28/06/202
Account type: CurrentAccount

This is just a csv outline, the properties will be the headers and everything will be divided by ";".

With each account, the account number will increase based on the previous.

---
- [ ] Derived classes *CurrentAccount* and *SavingsAccount*.

Both classes will inherit from base *Account*.

- *SavingsAccount* extras:
- deposit() and withdraw() need to calculate the fees.
- *CurrentAccount* extras:

