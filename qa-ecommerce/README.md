# E-commerce Web Application Testing

Portfolio QA project covering one e-commerce application end to end: manual test design,
a logged defect set, REST API validation in Postman, and a Selenium/Java smoke suite built
with the Page Object Model.

Everything here was executed against the live applications listed below, not written on paper.

| Layer | Target application | Tools |
| --- | --- | --- |
| Manual testing | https://www.saucedemo.com | Jira-style test cases and defect log (CSV, importable) |
| API testing | https://api.practicesoftwaretesting.com (Toolshop e-commerce API) | Postman collection, newman runner |
| Test automation | https://www.saucedemo.com | Java 17+, Selenium WebDriver 4.33, TestNG 7.11, Maven, POM |

Why two applications: saucedemo is a UI-only demo store with no public REST API, so the API
layer is validated against the Toolshop e-commerce API, which exposes the same core domain
(catalogue, auth, cart, invoice). Both are public demo systems with documented test accounts.

## Structure

```
qa-ecommerce/
├── manual-testing/
│   ├── test-cases.csv        # 64 manual test cases
│   └── defect-log.csv        # 21 defects, Jira CSV import format
├── api/
│   ├── Ecommerce-API-Smoke.postman_collection.json   # 15 requests, 36 assertions
│   └── Ecommerce-API-Local.postman_environment.json
└── automation/
    ├── pom.xml
    ├── testng.xml
    └── src/
        ├── main/java/pages/    # LoginPage, InventoryPage, CartPage, CheckoutPage, BasePage
        └── test/java/tests/    # LoginTest, InventoryTest, CartTest, CheckoutTest, NavigationTest
```

## Manual testing

`manual-testing/test-cases.csv` holds 64 test cases (IDs TC-001 to TC-064) with module, priority,
type, preconditions, steps, test data, expected result and execution status.

| Module | Cases |
| --- | --- |
| Login | 12 |
| Inventory | 12 |
| Checkout | 14 |
| Cart | 8 |
| Navigation | 7 |
| Account | 5 |
| Usability | 4 |
| Compatibility | 2 |

Execution status after this cycle: **33 passed** (all of them covered by the automated suite below),
**1 failed** (TC-046, defect QA-121), **30 not run** — the manual-only cases (responsive checks,
cross-browser passes, exploratory account checks) still need a human tester in front of a browser.

The defect log contains 21 bugs (QA-101 to QA-121) recorded with severity, priority, environment,
preconditions, reproduction steps, expected vs actual result and the evidence used to confirm them.
Every entry was reproduced in a headless Chrome session with Selenium before being logged; the
`Evidence` column says how each one was confirmed.

To import into Jira: **Issues > Import issues from CSV**, map the columns
`Summary -> Summary`, `Issue Type -> Issue Type`, `Priority -> Priority`, `Module -> Component`,
`Steps to Reproduce + Expected Result + Actual Result -> Description`, `Labels -> Labels`.
The `Issue Key` column is deliberately left out of the mapping so Jira assigns its own keys.

Two of the logged defects come from saucedemo's intentionally broken demo accounts
(`problem_user`, `performance_glitch_user`); they are still real, reproducible behaviour
differences and are labelled as such.

## API testing with Postman

Import both files into Postman and select the **Ecommerce-API-Local** environment. The collection
runs top to bottom and chains values between requests (product id, access token, cart id, address,
invoice number), so run the whole collection rather than single requests.

Coverage:

1. **Catalog** — paginated product list, product by id, search, postcode lookup used as the
   checkout address.
2. **Authentication** — login returns a bearer token, profile endpoint returns the same user.
3. **Cart** — create cart, add a product with quantity 2, read the cart back and assert the line item.
4. **Checkout** — create an invoice from the cart, then confirm it appears in the invoice list.
5. **Negative cases** — profile without a token (401), wrong password (401), unknown product (404),
   invalid cart id (404).

Latest run (newman, no failures):

```
requests 15 | assertions 36 | failures 0 | average response time 190-310 ms per run
```

Command line equivalent:

```bash
npm install -g newman
newman run api/Ecommerce-API-Smoke.postman_collection.json \
  -e api/Ecommerce-API-Local.postman_environment.json
```

The demo credentials in the environment file are saucedemo/Toolshop public demo credentials, not
personal accounts.

## Automation with Java, Selenium and TestNG

Requires JDK 17+ and Maven. ChromeDriver is resolved automatically by Selenium Manager, and the
browser runs headless by default.

```bash
cd automation
mvn test
```

Optional overrides: `mvn test -DbaseUrl=https://www.saucedemo.com`.

The suite has 27 tests in five classes, all mapped to test case IDs:

| Class | Tests | What it verifies |
| --- | --- | --- |
| `LoginTest` | 7 | valid login, locked-out user, wrong password, empty username, empty password, error dismissal, masked password |
| `InventoryTest` | 8 | six products, sort by price both ways, sort by name, add one/two items, remove item, slow demo account login |
| `CartTest` | 5 | cart contents and price, removal, badge decrement, empty cart, continue shopping |
| `CheckoutTest` | 4 | full order flow with totals maths and empty cart afterwards, required first name, required postal code, cancel |
| `NavigationTest` | 3 | logout, reset app state, protected page redirect when logged out |

Latest run:

```
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Design notes: `BaseTest` owns the driver lifecycle (one fresh session per test, always quit),
`BasePage` holds the explicit waits and element helpers, and every page exposes intention-revealing
methods (`loginAsStandardUser()`, `addToCart("sauce-labs-backpack")`, `finish()`), so tests read as
user journeys and locators live only in the page objects.

## Limitations

- The 30 manual-only cases are designed but not executed; no results are claimed for them.
- No cross-browser matrix (Firefox/Safari) was run — the suite is Chrome-only.
- The defect log reflects the demo accounts and data available at the time of testing; the
  performance numbers are single-run measurements, not benchmarks.
- The HTML report from Surefire lives in `automation/target/surefire-reports` and is not committed.
