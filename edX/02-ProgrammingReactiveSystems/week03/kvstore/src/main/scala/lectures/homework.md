# Distributed Key Value Store
[Description](https://courses.edx.org/courses/course-v1:EPFLx+scala-reactiveX+2T2019/courseware/0cef6ee9d8794d9b9203638ffc642dc9/29a6d5c11cdc4725bf290c2cc10104c2/1?activate_block_id=block-v1%3AEPFLx%2Bscala-reactiveX%2B2T2019%2Btype%40vertical%2Bblock%40d974537dc5174f349100339a96f7cf3b)

## Step 1

Implement the primary replica role so that it correctly responds to the KV protocol messages without considering persistence or replication.

1. Join Commands
   - New replicas must first send a Join message to the Arbiter signaling that they are ready to be used.
   - The Join message will be answered by either a JoinedPrimary or JoinedSecondary message indicating the role of the new node; the answer will be sent to the sender of the Join message. The first node to join will get the primary role, other subsequent nodes are assigned the secondary role.
   - The arbiter will send a Replicas message to the primary replica whenever it receives the Join message; for this reason the sender of the Join message must be the replica Actor itself. This message contains the set of available replica nodes including the primary and all the secondaries.
   - All messages sent by the Arbiter will have the Arbiter as their sender.
1. Update Commands
   - Insert(key, value, id) - This message instructs the primary to insert the (key, value) pair into the storage and replicate it to the secondaries: id is a client-chosen unique identifier for this request.
   - Remove(key, id) - This message instructs the primary to remove the key (and its corresponding value) from the storage and then remove it from the secondaries.
   - A successful Insert or Remove results in a reply to the client (precisely: to the sender of the update command) in the form of an OperationAck(id) message where the id field matches the corresponding id field of the operation that has been acknowledged.
   - A failed Insert or Remove command results in an OperationFailed(id) reply. A failure is defined as the inability to confirm the operation within 1 second. See the sections on replication and persistence below for more details.

1. Lookup Commands
   - Get(key, id) - Instructs the replica to look up the "current" (what current means is described in detail in the next section) value assigned with the key in the storage and reply with the stored value.
   - A Get operation results in a GetResult(key, valueOption, id) message to be sent back to the sender of the lookup request where the id field matches the value in the id field of the corresponding Get message. The valueOption field should contain None if the key is not present in the replica or Some(value) if a value is currently assigned to the given key in that replica.

**All replies sent by the Replica shall have that Replica as their sender**.

## Step 2
Implement the secondary replica role so that it correctly responds to the read-only part of the KV protocol and accepts the replication protocol, without considering persistence.

## Step 3

Implement the replicator so that it correctly mediates between replication requests, snapshots and acknowledgements

### The Replication Protocol
Apart from providing the KV protocol for external clients, you must implement another protocol involving the primary and secondary replicas and some newly introduced helper nodes. The KV store will use this protocol to synchronize its state between nodes.

When a new replica joins the system, the primary receives a new Replicas message and must allocate a new actor of type Replicator for the new replica; when a replica leaves the system its corresponding Replicator must be terminated. The role of this Replicator actor is to accept update events, and propagate the changes to its corresponding replica (i.e. there is exactly one Replicator per secondary replica). Also, notice that at creation time of the Replicator, the primary must forward update events for every key-value pair it currently holds to this Replicator.

Your task for this protocol will be to provide an Actor representing a Replicator. Its declaration is:

```scala
class Replicator(val replica: ActorRef) extends Actor
```

The replication protocol includes two pairs of messages.

1. Is used by the replica actor which requests replication of an update:
   - Replicate(key, valueOption, id) is sent to the Replicator to initiate the replication of the given update to the key; in case of an Insert operation the valueOption will be Some(value) while in case of a Remove operation it will be None. The sender of the Replicate message shall be the Replica itself.
   - Replicated(key, id) is sent as a reply to the corresponding Replicate message once replication of that update has been successfully completed (see SnapshotAck). The sender of the Replicated message shall be the Replicator.
1. Is used by the replicator when communicating with its partner replica:
   - Snapshot(key, valueOption, seq) is sent by the Replicator to the appropriate secondary replica to indicate a new state of the given key. valueOption has the same meaning as for Replicate messages. The sender of the Snapshot message shall be the Replicator.

