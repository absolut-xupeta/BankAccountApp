### Bank Account App
Simple bank-like app for study purposes.

### TODO
Implementation guideline:
- [ ] Base class *Account*

- *Account* properties:
- accountNumber: Int, password: String, ownerName: String, balance: Currency, creationDate: Date
- Currency type will have: (amount: Int, currency: String) (Example: "BRL")

- *Account* methods:
- deposit(), withdraw()

All proprerties are gonna be saved inside a .csv file inside the assets folder like:
AccountNumber;Password;OwnerName;Balance;Currency;CreationDate;Account type

0001---------;Pa$$20--;John Doe-;00,00--;BRL-----;28/06/2021--;CurrentAccount

With each account, the account number will increase based on the previous.

- [ ] Derived classes *CurrentAccount* and *SavingsAccount*.

Both classes will inherit from base *Account*.

- *SavingsAccount* extras:
- deposit() and withdraw() need to calculate the fees.
- *CurrentAccount* extras:

