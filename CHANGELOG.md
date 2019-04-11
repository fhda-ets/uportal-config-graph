# Changelog

## 2.0 (April 2019)

### Highlights

* Dropped JPA and Hibernate entity management
* New `EntityProvider` API to allow the query and storage backend to be completely pluggable
* Changed all uses of entity objects to work entirely through the API and interfaces rather than being pinned to a vendor specific implementation
* Added backend implementation for Hazelcast
* Dependencies updated

### Details

__JPA/Hibernate Removal:__ ORMs definitely have their place, but for such a simple entity model, it felt a bit overkill. All that is needed is a simple, fast backend with some query capabilities. I have added Hazelcast for this round, but there are many excellent databases that are more object oriented (Mongo, DynamoDB) or KV stores such as Redis which more recently now can be given indexing and querying superpowers.

__APIs:__ Virtually any kind of storage backend can be implemented using the `EntityProvider` API. Uses Spring profiles to activate a specific provider at runtime.

__Hazelcast Provider:__ Hazelcast is battle-tested data grid with querying and indexing, very simple to embed, and highly configurable. This is a very good place to start using Config Graph across a multi-node highly available portal deployment.

## 1.0 (January 2019)

* Initial version using Spring Boot JPA and Hibernate entities backed by an RDBMS