<div align="center">

# 🧱 SOLID PRINCIPLES

### _The Visual Field Guide for Java Backend Developers_

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Clean Code](https://img.shields.io/badge/Clean_Code-✓-blue?style=for-the-badge)
![For](https://img.shields.io/badge/Made_for-Rachit-ff69b4?style=for-the-badge)

</div>

```text
   ┌─────────────────────────────────────────────────────┐
   │   S  ·  O  ·  L  ·  I  ·  D                           │
   │                                                       │
   │   Five principles. One goal:                          │
   │   code that is easy to change, test, and trust.       │
   └─────────────────────────────────────────────────────┘
```

---

## 🗺️ Map of the Guide

| # | Principle | Letter | The One-Liner |
|:-:|:----------|:------:|:--------------|
| 1 | [Single Responsibility](#-1--single-responsibility-principle) | 🔴 **S** | One class, one job |
| 2 | [Open / Closed](#-2--openclosed-principle) | 🟠 **O** | Extend, don't modify |
| 3 | [Liskov Substitution](#-3--liskov-substitution-principle) | 🟣 **L** | Subtypes must fit |
| 4 | [Interface Segregation](#-4--interface-segregation-principle) | 🟢 **I** | Small, focused interfaces |
| 5 | [Dependency Inversion](#-5--dependency-inversion-principle) | 🔵 **D** | Depend on abstractions |

> 🎯 **Jump to:** [Real-World System](#-real-world-all-five-together) · [Spring & SOLID](#-spring--solid) · [Pitfalls](#-common-pitfalls) · [Cheat Sheet](#-cheat-sheet) · [Quiz](#-test-yourself)

---

## 🌟 Why This Matters

```text
   WITHOUT SOLID                      WITH SOLID
   ─────────────                      ──────────
   🍝  tangled spaghetti              🧩  clean building blocks
   😰  one change breaks ten things   😌  change one thing safely
   🐌  scary to refactor              🚀  refactor with confidence
   🧪  "how do I even test this?"     ✅  mock & test in seconds
```

<div align="center">

| 🧪 Testable | 🔧 Maintainable | 📈 Scalable |
|:-----------:|:---------------:|:-----------:|
| **🔁 Reusable** | **🤝 Team-friendly** | **🌱 Spring-ready** |

</div>

---

<div align="center">

# 🔴 1 · SINGLE RESPONSIBILITY PRINCIPLE

</div>

> ### 💡 _"A class should have only **ONE** reason to change."_

> 🎯 **The problem it solves**
> When one class handles many concerns (validation, DB, email, logging), a change to *any* of them risks breaking the others. The class becomes hard to read, impossible to reuse, and a nightmare to unit-test because everything is tangled together.

> 🛠️ **How it solves it**
> By giving each class a single, well-defined job. Now a change to email logic touches only `EmailService`, the DB code lives only in `UserRepository`, and each piece can be tested and reused in isolation. Fewer reasons to change = fewer ways to break.

```text
   ❌ BEFORE: one class wearing 4 hats          ✅ AFTER: one hat each
   ───────────────────────────────────         ──────────────────────

        ┌──────────────────┐                    ┌──────────────┐
        │   UserService    │                    │ UserValidator│
        │ ┌──────────────┐ │                    └──────────────┘
        │ │ validate     │ │                     ┌──────────────┐
        │ │ save to DB   │ │   ───────────▶      │ UserRepo     │
        │ │ send email   │ │    split into       └──────────────┘
        │ │ write logs   │ │                     ┌──────────────┐
        │ └──────────────┘ │                     │ EmailService │
        │   😵 too much!   │                     └──────────────┘
        └──────────────────┘                     ┌──────────────┐
                                                 │ Logger       │
                                                 └──────────────┘
```

<details>
<summary>🔴 &nbsp;<b>BAD — one class doing everything</b> &nbsp;<i>(click to expand)</i></summary>

```java
// 🚨 validates + talks to DB + sends email + logs — four reasons to change
public class UserService {
    public void registerUser(String username, String email, String password) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email");
        if (password.length() < 8)
            throw new IllegalArgumentException("Password too short");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db");
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, email);
        stmt.setString(3, hash(password));
        stmt.executeUpdate();

        sendWelcomeEmail(email);
        System.out.println("User registered: " + username);
    }
}
```
</details>

<details open>
<summary>🟢 &nbsp;<b>GOOD — one job per class</b> &nbsp;<i>(click to collapse)</i></summary>

```java
public class UserValidator {
    public void validate(String email, String password) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email");
        if (password.length() < 8)
            throw new IllegalArgumentException("Password too short");
    }
}

public class UserRepository {
    private final DataSource dataSource;
    public UserRepository(DataSource dataSource) { this.dataSource = dataSource; }
    public void save(User user) { /* persist to DB */ }
}

public class EmailService    { public void sendWelcome(String email) { /* ... */ } }
public class PasswordEncoder { public String encode(String raw) { return BCrypt.hashpw(raw, BCrypt.gensalt()); } }

// Orchestrator simply coordinates — it delegates real work
public class UserService {
    private final UserValidator validator;
    private final UserRepository repository;
    private final EmailService email;
    private final PasswordEncoder encoder;

    public UserService(UserValidator v, UserRepository r, EmailService e, PasswordEncoder p) {
        this.validator = v; this.repository = r; this.email = e; this.encoder = p;
    }

    public void registerUser(String username, String mail, String pwd) {
        validator.validate(mail, pwd);
        repository.save(new User(username, mail, encoder.encode(pwd)));
        email.sendWelcome(mail);
    }
}
```
</details>

> 🧭 **Litmus test** → _"How many reasons does this class have to change?"_  More than one? **Split it.**

---

<div align="center">

# 🟠 2 · OPEN/CLOSED PRINCIPLE

</div>

> ### 💡 _"Software should be **open for extension**, but **closed for modification**."_

> 🎯 **The problem it solves**
> Every new requirement (a new payment type, a new report format) forces you to crack open existing, working code. Each edit risks introducing bugs into features that were already tested and stable — and the `if/else` chains keep growing.

> 🛠️ **How it solves it**
> By coding against an abstraction (interface) and adding new behavior as *new classes* rather than edits. Existing code stays sealed and stable; you extend the system by plugging in new implementations. New feature = new file, not a risky change.

```text
   The PaymentProcessor never changes.
   New payment types just plug in. 🔌

                    ┌─────────────────────┐
                    │   PaymentProcessor  │
                    └──────────┬──────────┘
                               │ depends on
                       ┌───────▼────────┐
                       │ «PaymentMethod» │   ◀── the abstraction
                       └───────┬────────┘
            ┌──────────┬───────┼────────┬────────────┐
            ▼          ▼       ▼         ▼            ▼
       CreditCard   PayPal  Bitcoin   ApplePay   (add more...)
                                       ＋ NEW       no edits!  ✨
```

<table>
<tr><th>🔴 BEFORE — edit every time</th><th>🟢 AFTER — extend freely</th></tr>
<tr valign="top">
<td>

```java
class PaymentProcessor {
  void process(String type, double amt){
    if (type.equals("CREDIT_CARD")) {
      // ...
    } else if (type.equals("PAYPAL")) {
      // ...
    } else if (type.equals("BITCOIN")) {
      // ...
    }
    // new type? edit here 😩
  }
}
```

</td>
<td>

```java
interface PaymentMethod {
  void process(double amount);
}

class CreditCard implements PaymentMethod {
  public void process(double amt){ /*...*/ }
}
class PayPal implements PaymentMethod {
  public void process(double amt){ /*...*/ }
}

class PaymentProcessor {
  void process(PaymentMethod m, double amt){
    m.process(amt);   // never changes 🎉
  }
}
```

</td>
</tr>
</table>

> 🧩 **Pattern alert** → this is the **Strategy Pattern**, the cleanest way to honor OCP.

---

<div align="center">

# 🟣 3 · LISKOV SUBSTITUTION PRINCIPLE

</div>

> ### 💡 _"Subtypes must be replaceable for their base types — **without surprises**."_

> 🎯 **The problem it solves**
> Inheritance can lie. A subclass may look like its parent but secretly break the parent's contract (like `Square` redefining how width/height work). Code that trusts the base type then fails in unexpected ways, and polymorphism becomes a trap instead of a tool.

> 🛠️ **How it solves it**
> By insisting every subtype honors the behavior promised by its base type — same inputs, no weakened guarantees, no surprise exceptions. When that's not natural, model behavior with interfaces or composition instead of forcing inheritance. Substitution stays safe and predictable.

```text
   ❌ THE TRAP                              ✅ THE FIX
   ──────────                              ──────────
   Rectangle                               «Shape» { area() }
      ▲                                        ▲        ▲
      │ extends                                │        │
   Square  ──▶ overrides setters          Rectangle   Square
      │        breaks area() 💥           (own area)  (own area)
      ▼                                   no inheritance trap ✨
   resizeTest(rect) returns 16, not 20
```

<details>
<summary>🔴 &nbsp;<b>BAD — the classic Rectangle / Square trap</b></summary>

```java
class Rectangle {
    protected int width, height;
    public void setWidth(int w)  { width = w; }
    public void setHeight(int h) { height = h; }
    public int area() { return width * height; }
}

class Square extends Rectangle {
    public void setWidth(int w)  { width = w; height = w; }  // 🚨 surprise!
    public void setHeight(int h) { width = h; height = h; }  // 🚨 surprise!
}

void resizeTest(Rectangle r) {
    r.setWidth(5);
    r.setHeight(4);
    assert r.area() == 20;   // ✅ Rectangle → 20   ❌ Square → 16
}
```
</details>

<details open>
<summary>🟢 &nbsp;<b>GOOD — model behavior, not hierarchy</b></summary>

```java
interface Shape { int area(); }

class Rectangle implements Shape {
    private final int width, height;
    Rectangle(int w, int h) { width = w; height = h; }
    public int area() { return width * height; }
}

class Square implements Shape {
    private final int side;
    Square(int side) { this.side = side; }
    public int area() { return side * side; }
}

void printArea(Shape s) { System.out.println(s.area()); }  // works for ANY shape
```
</details>

> ⚠️ **Red flag** → if a subclass throws `UnsupportedOperationException` or weakens a promise, you're breaking LSP.

---

<div align="center">

# 🟢 4 · INTERFACE SEGREGATION PRINCIPLE

</div>

> ### 💡 _"No client should be forced to depend on methods it **never uses**."_

> 🎯 **The problem it solves**
> A big "do-everything" interface forces implementers to provide methods they don't need — often as empty bodies or thrown exceptions (like a `Robot` forced to `eat()`). Clients get coupled to behavior irrelevant to them, and any change to the fat interface ripples across every implementer.

> 🛠️ **How it solves it**
> By breaking large interfaces into small, role-based ones. Each class implements only the contracts it genuinely fulfills. No dead methods, no fake implementations, and changes to one capability no longer disturb unrelated classes.

```text
   ❌ ONE FAT INTERFACE                     ✅ SMALL FOCUSED INTERFACES
   ────────────────────                     ───────────────────────────
   «Worker»                                 «Workable»  «Eatable»  «Sleepable»
   ├ work()                                     ▲    ▲      ▲          ▲
   ├ eat()    ◀─ Robot forced to throw 💥        │    └──┬───┘          │
   └ sleep()  ◀─ Robot forced to throw 💥     Robot      Human ─────────┘
                                              (work)     (work, eat, sleep)
                                              only what it needs ✨
```

<table>
<tr><th>🔴 Fat interface</th><th>🟢 Segregated</th></tr>
<tr valign="top">
<td>

```java
interface Worker {
  void work();
  void eat();    // robots can't!
  void sleep();  // robots can't!
}

class Robot implements Worker {
  public void work()  { /* ok */ }
  public void eat()   { throw new
    UnsupportedOperationException(); }
  public void sleep() { throw new
    UnsupportedOperationException(); }
}
```

</td>
<td>

```java
interface Workable  { void work(); }
interface Eatable   { void eat(); }
interface Sleepable { void sleep(); }

class Human implements
    Workable, Eatable, Sleepable {
  public void work()  {}
  public void eat()   {}
  public void sleep() {}
}

class Robot implements Workable {
  public void work() {}   // just this ✨
}
```

</td>
</tr>
</table>

> 🗃️ **Backend tip** → split bloated repos into `ReadRepository`, `WriteRepository`, `SearchableRepository` so read-only consumers never inherit write/maintenance methods.

---

<div align="center">

# 🔵 5 · DEPENDENCY INVERSION PRINCIPLE

</div>

> ### 💡 _"Depend on **abstractions**, not concretions. **Inject**, don't instantiate."_

> 🎯 **The problem it solves**
> When high-level business logic creates its own concrete dependencies (`new MySQLDatabase()`), it becomes welded to a specific implementation. Swapping the database, mocking it in tests, or reusing the logic elsewhere all become painful. Low-level details dictate the design.

> 🛠️ **How it solves it**
> By making both high-level and low-level code depend on a shared abstraction (interface), and injecting the concrete implementation from outside (usually via the constructor). The business logic no longer cares *which* database it talks to — you can swap MySQL, Postgres, or a mock freely.

```text
            ┌──────────────┐
            │  UserService │
            └──────┬───────┘
                   │ depends on
            ┌──────▼────────┐
            │  «Database»   │   ◀── the abstraction (interface)
            └──────┬────────┘
        ┌──────────┼───────────┐
        ▼          ▼           ▼
     MySQL     PostgreSQL   MockDB 🧪
   (prod)        (prod)     (tests — swap in instantly)
```

<details>
<summary>🔴 &nbsp;<b>BAD — hard-wired to MySQL</b></summary>

```java
public class UserRepository {
    private MySQLDatabase db = new MySQLDatabase();   // 🔒 locked in
    public User getUser(Long id) {
        db.connect();
        return db.query("SELECT * FROM users WHERE id=" + id);
    }
}
// Switching DBs or unit testing? Painful.
```
</details>

<details open>
<summary>🟢 &nbsp;<b>GOOD — inject an abstraction</b></summary>

```java
public interface Database {
    void connect();
    String query(String sql);
}

public class MySQLDatabase      implements Database { /* ... */ }
public class PostgreSQLDatabase implements Database { /* ... */ }

public class UserRepository {
    private final Database db;              // depends on interface
    public UserRepository(Database db) {    // injected via constructor
        this.db = db;
    }
    public User getUser(Long id) {
        db.connect();
        return parse(db.query("SELECT * FROM users WHERE id=" + id));
    }
}

// Swap freely — zero changes to UserRepository:
new UserRepository(new MySQLDatabase());
new UserRepository(new PostgreSQLDatabase());
new UserRepository(new MockDatabase());     // 🧪 instant testability
```
</details>

> 🌱 **Spring loves this** → constructor injection + interfaces = idiomatic Spring. DIP is baked into the framework.

---

<div align="center">

# 🛒 REAL-WORLD: ALL FIVE TOGETHER

### _E-Commerce Order Processing_

</div>

```text
   Client                                                    Result
     │                                                         ▲
     │  createOrder(items, payment)                            │
     ▼                                                         │
  ┌──────────────┐   validate   ┌────────────────┐            │
  │ OrderService │ ───────────▶ │ OrderValidator │  ✅         │
  └──────┬───────┘              └────────────────┘            │
         │  process(amount)     ┌────────────────────┐        │
         ├────────────────────▶ │ PaymentProcessor   │  ✅     │
         │  reserveStock(items) ┌────────────────────┐        │
         ├────────────────────▶ │ InventoryManager   │        │
         │  sendConfirmation    ┌────────────────────┐        │
         ├────────────────────▶ │ NotificationService│        │
         │                                                     │
         └──────────────── 🎉 Order CONFIRMED ─────────────────┘
```

<details>
<summary>📦 &nbsp;<b>Show the wiring (interfaces + orchestrator)</b></summary>

```java
// 🔵 DIP: every dependency is an abstraction
public interface ProductRepository   { Product findById(Long id); void updateStock(Long id, int qty); }
public interface OrderRepository      { void save(Order order); }
public interface PaymentProcessor     { PaymentResult process(double amount, String details); }
public interface NotificationService  { void sendConfirmation(String userId, Order order); }

// 🔴 SRP: one job — validation only
public class OrderValidator {
    private final ProductRepository products;
    public OrderValidator(ProductRepository p) { this.products = p; }
    public void validate(List<OrderItem> items) {
        if (items.isEmpty()) throw new IllegalArgumentException("Empty order");
        items.forEach(i -> {
            Product p = products.findById(i.getProduct().getId());
            if (p.getStockQuantity() < i.getQuantity())
                throw new IllegalArgumentException("Insufficient stock: " + p.getName());
        });
    }
}

// 🟠 OCP + 🔵 DIP: orchestrator depends only on interfaces
public class OrderService {
    private final OrderValidator validator;
    private final PaymentProcessor payment;
    private final InventoryManager inventory;
    private final OrderRepository orders;
    private final NotificationService notifier;

    public OrderService(OrderValidator v, PaymentProcessor p, InventoryManager i,
                        OrderRepository o, NotificationService n) {
        this.validator = v; this.payment = p; this.inventory = i;
        this.orders = o; this.notifier = n;
    }

    public Order createOrder(String userId, List<OrderItem> items, String paymentDetails) {
        validator.validate(items);
        Order order = new Order(System.currentTimeMillis(), userId, items, OrderStatus.PENDING);
        if (!payment.process(order.getTotalAmount(), paymentDetails).isSuccess())
            throw new RuntimeException("Payment failed");
        inventory.reserveStock(items);
        order.setStatus(OrderStatus.CONFIRMED);
        orders.save(order);
        notifier.sendConfirmation(userId, order);
        return order;
    }
}
```
</details>

#### 🔍 Spot each principle in the design

| | Principle | Where it lives here |
|:-:|:----------|:--------------------|
| 🔴 | **S** | `OrderValidator` & `InventoryManager` each do exactly one thing |
| 🟠 | **O** | Add `BitcoinPaymentProcessor` → `OrderService` stays untouched |
| 🟣 | **L** | Any `PaymentProcessor` substitutes cleanly |
| 🟢 | **I** | Repositories expose only the methods needed |
| 🔵 | **D** | `OrderService` depends on interfaces, never on concrete classes |

---

<div align="center">

# 🌱 SPRING & SOLID

</div>

Spring is basically **SOLID with batteries included**:

| | SOLID | How Spring helps |
|:-:|:-----:|:-----------------|
| 🔴 | **S** | `@Service`, `@Repository`, `@Controller` enforce separation |
| 🟠 | **O** | `@Profile` + beans swap behavior without touching code |
| 🟣 | **L** | Interface-based beans stay substitutable |
| 🟢 | **I** | Spring Data exposes small, focused repository interfaces |
| 🔵 | **D** | Constructor injection **is** DIP, out of the box |

<details>
<summary>🧰 &nbsp;<b>Show idiomatic Spring wiring</b></summary>

```java
@Service
public class OrderService {
    private final OrderRepository repository;
    private final PaymentProcessor payment;

    // Constructor injection = DIP (no @Autowired needed since Spring 4.3+)
    public OrderService(OrderRepository repository, PaymentProcessor payment) {
        this.repository = repository;
        this.payment = payment;
    }
}

@Configuration
public class PaymentConfig {
    @Bean @Profile("production")
    PaymentProcessor stripe() { return new StripePaymentProcessor(); }

    @Bean @Profile({"dev", "test"})
    PaymentProcessor mock() { return new MockPaymentProcessor(); }
}
```
</details>

> ✅ **Best practice** → prefer **constructor injection** over field injection. Dependencies become explicit and the class is trivially unit-testable.

---

<div align="center">

# ⚠️ COMMON PITFALLS

</div>

| | Pitfall | Breaks | Fix |
|:-:|:--------|:------:|:----|
| 🦸 | God class doing everything | **S** | Split into focused classes |
| 🪜 | Long `if/else` on a "type" field | **O** | Polymorphism / Strategy |
| 💥 | Subclass throws `UnsupportedOperationException` | **L** | Rethink hierarchy / use composition |
| 🐘 | Fat interface with unused methods | **I** | Break into small interfaces |
| 🔒 | `new ConcreteClass()` in business logic | **D** | Inject an interface |
| 🏗️ | Abstractions for trivial one-liners | _over-engineering_ | Keep it simple — refactor when it hurts |

> 🧠 **Golden rule** → SOLID is a set of **guidelines, not laws**. Apply them when complexity earns them.

---

<div align="center">

# 📋 CHEAT SHEET

_Pin this above your desk, Rachit._

</div>

```text
   ┌────┬──────┬─────────────────────────────────┬──────────────────────┐
   │ 🔴 │ SRP  │ "More than one reason to change?"│ Extract classes      │
   ├────┼──────┼─────────────────────────────────┼──────────────────────┤
   │ 🟠 │ OCP  │ "New feature = editing old code?"│ Strategy / Factory   │
   ├────┼──────┼─────────────────────────────────┼──────────────────────┤
   │ 🟣 │ LSP  │ "Child replaces parent safely?"  │ Composition          │
   ├────┼──────┼─────────────────────────────────┼──────────────────────┤
   │ 🟢 │ ISP  │ "Forced to implement dead code?" │ Split interfaces     │
   ├────┼──────┼─────────────────────────────────┼──────────────────────┤
   │ 🔵 │ DIP  │ "Using 'new' for a dependency?"  │ Constructor injection│
   └────┴──────┴─────────────────────────────────┴──────────────────────┘
```

---

<div align="center">

# 🧪 TEST YOURSELF

_Think first, then click to reveal._ 👀

</div>

<details>
<summary>&nbsp;<b>Q1.</b> &nbsp;A <code>ReportGenerator</code> uses <code>if(type=="PDF")... else if(type=="CSV")...</code>. Which principle breaks?</summary>

<br/>

> **Breaks:** 🟠 Open/Closed Principle.
> **Fix:** a `ReportGenerator` interface with `PdfReport`, `CsvReport`, `ExcelReport`. New format = new class, zero edits.
</details>

<details>
<summary>&nbsp;<b>Q2.</b> &nbsp;Your <code>Robot</code> implements <code>Worker.eat()</code> by throwing an exception. What's wrong?</summary>

<br/>

> **Breaks:** 🟢 Interface Segregation (and arguably 🟣 LSP).
> **Fix:** split `Worker` into `Workable`, `Eatable`, `Sleepable`. `Robot` implements only `Workable`.
</details>

<details>
<summary>&nbsp;<b>Q3.</b> &nbsp;A service does <code>new MySQLDatabase()</code> in its constructor. How do you make it testable?</summary>

<br/>

> **Breaks:** 🔵 Dependency Inversion.
> **Fix:** define a `Database` interface and inject it. In tests, pass a `MockDatabase`.
</details>

---

<div align="center">

# 📚 KEEP LEARNING

</div>

| Resource | Type |
|:---------|:----:|
| _Clean Code_ — Robert C. Martin | 📖 |
| _Clean Architecture_ — Robert C. Martin | 📖 |
| _Effective Java_ — Joshua Bloch | 📖 |
| [Baeldung — SOLID Guides](https://www.baeldung.com/solid-principles) | 🌐 |
| [Spring Framework Docs](https://spring.io/projects/spring-framework) | 🌐 |

---

<div align="center">

### 🚀 The Workflow to Live By

```text
   MAKE IT WORK  ──▶  MAKE IT RIGHT  ──▶  MAKE IT FAST
     (get it          (refactor to       (optimize only
      running)         SOLID)             if needed)
```

⭐ _Notes prepared for **Rachit**. Revisit before every design review._ ⭐

</div>
