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
