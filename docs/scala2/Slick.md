## Introduction
There are numerous libraries to make SQL calls to databases using JDBC (_Java Database Connectivity_), which is the standard API in the JVM world for relational databases access, and at ANGI we use Slick for our Scala apps.

[Slick](http://slick.lightbend.com) is is a database query and access library for Scala that provides an abstraction over JDBC.  Slick is not a ORM (Object-Relational Mapper) like Hibernate. Think of it more as a Scala DSL for JDBC that is *type safe*, *functional* and *asynchronous*

Slick lets you access data from a relational database, and the experience is like dealing with *collections* rather than a database.  Although, in the background, it auto-generates SQL queries.

For example, consider the following lines of code:
<!-- code -->
```scala
    val q = for {
      user <- users if user.name == "bob@gmail.com"
    } yield (user.name, user.creationTime)
```
Under the covers, this generates the following SQL command:

<!-- code -->
```sql
select user.name, user.creationTime from users  
where user.name='bob@gmail.com' 
```
## Using Slick to manually write SQL

Often there are advantages in interacting with the DB via the familiar collection API. However, there are times (e.g. code clarity or performance) when it is desirable create your own SQL directly. 
To this end Slick also provides an API to access a database using plain old SQL. 

There are several ways to directly make sql statements using Slick, probably the simplest is to using the `sqlu` interpolator to make a prepared statement from with a literal SQL string:

<!-- code -->
```scala
  def createCoffees: DBIO[Int] =
    sqlu"""create table coffees(
      name varchar not null,
      sup_id int not null,
      price double not null,
      sales int not null,
      total int not null,
      foreign key(sup_id) references suppliers(id))"""

  def createSuppliers: DBIO[Int] =
    sqlu"""create table suppliers(
      id int not null primary key,
      name varchar not null,
      street varchar not null,
      city varchar not null,
      state varchar not null,
      zip varchar not null)"""

  def insertSuppliers: DBIO[Unit] = DBIO.seq(
    // Insert some suppliers
    sqlu"insert into suppliers values(101, 'Acme, Inc.', '99 Market Street', 'Groundsville', 'CA', '95199')",
    sqlu"insert into suppliers values(49, 'Superior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460')",
    sqlu"insert into suppliers values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966')"
  )
```
## Fundamental concepts:
In Slick there are five fundamental concepts we nee to understand: `Tables`, `Queries`, `Actions`, `Joins` and `Profiles`
1. Tables are a way of defining relationships between Scala types and the Database tables
1. Queries is a Scala DSL for building SQL
1. Actions allow us to sequence queries together and send them to the DB transactionally
1. Joins a facility to aggregate data from multiple sources
1. Profiles or drivers are Slicks abstraction over different backend DB implementations (and their capabilities) allowing us to interact with them in a generic way

## Tables
In Slick `Tables` are a way of defining relationships between Scala types and the Database tables

Here we see the mapping between the `Coffee` case class and the Slick representation of the coffee table in the DB:

<!-- code -->
```scala
case class Coffee(id: Long = 0, name: String)

class CoffeeTable(tag: Tag) extends Table[Coffee](tag, "COFFEES") {
  def id: Rep[Long] = column[Long]("COF_ID", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("COF_NAME")
  
  def * : ProvenShape[Coffee] = (id, name) <> (Coffee.tupled, Coffee.unapply)
}
```
The `*` method is the so-called default projection for the table.  It defines how to take a tuple of `(id, name)` which are returned on DB reads convert it to an instance of the `Coffee` case class.  The the reverse for writes.  The `<>` (or bi-directional mapping operator) is optimized for case classes (with a simple apply method and an unapply method that wraps its result in an Option) but it can also be used with arbitrary mapping functions.  

## Queries
A Scala DSL for building SQL.  Actions are a way of grouping/ordering SQL and all of this operates in the run block provided by a db profile:

<!-- code -->
```scala
    db.run(
      CoffeeTable
         .filter(_.price > 8.0)  // Here we create a query
         .result // Here we convert it to an action 
     )
```

### Look at some of the Select Query Combinators
<!-- code -->
```scala
  lazy val suppliers: TableQuery[SupplierTable] = TableQuery[SupplierTable]

  val selectAllQuery = suppliers  //This is the basic select all query
  SELECT *
  FROM SUPPLIERS;

  val selectWhereQuery = suppliers  //select with a where clause
    .filter(_.state === "Indiana")
  SELECT *
  FROM SUPPLIERS
  WHERE state = 'Indiana';

  val selectSortedQuery1 = suppliers  // select with a sort
      .sortBy(_.name.asc)
  SELECT *
  FROM SUPPLIERS
  ORDER BY name ASC;

  val selectSortedQuery2 = suppliers  // another sort select
      .sortBy(s => (s.state.asc, s.city.asc))
  SELECT *
  FROM SUPPLIERS
  ORDER BY name ASC, city ASC;

  val selectPagedQuery = suppliers  //paged select
      .drop(2).take(1)
  SELECT *
  FROM SUPPLIERS
  OFFSET 2 LIMIT 1;

  val selectColumnsQuery1 = suppliers  //just select a single column
      .map(_.name)
  SELECT name
  FROM SUPPLIERS;

  val selectColumnsQuery2 = suppliers  //select two columns
      .map(s => (s.name, s.state))
 SELECT name, state
 FROM SUPPLIERS;

  val selectCombinedQuery = suppliers  //applying two sql operations
      .filter(_.state === "Indiana")
      .map(_.name)
 SELECT name
 FROM SUPPLIERS
 WHERE state = 'Indiana'
```


## Actions
Allow us to sequence queries together and send them to the DB transactionally

## Joins
A facility to pull data from multiple sources

## Profiles
Profiles or drivers are Slicks abstraction of supporting backend different DB implementations and their capabilities in a generic way.
 

## Play evolutions
When you are using Slick in a [Play](https://github.com/ryandavidhartman/ScalaSchool/wiki/Play) application you can use the play-slick evolutions plugin to manage your DB schema.

The Play app conf folder structure looks like:

    ├── conf
    │ ├── application.conf
    │ ├── evolutions
    │ │ └── default
    │ │ └── 1.sql
    │ └── routes

By convention, we will create the 1.sql file in the conf/evolutions/{database-name}/ directory. In our case, it is in the conf/evolutions/default/ folder.

The default folder signifies the database namespace to be used while executing 1.sql. The tables created in evolutions/default/1.sql will be persisted in the default database. Please refer to the following code:

<!-- code -->
```sql

    # --- !Ups 
 
    create table users (email VARCHAR NOT NULL PRIMARY KEY,passwdHash  
    VARCHAR NOT NULL, creationTime BIGINT NOT NULL ); 
 
    create table tokens(key VARCHAR NOT NULL  PRIMARY KEY , token
    VARCHAR NOT NULL UNIQUE , validTill BIGINT NOT NULL) 
 
    # --- !Downs 
 
    drop table users; 
    drop table tokens;
``` 

The !Ups contents of this file will be executed only the first time the application starts. It will not be executed if the application restarts in the future (unless the file content gets changed).

The way it works is as follows:

Play creates a meta-table `PLAY_EVOLUTIONS` in our database where it maintains the meta information
In future, if we create the 2.sql file in the evolutions/default folder, then, during the next start-up of the application:
It will execute the !Ups section of the 2.sql file.
Suppose, in later releases, the contents of 2.sql have changed. In such cases, play will execute the !Downs section of the 2.sql file and then !Ups on 2.sql to ensure that the database is in sync with the codebase.
If the contents of 2.sql do not change, no query will be executed from the file.
The Play evolution plugin will execute 3.sql in case it is created in future.

## Database Configuration

<!-- code -->
```scala
default = {
  url = "jdbc:h2:mem:test1"
  driver = org.h2.Driver
  connectionPool = disabled
  keepAliveConnection = true
}
```


In our application.conf, we provide the driver and database settings to Slick. Notice the word `default` in the preceding statements, which represent the connection to the default namespace of db.

## Examples
[Here](https://github.com/ryandavidhartman/SlickTutorial) are some code showing basic Scala Slick examples