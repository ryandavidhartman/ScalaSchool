package faulttolerance2.exception

@SerialVersionUID(1L)
class DiskError(msg: String) extends Error(msg) with Serializable
