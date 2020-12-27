Sample application to reproduce "A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance" bug with hibernate-enhance-maven-plugin

Without hibernate-enhance-maven-plugin appliaction works correctly. After executing command:
```mvn clean compile exec:java```
The output should looks like:
```
Hibernate: drop table if exists Child2 CASCADE
Hibernate: drop table if exists Parent CASCADE
Hibernate: create table Child1 (id bigint not null, name varchar(255), parent_id bigint, primary key (id))
gru 27, 2020 10:37:36 AM org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl getIsolatedConnection
INFO: HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@31094271] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Hibernate: create table Child2 (id bigint not null, name varchar(255), parent_id bigint, primary key (id))
Hibernate: create table Parent (id bigint not null, name varchar(255), primary key (id))
Hibernate: alter table Child1 add constraint FKdnj7ch56fyiumxg9ilg8fprha foreign key (parent_id) references Parent
Hibernate: alter table Child2 add constraint FK7wsyd8r8p6ll7eagnhcw14xyw foreign key (parent_id) references Parent
gru 27, 2020 10:37:36 AM org.hibernate.tool.schema.internal.exec.AbstractScriptSourceInput prepare
INFO: HHH000476: Executing import script 'file:/D:/junk/hibernateenhancetest/target/classes/META-INF/import.sql'
Hibernate: insert into parent(id, name) values (1, 'name')
gru 27, 2020 10:37:36 AM org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformInitiator initiateService
INFO: HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
Initial data
Hibernate: select parent0_.id as id1_2_, parent0_.name as name2_2_ from Parent parent0_
Hibernate: select child1x0_.parent_id as parent_i3_0_0_, child1x0_.id as id1_0_0_, child1x0_.id as id1_0_1_, child1x0_.name as name2_0_1_, child1x0_.parent_id as parent_i3_0_1_ from Child1 child1x0_ where child1x0_.parent_id=?
[Parent [id=1, name=name, child1=[]]]
Hibernate: select child2x0_.parent_id as parent_i3_1_0_, child2x0_.id as id1_1_0_, child2x0_.id as id1_1_1_, child2x0_.name as name2_1_1_, child2x0_.parent_id as parent_i3_1_1_ from Child2 child2x0_ where child2x0_.parent_id=?
Hibernate: update Parent set name=? where id=?
Data after merge
Hibernate: select parent0_.id as id1_2_, parent0_.name as name2_2_ from Parent parent0_
[Parent [id=1, name=new name, child1=[]]]
```
With hibernate-enhance-maven-plugin turned on after execute command
```mvn clean compile exec:java -Penhance```
application throws exception
```
javax.persistence.RollbackException: Error while committing the transaction
        at org.hibernate.internal.ExceptionConverterImpl.convertCommitException(ExceptionConverterImpl.java:81)
        at org.hibernate.engine.transaction.internal.TransactionImpl.commit(TransactionImpl.java:104)
        at Main.main(Main.java:32)
        at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:254)
        at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: javax.persistence.PersistenceException: org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: entities.Parent.child2
        at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:154)
        at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:181)
        at org.hibernate.internal.ExceptionConverterImpl.convertCommitException(ExceptionConverterImpl.java:65)
        ... 4 more
Caused by: org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: entities.Parent.child2
        at org.hibernate.engine.internal.Collections.processDereferencedCollection(Collections.java:100)
        at org.hibernate.engine.internal.Collections.processUnreachableCollection(Collections.java:51)
        at org.hibernate.event.internal.AbstractFlushingEventListener.lambda$flushCollections$1(AbstractFlushingEventListener.java:255)
        at org.hibernate.engine.internal.StatefulPersistenceContext.forEachCollectionEntry(StatefulPersistenceContext.java:1136)
        at org.hibernate.event.internal.AbstractFlushingEventListener.flushCollections(AbstractFlushingEventListener.java:252)
        at org.hibernate.event.internal.AbstractFlushingEventListener.flushEverythingToExecutions(AbstractFlushingEventListener.java:93)
        at org.hibernate.event.internal.DefaultFlushEventListener.onFlush(DefaultFlushEventListener.java:39)
        at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:102)
        at org.hibernate.internal.SessionImpl.doFlush(SessionImpl.java:1362)
        at org.hibernate.internal.SessionImpl.managedFlush(SessionImpl.java:453)
        at org.hibernate.internal.SessionImpl.flushBeforeTransactionCompletion(SessionImpl.java:3212)
        at org.hibernate.internal.SessionImpl.beforeTransactionCompletion(SessionImpl.java:2380)
        at org.hibernate.engine.jdbc.internal.JdbcCoordinatorImpl.beforeTransactionCompletion(JdbcCoordinatorImpl.java:447)
        at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.beforeCompletionCallback(JdbcResourceLocalTransactionCoordinatorImpl.java:183)
        at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.access$300(JdbcResourceLocalTransactionCoordinatorImpl.java:40)
        at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.commit(JdbcResourceLocalTransactionCoordinatorImpl.java:281)
        at org.hibernate.engine.transaction.internal.TransactionImpl.commit(TransactionImpl.java:101)
        ... 3 more
```
